package mypermissions.api;

import java.util.Map;

/**
 * @author Joe Goett
 */
public class PermissionGroup extends PermissionEntity {
    protected String name;
    protected Map<String, PermissionWorld> worlds;

    @Override
    public String getName() {
        return name;
    }
}
