package mypermissions;

import myessentials.localization.api.Local;
import myessentials.localization.api.LocalManager;
import mypermissions.command.api.CommandManager;
import mypermissions.command.core.Commands;
import mypermissions.core.config.Config;
import mypermissions.core.handlers.Ticker;
import mypermissions.permission.core.bridge.MyPermissionsBridge;
import mypermissions.permission.api.proxy.PermissionProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES, acceptableRemoteVersions = "*")
public class MyPermissions {
    public Logger LOG;
    public Local LOCAL;

    @Mod.Instance(Constants.MODID)
    public static MyPermissions instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        LOG = ev.getModLog();
        Constants.CONFIG_FOLDER = ev.getModConfigurationDirectory().getPath() + "/MyPermissions/";

        Config.instance.init(Constants.CONFIG_FOLDER + "/MyPermissions.cfg", "MyPermissions");
        LOCAL = new Local(Constants.CONFIG_FOLDER, Config.instance.localization.get(), "/mypermissions/localization/", MyPermissions.class);
        LocalManager.register(LOCAL, "mypermissions");

        FMLCommonHandler.instance().bus().register(Ticker.instance);
        MinecraftForge.EVENT_BUS.register(Ticker.instance);
    }

    public void loadConfig() {
        Config.instance.reload();
        PermissionProxy.init();
        LOCAL.load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent ev) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent ev) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent ev) {
        loadConfig();
        CommandManager.registerCommands(Commands.class, null, MyPermissions.instance.LOCAL, null);
        if(PermissionProxy.getPermissionManager() instanceof MyPermissionsBridge) {
            CommandManager.registerCommands(Commands.MyPermissionManagerCommands.class, "mypermissions.cmd", MyPermissions.instance.LOCAL, null);
        }
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent ev) {
    }
}