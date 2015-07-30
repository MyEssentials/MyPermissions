package mypermissions.localization;

import myessentials.Localization;

import java.io.InputStreamReader;

public class LocalizationProxy {

    private static Localization local;

    public static Localization getLocalization() {
        if(local == null) {
            local = new Localization(new InputStreamReader(LocalizationProxy.class.getResourceAsStream("/localization/en_US.lang")));
        }
        return local;
    }

}
