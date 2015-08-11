package mypermissions.command;

import mypermissions.api.command.CommandResponse;
import mypermissions.api.command.annotation.Command;
import myessentials.utils.ChatUtils;
import mypermissions.MyPermissions;
import mypermissions.api.entities.Group;
import mypermissions.exception.PermissionCommandException;
import mypermissions.localization.PermissionProxy;
import mypermissions.manager.MyPermissionsManager;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
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

    @Command(
            name = "group",
            permission = "mypermissions.cmd.group",
            parentName = "mypermissions.cmd",
            syntax = "/perm group <command>")
    public static CommandResponse groupCommand(ICommandSender sender, List<String> args) {
        return CommandResponse.SEND_HELP_MESSAGE;
    }

    @Command(
            name = "create",
            permission = "mypermissions.cmd.group.create",
            parentName = "mypermissions.cmd.group",
            syntax = "/perm group create <name> [parents]")
    public static CommandResponse groupCreateCommand(ICommandSender sender, List<String> args) {
        if(!(PermissionProxy.getPermissionManager() instanceof MyPermissionsManager))
            throw new PermissionCommandException("mypermissions.cmd.err.notAvailable");
        if(args.size() < 1)
            return CommandResponse.SEND_SYNTAX;

        Group group = new Group(args.get(0), null, null, null);
        getPermissionManager().groups.add(group);
        getPermissionManager().groupConfig.write(getPermissionManager().groups);

        return CommandResponse.DONE;
    }

    @Command(
            name = "rename",
            permission = "mypermissions.cmd.group.rename",
            parentName = "mypermissions.cmd.group",
            syntax = "/perm group rename <group> <name>")
    public static CommandResponse groupRenameCommand(ICommandSender sender, List<String> args) {
        return CommandResponse.DONE;
    }

    private static MyPermissionsManager getPermissionManager() {
        return (MyPermissionsManager) PermissionProxy.getPermissionManager();
    }
}
