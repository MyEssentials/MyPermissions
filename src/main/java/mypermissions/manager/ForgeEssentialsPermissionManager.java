package mypermissions.manager;

import myessentials.utils.PlayerUtils;
import mypermissions.api.IPermissionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.permission.PermissionManager;

import java.util.UUID;

public class ForgeEssentialsPermissionManager implements IPermissionManager {

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        EntityPlayer player = PlayerUtils.getPlayerFromUUID(uuid);
        return PermissionManager.checkPermission(player, permission);
    }
}
