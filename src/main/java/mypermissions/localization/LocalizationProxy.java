package mypermissions.localization;

import myessentials.Localization;

import java.io.InputStreamReader;

public class LocalizationProxy {
    private static Localization localization;

    public LocalizationProxy() {
    }

    public static Localization getLocalization() {
        if(localization == null) {
            localization = new Localization(new InputStreamReader(LocalizationProxy.class.getResourceAsStream("/localization/en_US.lang")));
            localization.load();
        }
        return localization;
    }
}
