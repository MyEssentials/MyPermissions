package mypermissions.proxies;

import myessentials.Localization;
import mypermissions.Constants;
import mypermissions.MyPermissions;
import mypermissions.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LocalizationProxy {
    private static Localization localization;

    public LocalizationProxy() {
    }

    public static Localization getLocalization() {
        if(localization == null) {
            try {
                InputStream is = null;

                File file = new File(Constants.CONFIG_FOLDER + "/localization/" + Config.localization + ".lang");
                if (file.exists()) {
                    is = new FileInputStream(file);
                }
                if (is == null) {
                    is = LocalizationProxy.class.getResourceAsStream("/mypermissions/localization/" + Config.localization + ".lang");
                }
                if (is == null) {
                    is = LocalizationProxy.class.getResourceAsStream("/mypermissions/localization/en_US.lang");
                    MyPermissions.instance.LOG.warn("Reverting to en_US.lang because {} does not exist!", Config.localization + ".lang");
                }

                localization = new Localization(new InputStreamReader(is));
                localization.load();
            } catch (Exception ex) {
                MyPermissions.instance.LOG.warn("Failed to load localization!", ex);
            }
        }
        return localization;
    }
}
