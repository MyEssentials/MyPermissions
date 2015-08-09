package mypermissions.entities;

import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of permissions that is assigned to players.
 * Each player can only have one group assigned to.
 * Groups have a hierarchy.
 */
public class Group {

    private List<Group> parents = new ArrayList<Group>();
    private List<String> permissions = new ArrayList<String>();
    private String name;

    public Group(String name, List<String> permissions, List<Group> parents) {
        this.name = name;
        this.permissions = permissions;
        this.parents.addAll(parents);
    }

    public Group(String name, List<String> permissions) {
        this(name, permissions, null);
    }

    public String getName() {
        return name;
    }

    public List<Group> getParents() {
        return parents;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getPermissionsAndSuperPermissions() {
        List<String> result = new ArrayList<String>();
        result.addAll(permissions);

        for(Group parent : parents) {
            result.addAll(parent.getPermissionsAndSuperPermissions());
        }

        return result;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
