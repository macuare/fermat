/*
package unit.com.bitdubai.fermat_dmp_plugin.layer.module.intra_user.developer.bitdubai.version_1.IntraUserModulePluginRoot;

import com.bitdubai.fermat_api.layer.all_definition.IntraUsers.IntraUserSettings;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.interfaces.IntraUserIdentity;
import com.bitdubai.fermat_api.layer.dmp_identity.intra_user.interfaces.IntraUserIdentityManager;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.exceptions.CantShowLoginIdentitiesException;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.interfaces.IntraUserLoginIdentity;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_dmp_plugin.layer.module.intra_user.developer.bitdubai.version_1.IntraWalletUserModulePluginRoot;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;

import junit.framework.TestCase;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

*/
/**
 * Created by natalia on 20/08/15.
 *//*


@RunWith(MockitoJUnitRunner.class)
public class ShowAvailableLoginIdentitiesTest extends TestCase {
    */
/**
     * DealsWithErrors interface Mocked
     *//*

    @Mock
    private ErrorManager mockErrorManager;

    */
/**
     * UsesFileSystem Interface member variables.
     *//*

    @Mock
    private PluginFileSystem mockPluginFileSystem;


    */
/**
     * DealWithIntraUserIdentityManager Interface member variables.
     *//*

    @Mock
    private IntraUserIdentityManager mockIntraUserIdentityManager;

    @Mock
    private PluginTextFile mockIntraUserLoginXml;
    @Mock
    IntraUserIdentity mockIntraUserIdentity;

    private IntraUserSettings intraUserSettings;

    private IntraWalletUserModulePluginRoot testIntraUserModulePluginRoot;





    private UUID pluginId;

    private List<IntraUserLoginIdentity> intraUserLoginIdentityList;


    @Before
    public void setUp() throws Exception{



        MockitoAnnotations.initMocks(this);

        pluginId= UUID.randomUUID();

        testIntraUserModulePluginRoot = new IntraWalletUserModulePluginRoot();
        testIntraUserModulePluginRoot.setPluginFileSystem(mockPluginFileSystem);
        testIntraUserModulePluginRoot.setErrorManager(mockErrorManager);
        testIntraUserModulePluginRoot.setIntraUserManager(mockIntraUserIdentityManager);

        setUpMockitoRules();
        testIntraUserModulePluginRoot.setId(pluginId);

        testIntraUserModulePluginRoot.start();

    }

    public void setUpMockitoRules()  throws Exception{
        intraUserSettings = new IntraUserSettings();
        intraUserSettings.setLoggedInPublicKey(UUID.randomUUID().toString());

        when(mockPluginFileSystem.getTextFile(pluginId, pluginId.toString(), "intraUsersLogin", FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT)).thenReturn(mockIntraUserLoginXml);
        when(mockIntraUserLoginXml.getContent()).thenReturn(XMLParser.parseObject(intraUserSettings));
    }


    @Test
    public void showAvailableLoginIdentitiesTest_GetOk_throwsCantShowLoginIdentitiesException() throws Exception{


        intraUserLoginIdentityList = testIntraUserModulePluginRoot.showAvailableLoginIdentities();

        Assertions.assertThat(intraUserLoginIdentityList)
                .isNotNull();

    }


    @Test
    public void showAvailableLoginIdentitiesTest_GetError_throwsCantShowLoginIdentitiesException() throws Exception{

        testIntraUserModulePluginRoot.setIntraUserManager(null);

        catchException(testIntraUserModulePluginRoot).showAvailableLoginIdentities();

        assertThat(caughtException())
                .isNotNull()
                .isInstanceOf(CantShowLoginIdentitiesException.class);


    }
}

*/
