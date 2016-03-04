package mypermissions.permission.core.container;


import mypermissions.permission.core.entities.PermissionLevel;

import java.util.ArrayList;
import java.util.Iterator;

public class PermissionsContainer extends ArrayList<String> {

    public PermissionLevel hasPermission(String permission) {
        PermissionLevel permLevel = PermissionLevel.NONE;
        if (contains(permission)) {
            permLevel = PermissionLevel.ALLOWED;
        }

        for (String p : this) {
            if (p.endsWith("*")) {
                if (permission.startsWith(p.substring(0, p.length() - 1))) {
                    permLevel = PermissionLevel.ALLOWED;
                } else if(p.startsWith("-") && permission.startsWith(p.substring(1, p.length() - 1))) {
                    permLevel = PermissionLevel.DENIED;
                }
            } else {
                if (permission.equals(p)) {
                    permLevel = PermissionLevel.ALLOWED;
                } else if(p.startsWith("-") && permission.equals(p.substring(1))) {
                    permLevel = PermissionLevel.DENIED;
                }
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
