package mypermissions.handlers;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import mypermissions.manager.MyPermissionsManager;
import mypermissions.proxies.PermissionProxy;

public class Ticker {

    public static final Ticker instance = new Ticker();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent ev) {
        if(PermissionProxy.getPermissionManager() instanceof MyPermissionsManager) {
            MyPermissionsManager manager = (MyPermissionsManager) PermissionProxy.getPermissionManager();

            if(manager.users.get(ev.player.getGameProfile().getId()) == null) {
                manager.users.add(ev.player.getGameProfile().getId());
                manager.saveUsers();
            }
        }
    }
}
