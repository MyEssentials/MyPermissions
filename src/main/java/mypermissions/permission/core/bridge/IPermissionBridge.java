package mypermissions.permission.core.bridge;

import java.util.UUID;

public interface IPermissionBridge {

    boolean hasPermission(UUID uuid, String permission);

}
