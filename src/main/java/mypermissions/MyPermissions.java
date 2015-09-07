package mypermissions;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import myessentials.Localization;
import mypermissions.api.command.CommandManager;
import myessentials.config.ConfigProcessor;
import mypermissions.command.Commands;
import mypermissions.config.Config;
import mypermissions.handlers.Ticker;
import mypermissions.proxies.PermissionProxy;
import mypermissions.manager.MyPermissionsManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES, acceptableRemoteVersions = "*")
public class MyPermissions {
    public Configuration config;
    public Logger LOG;
    public Localization LOCAL;

    @Mod.Instance(Constants.MODID)
    public static MyPermissions instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        LOG = ev.getModLog();
        Constants.CONFIG_FOLDER = ev.getModConfigurationDirectory().getPath() + "/MyPermissions/";
        LOCAL = new Localization(Constants.CONFIG_FOLDER, Config.localization, "/mypermissions/localization/", MyPermissions.class);
        LOG.info(LOCAL.getLocalization("mypermissions.notification.user.group.set"));
        FMLCommonHandler.instance().bus().register(Ticker.instance);
        MinecraftForge.EVENT_BUS.register(Ticker.instance);
    }

    public void loadConfig() {
        config = new Configuration(new File(Constants.CONFIG_FOLDER, "MyPermissions.cfg"));
        ConfigProcessor.load(config, Config.class);
        PermissionProxy.init();
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
        if(PermissionProxy.getPermissionManager() instanceof MyPermissionsManager) {
            CommandManager.registerCommands(Commands.MyPermissionManagerCommands.class, "mypermissions.cmd", MyPermissions.instance.LOCAL, null);
        }
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent ev) {
    }
}