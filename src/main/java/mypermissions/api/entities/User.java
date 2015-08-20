package mypermissions.api.entities;

import mypermissions.api.container.MetaContainer;
import mypermissions.api.container.PermissionsContainer;

import java.util.UUID;

/**
 * A wrapper around the EntityPlayer with additional objects for permissions
 */
public class User {

    public final UUID uuid;
    public final Group group;
    public final PermissionsContainer permsContainer = new PermissionsContainer();
    public final MetaContainer metaContainer = new MetaContainer();

    public User(UUID uuid, Group group) {
        this.uuid = uuid;
        this.group = group;
    }

    public boolean hasPermission(String permission) {
        return permsContainer.hasSuperPermission(permission) || group.permsContainer.hasSuperPermission(permission);
    }

}
