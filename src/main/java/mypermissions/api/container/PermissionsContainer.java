package mypermissions.api.container;

import myessentials.entities.Container;

public class PermissionsContainer extends Container<String> {

    public boolean hasSuperPermission(String permission) {
        if (contains(permission))
            return true;

        for (String p : items) {
            if (permission.startsWith(p)) {
                return true;
            }
        }
        return false;
    }



}
