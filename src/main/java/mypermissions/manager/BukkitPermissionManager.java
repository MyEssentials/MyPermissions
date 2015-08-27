package mypermissions.manager;

import mypermissions.MyPermissions;
import mypermissions.api.IPermissionManager;
import mypermissions.api.command.CommandManager;
import mypermissions.command.CommandTree;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class BukkitPermissionManager implements IPermissionManager {

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        OfflinePlayer player = Bukkit.getPlayer(uuid);
        if(player == null || player.getPlayer() == null) {
            MyPermissions.instance.LOG.error("Failed to get player with the UUID: " + uuid.toString());
            return false;
        }

        return player.getPlayer().hasPermission(trimPermission(permission));
    }

    /**
     * Bukkit permissions will only check for base permission of each tree since implementing
     * it is not important and should work as intended 99% of the time.
     */
    public String trimPermission(String permission) {
        CommandTree tree = CommandManager.getTreeFromPermission(permission);
        if(tree != null) {
            return tree.getRoot().getAnnotation().permission();
        } else {
            return permission;
        }
    }
}
