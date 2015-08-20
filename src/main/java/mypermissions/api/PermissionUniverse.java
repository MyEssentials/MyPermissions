package mypermissions.api;

import java.util.Map;

/**
 * @author Joe Goett
 */
public final class PermissionUniverse {
    public Map<String, PermissionGroup> groups;
    public Map<String, PermissionUser> users;
    public Map<String, PermissionWorld> worlds;
}
