package mypermissions.api;

import net.minecraft.entity.player.EntityPlayer;

public interface IPermissionManager {



    boolean hasPermission(EntityPlayer player, String permission);

}
