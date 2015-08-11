package mypermissions.api.entities;

/**
 * Variables inside permission strings.
 */
public class Meta {

    public final String permission;
    public final int metadata;

    public Meta(String permission, int metadata) {
        this.permission = permission;
        this.metadata = metadata;
    }
}
