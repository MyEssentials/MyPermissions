package mypermissions.commands;

import mypermissions.command.CommandResponse;
import mypermissions.command.annotation.Command;
import myessentials.utils.ChatUtils;
import mypermissions.MyPermissions;
import mypermissions.localization.PermissionProxy;
import mypermissions.manager.MyPermissionsManager;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class Commands {

    @Command(
            name = "myperm",
            permission = "mypermissions.cmd",
            syntax = "/perm <command>",
            alias = {"p", "perm"})
    public static CommandResponse permCommand(ICommandSender sender, List<String> args) {
        return CommandResponse.SEND_HELP_MESSAGE;
    }

    @Command(
            name = "config",
            permission = "mypermissions.cmd.config",
            parentName = "mypermissions.cmd",
            syntax = "/perm config <command>")
    public static CommandResponse configCommand(ICommandSender sender, List<String> args) {
        return CommandResponse.SEND_HELP_MESSAGE;
    }

    @Command(
            name = "reload",
            permission = "mypermissions.cmd.config.reload",
            parentName = "mypermissions.cmd.config",
            syntax = "/perm config reload")
    public static CommandResponse configReloadCommand(ICommandSender sender, List<String> args) {
        MyPermissions.instance.loadConfig();
        ChatUtils.sendChat(sender, "Successfully reloaded mod configs!");
        if(PermissionProxy.getPermissionManager() instanceof MyPermissions) {
            ((MyPermissionsManager) PermissionProxy.getPermissionManager()).loadConfigs();
            ChatUtils.sendChat(sender, "Successfully reloaded permission configs!");
        } else {
            ChatUtils.sendChat(sender, "Currently using third party permission system.");
        }
        return CommandResponse.DONE;
    }
}
