package mypermissions;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class MyPermissions {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent ev) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent ev) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent ev) {
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent ev) {
    }
}