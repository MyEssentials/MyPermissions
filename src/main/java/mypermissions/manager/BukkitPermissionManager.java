package mypermissions.manager;

import mypermissions.api.IPermissionManager;
import mypermissions.api.command.CommandManager;
import mypermissions.command.CommandTree;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;

import java.util.UUID;

public class BukkitPermissionManager implements IPermissionManager {

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        OfflinePlayer player = Bukkit.getPlayer(uuid);
        permission = trimPermission(permission);
        return player.getPlayer().hasPermission(permission);
    }

    public String trimPermission(String permission) {
        CommandTree tree = CommandManager.getTreeFromPermission(permission);
        if(tree != null) {
            return tree.getRoot().getAnnotation().permission();
        } else {
            return permission;
        }
    }
}
