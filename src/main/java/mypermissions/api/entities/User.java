package mypermissions.api.entities;

import mypermissions.api.container.MetaContainer;
import mypermissions.api.container.PermissionsContainer;

import java.util.UUID;

/**
 * A wrapper around the EntityPlayer with additional objects for permissions
 */
public class User {

    private Group group;

    public final UUID uuid;
    public final PermissionsContainer permsContainer = new PermissionsContainer();
    public final MetaContainer metaContainer = new MetaContainer();

    public User(UUID uuid, Group group) {
        this.uuid = uuid;
        this.group = group;
    }

    public boolean hasPermission(String permission) {
        PermissionLevel permLevel = permsContainer.hasPermission(permission);

        if(permLevel == PermissionLevel.ALLOWED) {
            return true;
        } else if(permLevel == PermissionLevel.DENIED){
            return false;
        }

        permLevel = group.hasPermission(permission);
        return permLevel == PermissionLevel.ALLOWED;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
