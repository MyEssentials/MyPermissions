package mypermissions.manager;

import mypermissions.MyPermissions;
import mypermissions.api.IPermissionManager;
import mypermissions.api.command.CommandManager;
import mypermissions.command.CommandTree;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.UUID;

public class ServerToolsPermissionManager implements IPermissionManager {

    private Method serverToolsManagerMethod;

    public ServerToolsPermissionManager() {
        try {
            Class<?> serverToolsManagerClass = Class.forName("info.servertools.permission.PermissionManager");
            serverToolsManagerMethod = serverToolsManagerClass.getMethod("checkPerm", String.class, UUID.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find ServerTools-PERMISSION class!", e);
        }
    }

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        try {
            return (Boolean) serverToolsManagerMethod.invoke(null, trimPermission(permission), uuid);
        } catch (Exception ex) {
            MyPermissions.instance.LOG.error("Error ocurred when trying to check permission!");
            MyPermissions.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    /**
     * Another mod in which wildcard does not add permission for the base permission node... why?
     */
    public String trimPermission(String permission) {
        CommandTree tree = CommandManager.getTreeFromPermission(permission);
        if(tree != null) {
            return tree.getRoot().getAnnotation().permission();
        } else {
            return permission;
        }
    }

}
