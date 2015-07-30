package mypermissions;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import myessentials.command.CommandManager;
import myessentials.config.ConfigProcessor;
import mypermissions.commands.Commands;
import mypermissions.config.Config;
import mypermissions.config.json.GroupConfig;
import mypermissions.localization.LocalizationProxy;
import mypermissions.localization.PermissionsProxy;
import mypermissions.manager.PermissionManager;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES, acceptableRemoteVersions = "*")
public class MyPermissions {
    public Configuration config;
    public Logger LOG;

    public GroupConfig groupConfig;

    @Mod.Instance(Constants.MODID)
    public static MyPermissions instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        LOG = ev.getModLog();
        Constants.CONFIG_FOLDER = ev.getModConfigurationDirectory().getPath() + "/MyPermissions/";

        // Read Configs
        config = new Configuration(new File(Constants.CONFIG_FOLDER, "MyTown.cfg"));
        ConfigProcessor.load(config, Config.class);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent ev) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent ev) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent ev) {
        groupConfig = new GroupConfig(Constants.CONFIG_FOLDER + "GroupConfig.json", (PermissionManager) PermissionsProxy.getPermissionManager());
        CommandManager.registerCommands(Commands.class, null, LocalizationProxy.getLocalization());
        groupConfig.init();
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent ev) {
    }
}