package mypermissions.api.container;


import mypermissions.api.entities.PermissionLevel;

import java.util.ArrayList;
import java.util.Iterator;

public class PermissionsContainer extends ArrayList<String> {

    public PermissionLevel hasPermission(String permission) {
        PermissionLevel permLevel = PermissionLevel.NONE;
        if (contains(permission)) {
            permLevel = PermissionLevel.ALLOWED;
        }

        for (String p : this) {
            if (permission.startsWith(p)) {
                permLevel = PermissionLevel.ALLOWED;
                //MyPermissions.instance.LOG.info("PermLevel is allowed for " + permission);
            } else if(p.startsWith("-") && permission.startsWith(p.substring(1))) {
                permLevel = PermissionLevel.DENIED;
                //MyPermissions.instance.LOG.info("PermLevel is denied for " + permission);
            }
        }

        return permLevel;
    }


    public boolean remove(String perm) {
        for(Iterator<String> it = iterator(); it.hasNext(); ) {
            String p = it.next();
            if(p.equals(perm)) {
                it.remove();
                return true;
            }
        }
        return false;
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
