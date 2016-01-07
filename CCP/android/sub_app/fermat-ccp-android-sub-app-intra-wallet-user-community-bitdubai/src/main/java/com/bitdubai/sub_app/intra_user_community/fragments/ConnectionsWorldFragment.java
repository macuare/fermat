package com.bitdubai.sub_app.intra_user_community.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.interfaces.IntraUserWalletSettings;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.exceptions.CantGetActiveLoginIdentityException;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.exceptions.CantGetIntraUsersListException;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.interfaces.IntraUserInformation;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.interfaces.IntraUserModuleManager;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.interfaces.IntraUserSearch;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.sub_app.intra_user_community.R;
import com.bitdubai.sub_app.intra_user_community.adapters.AppListAdapter;
import com.bitdubai.sub_app.intra_user_community.common.popups.PresentationIntraUserCommunityDialog;
import com.bitdubai.sub_app.intra_user_community.constants.Constants;
import com.bitdubai.sub_app.intra_user_community.session.IntraUserSubAppSession;
import com.bitdubai.sub_app.intra_user_community.util.CommonLogger;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * Created by Matias Furszyfer on 15/09/15.
 * modified by Jose Manuel De Sousa Dos Santos on 08/12/2015
 */

public class ConnectionsWorldFragment extends AbstractFermatFragment implements
        AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, FermatListItemListeners<IntraUserInformation>, SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    public static final String INTRA_USER_SELECTED = "intra_user";

    private static final int MAX = 20;
    /**
     * MANAGERS
     */
    private static IntraUserModuleManager moduleManager;
    private static ErrorManager errorManager;

    protected final String TAG = "Recycler Base";
    FermatWorker worker;
    private int offset = 0;
    private int mNotificationsCount = 0;
    private SearchView mSearchView;
    private AppListAdapter adapter;
    private boolean isStartList = false;

    //private ProgressDialog mDialog;
    // recycler
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    //private ActorAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

    // flags
    private boolean isRefreshing = false;
    private View rootView;
    private IntraUserSubAppSession intraUserSubAppSession;
    private String searchName;
    private LinearLayout emptyView;
    private ArrayList<IntraUserInformation> lstIntraUserInformations;
    private List<IntraUserInformation> dataSet = new ArrayList<>();
    private android.support.v7.widget.Toolbar toolbar;

    SettingsManager<IntraUserWalletSettings> settingsManager;
    IntraUserWalletSettings intraUserWalletSettings = null;
    /**
     * Create a new instance of this fragment
     *
     * @return InstalledFragment instance object
     */
    public static ConnectionsWorldFragment newInstance() {
        return new ConnectionsWorldFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setHasOptionsMenu(true);
            // setting up  module
            intraUserSubAppSession = ((IntraUserSubAppSession) appSession);
            moduleManager = intraUserSubAppSession.getModuleManager();
            errorManager = appSession.getErrorManager();

            settingsManager = intraUserSubAppSession.getModuleManager().getSettingsManager();

            try {
                intraUserWalletSettings = settingsManager.loadAndGetSettings(intraUserSubAppSession.getAppPublicKey());
            }catch (Exception e){
                intraUserWalletSettings = null;
            }

            if(intraUserWalletSettings == null){
                intraUserWalletSettings = new IntraUserWalletSettings();
                intraUserWalletSettings.setIsPresentationHelpEnabled(true);
                settingsManager.persistSettings(intraUserSubAppSession.getAppPublicKey(),intraUserWalletSettings);
            }
            mNotificationsCount = moduleManager.getIntraUsersWaitingYourAcceptanceCount();
            new FetchCountTask().execute();

        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, ex);
        }
    }


    /**
     * Fragment Class implementation.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            rootView = inflater.inflate(R.layout.fragment_connections_world, container, false);
            rootView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        worker.shutdownNow();
                        return true;
                    }
                    return false;
                }
            });
            toolbar = getToolbar();
            toolbar.setTitle("Cripto wallet users");
            setUpScreen(inflater);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.gridView);
            recyclerView.setHasFixedSize(true);
            layoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new AppListAdapter(getActivity(), lstIntraUserInformations);
            recyclerView.setAdapter(adapter);
            adapter.setFermatListEventListener(this);
            swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
            swipeRefresh.setOnRefreshListener(this);
            swipeRefresh.setColorSchemeColors(Color.BLUE, Color.BLUE);
            rootView.setBackgroundColor(Color.parseColor("#000b12"));
            emptyView = (LinearLayout) rootView.findViewById(R.id.empty_view);
            dataSet.addAll(moduleManager.getCacheSuggestionsToContact(MAX, offset));
            SharedPreferences pref = getActivity().getSharedPreferences(Constants.PRESENTATIO_DIALOG_CHECKED, Context.MODE_PRIVATE);
            if (intraUserWalletSettings.isPresentationHelpEnabled()) {
                showDialogHelp();
            } else {
                showCriptoUsersCache();
            }
        } catch (Exception ex) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(ex));
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    @Override
    public void onRefresh() {
        if (!isRefreshing) {
            isRefreshing = true;
            worker = new FermatWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    return getMoreData();
                }
            };
            worker.setContext(getActivity());
            worker.setCallBack(new FermatWorkerCallBack() {
                @SuppressWarnings("unchecked")
                @Override
                public void onPostExecute(Object... result) {
                    isRefreshing = false;
                    if (swipeRefresh != null)
                        swipeRefresh.setRefreshing(false);
                    if (result != null &&
                            result.length > 0) {
                        if (getActivity() != null && adapter != null) {
                            lstIntraUserInformations = (ArrayList<IntraUserInformation>) result[0];
                            adapter.changeDataSet(lstIntraUserInformations);
                            if (lstIntraUserInformations.isEmpty()) {
                                showEmpty(true, emptyView);

                            } else {
                                showEmpty(false, emptyView);
                            }
                        }
                    } else {
                        showEmpty(true, emptyView);
                    }
                }

                @Override
                public void onErrorOccurred(Exception ex) {
                    isRefreshing = false;
                    if (swipeRefresh != null)
                        swipeRefresh.setRefreshing(false);
                    if (getActivity() != null)
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();

                }
            });
            worker.execute();
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cripto_users_menu, menu);
        try {
            final MenuItem searchItem = menu.findItem(R.id.action_search);
            menu.findItem(R.id.action_help).setVisible(true);
            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.action_close).setVisible(false);
            searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    menu.findItem(R.id.action_help).setVisible(false);
                    menu.findItem(R.id.action_close).setVisible(true);
                    menu.findItem(R.id.action_search).setVisible(false);
                    toolbar = getToolbar();
                    toolbar.setTitle("");
                    return false;
                }
            });
            menu.findItem(R.id.action_close).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    menu.findItem(R.id.action_help).setVisible(true);
                    menu.findItem(R.id.action_close).setVisible(false);
                    menu.findItem(R.id.action_search).setVisible(true);
                    toolbar = getToolbar();
                    toolbar.setTitle("Cripto wallet users");
                    return false;
                }
            });

        } catch (Exception e) {

        }
       /* MenuItem menuItem = menu.add(0, IntraUserCommunityConstants.IC_ACTION_SEARCH, 0, "send");
        menuItem.setIcon(R.drawable.ic_action_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setActionProvider(new SearchView(getActivity()))*/


        //MenuItem action_connection_request = menu.findItem(R.id.action_connection_request);
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
       /* MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();*/

        // Update LayerDrawable's BadgeDrawable
        // Utils.setBadgeCount(getActivity(), icon, mNotificationsCount);


