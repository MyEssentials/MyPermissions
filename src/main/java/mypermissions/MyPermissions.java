package mypermissions;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import mypermissions.api.command.CommandManager;
import myessentials.config.ConfigProcessor;
import mypermissions.command.Commands;
import mypermissions.config.Config;
import mypermissions.localization.LocalizationProxy;
import mypermissions.localization.PermissionProxy;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES, acceptableRemoteVersions = "*")
public class MyPermissions {
    public Configuration config;
    public Logger LOG;

    @Mod.Instance(Constants.MODID)
    public static MyPermissions instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        LOG = ev.getModLog();
        Constants.CONFIG_FOLDER = ev.getModConfigurationDirectory().getPath() + "/MyPermissions/";
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
        CommandManager.registerCommands(Commands.class, null, LocalizationProxy.getLocalization(), null);
        CommandManager.registerCommands(Commands.MyPermissionManagerCommands.class, "mypermissions.cmd", LocalizationProxy.getLocalization(), null);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent ev) {
    }
}