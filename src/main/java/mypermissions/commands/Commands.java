package mypermissions.commands;

import myessentials.command.CommandResponse;
import myessentials.command.annotation.Command;
import myessentials.utils.ChatUtils;
import mypermissions.MyPermissions;
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
        MyPermissions.instance.groupConfig.init();
        ChatUtils.sendChat(sender, "Reloaded config!");
        return CommandResponse.DONE;
    }

}
