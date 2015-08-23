package mypermissions.manager;

import mypermissions.MyPermissions;
import mypermissions.api.IPermissionManager;
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
            return (Boolean) serverToolsManagerMethod.invoke(null, permission, uuid);
        } catch (Exception ex) {
            MyPermissions.instance.LOG.error("Error ocurred when trying to check permission!");
            MyPermissions.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }
}
