package mypermissions.config;

import myessentials.config.ConfigProperty;

public class Config {

    @ConfigProperty(name = "permissionSystem", category = "general", comment = "The permission system it should be used as default. $ForgeEssentials for FE permission system, $Bukkit for Bukkit permission system, $ServerTools for ServerTools-PERMISION's permisison system and $MyPermissions for our own permission system.")
    public static String permissionSystem = "$MyPermissions";

    @ConfigProperty(name = "localization", category = "general", comment = "The localization file used")
    public static String localization = "en_US";
}
