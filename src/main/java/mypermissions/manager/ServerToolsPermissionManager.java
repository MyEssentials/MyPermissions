package mypermissions.manager;

import com.esotericsoftware.reflectasm.MethodAccess;
import mypermissions.api.IPermissionManager;

import java.util.UUID;

public class ServerToolsPermissionManager implements IPermissionManager {

    private MethodAccess serverToolsManagerAccess;
    private int checkPermissionMethodID;

    public ServerToolsPermissionManager() {
        try {
            Class serverToolsManagerClass = Class.forName("info.servertools.permission.PermissionManager");
            serverToolsManagerAccess = MethodAccess.get(serverToolsManagerClass);
            checkPermissionMethodID = serverToolsManagerAccess.getIndex("checkPerm", String.class, UUID.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find ServerTools-PERMISSION class!", e);
        }
    }

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        return (Boolean) serverToolsManagerAccess.invoke(null, checkPermissionMethodID, permission, uuid);
    }
}
