package mypermissions.localization;

import mypermissions.api.IPermissionManager;
import mypermissions.manager.PermissionManager;

public class PermissionsProxy {

    private static IPermissionManager permissionManager;

    public static IPermissionManager getPermissionManager() {
        if(permissionManager == null) {
            permissionManager = new PermissionManager();
        }
        return permissionManager;
    }
}
