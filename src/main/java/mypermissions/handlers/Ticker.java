package mypermissions.handlers;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import mypermissions.MyPermissions;
import mypermissions.api.command.CommandManager;
import mypermissions.api.entities.User;
import mypermissions.manager.MyPermissionsManager;
import mypermissions.proxies.PermissionProxy;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.CommandEvent;

import java.util.UUID;

public class Ticker {

    public static final Ticker instance = new Ticker();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent ev) {
        if(PermissionProxy.getPermissionManager() instanceof MyPermissionsManager) {
            MyPermissionsManager manager = (MyPermissionsManager) PermissionProxy.getPermissionManager();

            UUID uuid = ev.player.getGameProfile().getId();
            if(manager.users.get(uuid) == null) {
                manager.users.add(uuid);
                manager.users.updateLastPlayerName(uuid);
                manager.saveUsers();
            } else if(manager.users.updateLastPlayerName(uuid)) {
                manager.saveUsers();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCommandExecuted(CommandEvent ev) {
        if(PermissionProxy.getPermissionManager() instanceof MyPermissionsManager && ev.sender instanceof EntityPlayer) {
            MyPermissionsManager manager = (MyPermissionsManager) PermissionProxy.getPermissionManager();
            User user = manager.users.get(((EntityPlayer) ev.sender).getGameProfile().getId());

            String permission = CommandManager.getPermForCommand(ev.command.getCommandName());
            if(permission == null) {
                permission = "cmd." + ev.command.getCommandName();
            }
            if(!user.hasPermission(permission)) {
                ev.setCanceled(true);
                ev.exception = new CommandException("commands.generic.permission");
            }
        }
    }
}
