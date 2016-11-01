package mypermissions.core.config;

import myessentials.config.api.ConfigProperty;
import myessentials.config.api.ConfigTemplate;

public class Config extends ConfigTemplate {

    public static final Config instance = new Config();

    public ConfigProperty<String> permissionSystem = new ConfigProperty<String>(
            "permissionSystem", "general",
            "The permission system it should be used as default. $ForgeEssentials for FE permission system, $Bukkit for Bukkit permission system, $ServerTools for ServerTools-PERMISION's permisison system and $MyPermissions for our own permission system.",
            "$MyPermissions");

    public ConfigProperty<String> localization = new ConfigProperty<String>(
            "localization", "general",
            "The localization used",
            "en_US");

    public ConfigProperty<Boolean> fullAccessForOPS = new ConfigProperty<Boolean>(
            "fullAccessForOPS", "permissions",
            "Players that are opped will have access to any command.",
            true);

    public ConfigProperty<String> defaultGroup = new ConfigProperty<String>(
            "defaultGroup", "groups",
            "The default group assigned to any player that does not have one assigned yet.",
            "default");
}
