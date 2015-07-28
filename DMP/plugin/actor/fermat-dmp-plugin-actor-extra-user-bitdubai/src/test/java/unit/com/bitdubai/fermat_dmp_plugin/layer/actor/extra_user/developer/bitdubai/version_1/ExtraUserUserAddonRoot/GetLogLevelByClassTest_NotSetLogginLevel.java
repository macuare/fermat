package unit.com.bitdubai.fermat_dmp_plugin.layer.actor.extra_user.developer.bitdubai.version_1.ExtraUserUserAddonRoot;

import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_dmp_plugin.layer.actor.extra_user.developer.bitdubai.version_1.ExtraUserUserAddonRoot;

import org.fest.assertions.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Manuel Perez on 26/07/15.
 */
public class GetLogLevelByClassTest_NotSetLogginLevel {

    ExtraUserUserAddonRoot extraUserUserAddonRoot;

    @Test
    public void getLogLevelByClassTest_notSetLogginLevel_returnDEFAULT_LOG_LEVEL() throws Exception{

        LogLevel resultLogLevel=extraUserUserAddonRoot.getLogLevelByClass("com.bitdubai.fermat_dmp_plugin.layer.actor.extra_user.developer.bitdubai.version_1.structure.ExtraUser");
        Assertions.assertThat(resultLogLevel)
                .isNotNull()
                .isEqualTo(LogLevel.MINIMAL_LOGGING);

    }

    //@Ignore
    @Test
    public void getLogLevelByClassTest_NotSetLogginLevelNullArgument_returnDEFAULT_LOG_LEVEL() throws Exception{

        extraUserUserAddonRoot=new ExtraUserUserAddonRoot();
        LogLevel resultLogLevel=extraUserUserAddonRoot.getLogLevelByClass(null);
        Assertions.assertThat(resultLogLevel)
                .isNotNull()
                .isEqualTo(LogLevel.MINIMAL_LOGGING);

    }

}