//        List<String> lst = new ArrayList<String>();
//        lst.add("Matias");
//        lst.add("Work");
//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lst);
//        MenuItem item = menu.findItem(R.id.spinner);
//        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
//
//        spinner.setAdapter(itemsAdapter); // set the adapter to provide layout of rows and content
//        //s.setOnItemSelectedListener(onItemSelectedListener); // set the listener, to perform actions based on item selection

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_help)
                showDialogHelp();

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! recovering from system error",
                    LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        getActivity().invalidateOptionsMenu();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    private synchronized List<IntraUserInformation> getMoreData() {
        List<IntraUserInformation> dataSet = new ArrayList<>();
        try {
            dataSet.addAll(moduleManager.getSuggestionsToContact(MAX, offset));
            offset = dataSet.size();

        } catch (CantGetIntraUsersListException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    @Override
    public void onItemClickListener(IntraUserInformation data, int position) {
        try {
            if (moduleManager.getActiveIntraUserIdentity() != null) {
                if (!moduleManager.getActiveIntraUserIdentity().getPublicKey().isEmpty())
                    appSession.setData(INTRA_USER_SELECTED, data);
                changeActivity(Activities.CCP_SUB_APP_INTRA_USER_COMMUNITY_CONNECTION_OTHER_PROFILE.getCode(), appSession.getAppPublicKey());
            }
        } catch (CantGetActiveLoginIdentityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLongItemClickListener(IntraUserInformation data, int position) {

    }


    private void showDialogHelp() {
        try {
            if (moduleManager.getActiveIntraUserIdentity() != null) {
                if (!moduleManager.getActiveIntraUserIdentity().getPublicKey().isEmpty()) {
                    PresentationIntraUserCommunityDialog presentationIntraUserCommunityDialog = new PresentationIntraUserCommunityDialog(getActivity(),
                            intraUserSubAppSession,
                            null,
                            moduleManager,
                            PresentationIntraUserCommunityDialog.TYPE_PRESENTATION_WITHOUT_IDENTITIES);
                    presentationIntraUserCommunityDialog.show();
                    presentationIntraUserCommunityDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            showCriptoUsersCache();
                        }
                    });
                } else {
                    PresentationIntraUserCommunityDialog presentationIntraUserCommunityDialog = new PresentationIntraUserCommunityDialog(getActivity(),
                            intraUserSubAppSession,
                            null,
                            moduleManager,
                            PresentationIntraUserCommunityDialog.TYPE_PRESENTATION);
                    presentationIntraUserCommunityDialog.show();
                    presentationIntraUserCommunityDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Boolean isBackPressed = (Boolean) intraUserSubAppSession.getData(Constants.PRESENTATION_DIALOG_DISMISS);
                            if (isBackPressed != null) {
                                if (isBackPressed) {
                                    getActivity().finish();
                                }
                            } else
                                showCriptoUsersCache();
                        }
                    });
                }
            } else {
                PresentationIntraUserCommunityDialog presentationIntraUserCommunityDialog = new PresentationIntraUserCommunityDialog(getActivity(),
                        intraUserSubAppSession,
                        null,
                        moduleManager,
                        PresentationIntraUserCommunityDialog.TYPE_PRESENTATION);
                presentationIntraUserCommunityDialog.show();
                presentationIntraUserCommunityDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Boolean isBackPressed = (Boolean) intraUserSubAppSession.getData(Constants.PRESENTATION_DIALOG_DISMISS);
                        if (isBackPressed != null) {
                            if (isBackPressed) {
                                getActivity().onBackPressed();
                            }
                        } else
                            showCriptoUsersCache();
                    }
                });
            }
        } catch (CantGetActiveLoginIdentityException e) {
            e.printStackTrace();
        }
    }

    private void showCriptoUsersCache() {
        if (dataSet.isEmpty()) {
            showEmpty(true, emptyView);
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                    onRefresh();
                }
            });
        } else {
            adapter.changeDataSet(dataSet);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onRefresh();
                }
            }, 1500);
        }
    }

    public void showEmpty(boolean show, View emptyView) {
        Animation anim = AnimationUtils.loadAnimation(getActivity(),
                show ? android.R.anim.fade_in : android.R.anim.fade_out);
        if (show &&
                (emptyView.getVisibility() == View.GONE || emptyView.getVisibility() == View.INVISIBLE)) {
            emptyView.setAnimation(anim);
            emptyView.setVisibility(View.VISIBLE);
            if (adapter != null)
                adapter.changeDataSet(null);
        } else if (!show && emptyView.getVisibility() == View.VISIBLE) {
            emptyView.setAnimation(anim);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void setUpScreen(LayoutInflater layoutInflater) throws CantGetActiveLoginIdentityException {

    }

    @Override
    public boolean onQueryTextSubmit(String name) {
        //swipeRefreshLayout.setRefreshing(true);
        IntraUserSearch intraUserSearch = moduleManager.searchIntraUser();
        intraUserSearch.setNameToSearch(name);

        // This method does not exist
        mSearchView.onActionViewCollapsed();
        //TODO: cuando esté el network service, esto va a descomentarse
//        try {
//            adapter.changeDataSet(intraUserSearch.getResult());
//
//        } catch (CantGetIntraUserSearchResult cantGetIntraUserSearchResult) {
//            cantGetIntraUserSearchResult.printStackTrace();
//        }


        //adapter.setAddButtonVisible(true);
        //adapter.changeDataSet(IntraUserConnectionListItem.getTestDataExample(getResources()));
        //swipeRefreshLayout.setRefreshing(false);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //Toast.makeText(getActivity(), "Probando busqueda completa", Toast.LENGTH_SHORT).show();
        if (s.length() == 0 && isStartList) {
            //((IntraUserConnectionsAdapter)adapter).setAddButtonVisible(false);
            //adapter.changeDataSet(IntraUserConnectionListItem.getTestData(getResources()));
            return true;
        }
        return false;
    }

    @Override
    public boolean onClose() {
        if (!mSearchView.isActivated()) {
            //adapter.changeDataSet(IntraUserConnectionListItem.getTestData(getResources()));
        }

        return true;
    }

    /*
    Sample AsyncTask to fetch the notifications count
    */
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            // example count. This is where you'd
            // query your data store for the actual count.
            return mNotificationsCount;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }


}




