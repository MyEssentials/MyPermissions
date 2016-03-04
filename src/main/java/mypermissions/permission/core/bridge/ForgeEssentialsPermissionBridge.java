package mypermissions.permission.core.bridge;

import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.permission.PermissionManager;

import java.util.UUID;

public class ForgeEssentialsPermissionBridge implements IPermissionBridge {

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        EntityPlayer player = PlayerUtils.getPlayerFromUUID(uuid);
        return PermissionManager.checkPermission(player, permission);
    }
}
