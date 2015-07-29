package mypermissions.commands;

import myessentials.command.CommandResponse;
import myessentials.command.annotation.Command;
import mypermissions.MyPermissions;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class MyPermissionsCommands {

    @Command(
            name = "config",
            permission = "mypermissions.cmd.config")
    public static CommandResponse configCommand(ICommandSender sender, List<String> args) {
        return CommandResponse.SEND_HELP_MESSAGE;
    }

    @Command(
            name = "reload",
            permission = "mypermissions.cmd.config.reload",
            parentName = "mypermissions.cmd.config")
    public static CommandResponse configReloadCommand(ICommandSender sender, List<String> args) {
        MyPermissions.instance.groupConfig.init();
        sender.addChatMessage(new ChatComponentText("Reloaded config!"));
        return CommandResponse.DONE;
    }

}
