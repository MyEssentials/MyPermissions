package mypermissions.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of permissions that is assigned to players.
 * Each player can only have one group assigned to.
 * Groups have a hierarchy.
 */
public class Group {

    private Group parent;
    private List<String> permissions = new ArrayList<String>();
    private String name;

    public Group(String name, List<String> permissions, Group parent) {
        this.name = name;
        this.permissions = permissions;
        this.parent = parent;
    }

    public Group(String name, List<String> permissions) {
        this(name, permissions, null);
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        List<String> result = new ArrayList<String>();

        result.addAll(permissions);

        if(parent != null) {
            result.addAll(getPermissions());
        }

        return result;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
