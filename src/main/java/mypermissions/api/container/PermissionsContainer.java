package mypermissions.api.container;


import mypermissions.api.entities.PermissionLevel;

import java.util.ArrayList;

public class PermissionsContainer extends ArrayList<String> {

    public PermissionLevel hasPermission(String permission) {
        PermissionLevel permLevel = PermissionLevel.NONE;
        if (contains(permission)) {
            permLevel = PermissionLevel.ALLOWED;
        }

        for (String p : this) {
            if (permission.startsWith(p)) {
                permLevel = PermissionLevel.ALLOWED;
            } else if(p.startsWith("-") && permission.startsWith(p.substring(1))) {
                permLevel = PermissionLevel.DENIED;
            }
        }

        return permLevel;
    }

    @Override
    public String toString() {
        String formattedList = "";
        for(String perm : this) {
            if(formattedList.equals("")) {
                formattedList += perm;
            } else {
                formattedList += "\\n" + perm;
            }
        }
        return formattedList;
    }
}
