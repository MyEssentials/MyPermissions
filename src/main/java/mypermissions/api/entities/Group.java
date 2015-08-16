package mypermissions.api.entities;

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

    public final String name;
    public final Type type;
    public final PermissionsContainer permsContainer = new PermissionsContainer();
    public final MetaContainer metaContainer = new MetaContainer();
    public final List<Group> parents = new ArrayList<Group>();

    public Group(String name, List<String> permissions, List<Group> parents, Type type) {
        this.name = name;
        this.permsContainer.addAll(permissions);
        if(parents != null) {
            this.parents.addAll(parents);
        }
        if(type == null) {
            this.type = Type.NORMAL;
        } else {
            this.type = type;
        }
    }

    public enum Type {
        DEFAULT,
        NORMAL
    }
}
