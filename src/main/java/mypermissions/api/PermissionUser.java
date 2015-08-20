package mypermissions.api;

import net.minecraft.server.MinecraftServer;

import java.util.Map;
import java.util.UUID;

/**
 * @author Joe Goett
 */
public class PermissionUser extends PermissionEntity {
    protected UUID uuid;
    protected Map<String, PermissionGroup> groups;
    protected Map<String, PermissionWorld> worlds;

    @Override
    public String getName() {
        return MinecraftServer.getServer().func_152358_ax().func_152652_a(uuid).getName();
    }
}
