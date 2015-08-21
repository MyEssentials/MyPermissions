package mypermissions.api.entities;

import mypermissions.api.container.GroupsContainer;
import mypermissions.api.container.MetaContainer;
import mypermissions.api.container.PermissionsContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of permissions that is assigned to players.
 * Each player can only have one group assigned to.
 * Groups have a hierarchy.
 */
public class Group {

    private String name;
    private Type type;

    public final PermissionsContainer permsContainer = new PermissionsContainer();
    public final MetaContainer metaContainer = new MetaContainer();
    public final GroupsContainer parents = new GroupsContainer();

    public Group(String name, List<String> permissions, List<Group> parents, Type type) {
        this.name = name;
        if(permissions != null) {
            this.permsContainer.addAll(permissions);
        }
        if(parents != null) {
            this.parents.addAll(parents);
        }
        if(type == null) {
            this.type = Type.NORMAL;
        } else {
            this.type = type;
        }
    }

    public PermissionLevel hasPermission(String permission) {
        PermissionLevel permLevel = permsContainer.hasPermission(permission);

        if(permLevel == PermissionLevel.DENIED || permLevel == PermissionLevel.ALLOWED) {
            return permLevel;
        }

        // If nothing was found search the inherited permissions

        for(Group parent : parents) {
            permLevel = parent.hasPermission(permission);
            if(permLevel == PermissionLevel.DENIED || permLevel == PermissionLevel.ALLOWED) {
                return permLevel;
            }
        }

        return permLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        DEFAULT,
        NORMAL
    }
}
