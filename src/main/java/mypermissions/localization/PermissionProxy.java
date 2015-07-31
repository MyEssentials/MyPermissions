package mypermissions.localization;

import cpw.mods.fml.common.Loader;
import myessentials.utils.ClassUtils;
import mypermissions.api.IPermissionManager;
import mypermissions.manager.BukkitPermissionManager;
import mypermissions.manager.ForgeEssentialsPermissionManager;
import mypermissions.manager.MyPermissionsManager;

public class PermissionProxy {

    private static IPermissionManager permissionManager;

    public static IPermissionManager getPermissionManager() {
        if(permissionManager == null) {
            if(ClassUtils.isBukkitLoaded()) {
                permissionManager = new BukkitPermissionManager();
            } else if(Loader.isModLoaded("ForgeEssentials")) {
                permissionManager = new ForgeEssentialsPermissionManager();
            } else {
                permissionManager = new MyPermissionsManager();
            }
        }
        return permissionManager;
    }
}
