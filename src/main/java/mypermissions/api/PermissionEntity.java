package mypermissions.api;

import java.util.List;
import java.util.Map;

/**
 * Base PermissionEntity
 *
 * @author Joe Goett
 */
public abstract class PermissionEntity {
    /**
     * Permission nodes associated with this PermissionEntity.
     */
    protected List<String> permissions;

    /**
     * Meta-data associated with this PermissionEntity.
     *
     * Useful for things like chat prefixes and suffixes.
     */
    protected Map<String, String> meta;

    public abstract String getName();

    public String[] getPermissions() {
        return (String[]) permissions.toArray();
    }

    public String getStringMeta(String key) {
        return meta.get(key);
    }

    public int getIntMeta(String key) {
        return Integer.parseInt(getStringMeta(key));
    }

    public double getDoubleMeta(String key) {
        return Double.parseDouble(getStringMeta(key));
    }

    public float getFloatMeta(String key) {
        return Float.parseFloat(getStringMeta(key));
    }

    public boolean getBooleanMeta(String key) {
        return Boolean.parseBoolean(getStringMeta(key));
    }
}
