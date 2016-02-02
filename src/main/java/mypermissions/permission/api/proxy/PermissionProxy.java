package mypermissions.permission.api.proxy;

import cpw.mods.fml.common.Loader;
import myessentials.utils.ClassUtils;
import mypermissions.MyPermissions;
import mypermissions.permission.core.bridge.IPermissionBridge;
import mypermissions.core.config.Config;
import mypermissions.permission.core.exception.PermissionException;
import mypermissions.permission.core.bridge.BukkitPermissionBridge;
import mypermissions.permission.core.bridge.ForgeEssentialsPermissionBridge;
import mypermissions.permission.core.bridge.MyPermissionsBridge;
import mypermissions.permission.core.bridge.ServerToolsPermissionBridge;

public class PermissionProxy {
    public static final String PERM_SYSTEM_BUKKIT = "$Bukkit";
    public static final String PERM_SYSTEM_FORGE_ESSENTIALS = "$ForgeEssentials";
    public static final String PERM_SYSTEM_MY_PERMISSIONS = "$MyPermissions";
    public static final String PERM_SYSTEM_SERVER_TOOLS = "$ServerTools";

    private static IPermissionBridge permissionManager;

    public static IPermissionBridge getPermissionManager() {
        if(permissionManager == null) {
            init();
        }
        return permissionManager;
    }

    public static void init() {
        if(Config.instance.permissionSystem.get().equals(PERM_SYSTEM_BUKKIT)) {
            if(!ClassUtils.isBukkitLoaded()) {
                throw new PermissionException("Failed to find Bukkit permission system.");
            }
            permissionManager = new BukkitPermissionBridge();
            MyPermissions.instance.LOG.info("Successfully linked to Bukkit's permission system");
        } else if(Config.instance.permissionSystem.get().equals(PERM_SYSTEM_FORGE_ESSENTIALS)) {
            if (!Loader.isModLoaded("ForgeEssentials")) {
                throw new PermissionException("Failed to find ForgeEssentials permission system.");
            }
            permissionManager = new ForgeEssentialsPermissionBridge();
            MyPermissions.instance.LOG.info("Successfully linked to ForgeEssentials' permission system");
        } else if(Config.instance.permissionSystem.get().equals(PERM_SYSTEM_SERVER_TOOLS)) {
            if(!Loader.isModLoaded("ServerTools-PERMISSION")) {
                throw new PermissionException("Failed to find ServerTools' permission system.");
            }
            permissionManager = new ServerToolsPermissionBridge();
            MyPermissions.instance.LOG.info("Successfully linked to ServerTools' permission system");
        } else {
            permissionManager = new MyPermissionsBridge();
            ((MyPermissionsBridge) permissionManager).loadConfigs();
            MyPermissions.instance.LOG.info("Currently using built-in permission system.");
            MyPermissions.instance.LOG.info("This is not fully functional and only works for mods that use this API.");
            MyPermissions.instance.LOG.info("If you have Bukkit or ForgeEssentials installed please use that instead.");
        }
    }
}
