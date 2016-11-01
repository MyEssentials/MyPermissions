package mypermissions.core.handlers;

import mypermissions.command.api.CommandManager;
import mypermissions.permission.core.entities.User;
import mypermissions.permission.core.bridge.MyPermissionsBridge;
import mypermissions.permission.api.proxy.PermissionProxy;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.UUID;

public class Ticker {

    public static final Ticker instance = new Ticker();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent ev) {
        if(PermissionProxy.getPermissionManager() instanceof MyPermissionsBridge) {
            MyPermissionsBridge manager = (MyPermissionsBridge) PermissionProxy.getPermissionManager();

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
        if(PermissionProxy.getPermissionManager() instanceof MyPermissionsBridge && ev.getSender() instanceof EntityPlayer) {
            MyPermissionsBridge manager = (MyPermissionsBridge) PermissionProxy.getPermissionManager();
            User user = manager.users.get(((EntityPlayer) ev.getSender()).getGameProfile().getId());

            String permission = CommandManager.getPermForCommand(ev.getCommand().getCommandName());
            if(permission == null) {
                permission = "cmd." + ev.getCommand().getCommandName();
            }
            if(!user.hasPermission(permission)) {
                ev.setCanceled(true);
                ev.setException(new CommandException("commands.generic.permission"));
            }
        }
    }
}
