package mypermissions.api;

import java.util.UUID;

public interface IPermissionManager {

    boolean hasPermission(UUID uuid, String permission);

}
