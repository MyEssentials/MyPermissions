package mypermissions.manager;

import mypermissions.api.IPermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;

import java.util.UUID;

public class BukkitPermissionManager implements IPermissionManager {

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        OfflinePlayer player = Bukkit.getPlayer(uuid);
        return player.getPlayer().hasPermission(permission);
    }
}
