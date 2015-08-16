package mypermissions.api.container;


import java.util.ArrayList;

public class PermissionsContainer extends ArrayList<String> {

    public boolean hasSuperPermission(String permission) {
        if (contains(permission))
            return true;

        for (String p : this) {
            if (permission.startsWith(p)) {
                return true;
            }
        }
        return false;
    }



}
