package com.bitdubai.fermat_dmp_plugin.layer.module.intra_user.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.exceptions.CantGetUserIntraUserIdentitiesException;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.interfaces.IntraUserIdentity;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.interfaces.IntraUserIdentityManager;
import com.bitdubai.fermat_api.layer.dmp_module.intra_user.exceptions.CantGetIntraUserSearchResult;
import com.bitdubai.fermat_api.layer.dmp_module.intra_user.interfaces.IntraUserInformation;
import com.bitdubai.fermat_api.layer.dmp_module.intra_user.interfaces.IntraUserSearch;
import com.bitdubai.fermat_api.layer.dmp_network_service.intra_user.exceptions.ErrorInIntraUserSearchException;
import com.bitdubai.fermat_api.layer.dmp_network_service.intra_user.interfaces.IntraUser;
import com.bitdubai.fermat_api.layer.dmp_network_service.intra_user.interfaces.IntraUserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * The class <code>com.bitdubai.fermat_dmp_plugin.layer.module.intra_user.developer.bitdubai.version_1.structure.IntraUserModuleSearch</code>
 * is the implementation of IntraUserSearch interface.
 * And provides the methods to search for a particular intra user
 *
 * Created by natalia on 11/08/15.
 */
public class IntraUserModuleSearch implements IntraUserSearch {

    /**
     * DealsWithIntraUsersNetworkService interface member variable
     */

    IntraUserManager intraUserNSManager;

    /**
     * DealsWithIdentityIntraUser interface member variable
     */

    IntraUserIdentityManager intraUserIdentityManager;

    private String nameToSearch;

    /**
     * Constructor
     */

   public IntraUserModuleSearch(IntraUserManager intraUserNSManager, IntraUserIdentityManager intraUserIdentityManager){
       this.intraUserNSManager = intraUserNSManager;
       this.intraUserIdentityManager = intraUserIdentityManager;

   }
    /**
     * That metod search the intra user name to search
     * @param nameToSearch Intra User name
     */
    @Override
    public void setNameToSearch(String nameToSearch) {
            this.nameToSearch = nameToSearch;
    }

    /**
     * That metod return a list of intra user that match the search condition
     * @return list of IntraUserInformation>
     */
    @Override
    public List<IntraUserInformation> getResult() throws CantGetIntraUserSearchResult {

        try
        {
            List<IntraUserInformation>  intraUserInformationList = new ArrayList<IntraUserInformation>();
            /**
             * search intra users by name from intra user network service
             */
            List<IntraUser> intraUserList = this.intraUserNSManager.searchIntraUserByName(this.nameToSearch);

            /**
             * search Device User intra users  from intra user identity
             */
            List<IntraUserIdentity>  intraUserIdentityList = this.intraUserIdentityManager.getAllIntraUsersFromCurrentDeviceUser();

            /**
             * I only add intra users belonging to the Device User log
             */
            for (IntraUser intraUser : intraUserList) {

                for (IntraUserIdentity intraUserIdentity : intraUserIdentityList) {
                   if(intraUserIdentity.getPublicKey().equals(intraUser.getPublicKey()) )
                       intraUserInformationList.add(new IntraUserModuleInformation(intraUserIdentity.getAlias(),intraUserIdentity.getPublicKey(),intraUserIdentity.getProfileImage()));

                }
             }

            return intraUserInformationList;
        }
        catch(ErrorInIntraUserSearchException e)
        {
            throw new CantGetIntraUserSearchResult("CAN'T GET INTRA USERS SEARCH RESULT",e,"","");
        }
        catch(CantGetUserIntraUserIdentitiesException e)
        {
            throw new CantGetIntraUserSearchResult("CAN'T GET INTRA USERS SEARCH RESULT",e,"","");
        }
        catch(Exception e)
        {
            throw new CantGetIntraUserSearchResult("CAN'T GET INTRA USERS SEARCH RESULT", FermatException.wrapException(e),"","unknown exception");
        }

    }
}
