package mypermissions.command;

import myessentials.utils.PlayerUtils;
import mypermissions.api.command.CommandResponse;
import mypermissions.api.command.annotation.Command;
import myessentials.utils.ChatUtils;
import mypermissions.MyPermissions;
import mypermissions.api.entities.Group;
import mypermissions.api.entities.User;
import mypermissions.exception.PermissionCommandException;
import mypermissions.proxies.LocalizationProxy;
import mypermissions.proxies.PermissionProxy;
import mypermissions.manager.MyPermissionsManager;
import net.minecraft.command.ICommandSender;

import java.util.List;
import java.util.UUID;

public class Commands {

    protected static Group getGroupFromName(String name) {
        Group group = getManager().groups.get(name);
        if(group == null) {
            throw new PermissionCommandException("mypermissions.cmd.err.group.notExist", name);
        }
        return group;
    }

    protected static UUID getUUIDFromUsername(String username) {
        UUID uuid = PlayerUtils.getUUIDFromUsername(username);
        if(uuid == null) {
            throw new PermissionCommandException("mypermissions.cmd.err.player.notExist", username);
        }
        return uuid;
    }

    protected static void sendChat(ICommandSender sender, String localKey, Object... args) {
        ChatUtils.sendChat(sender, LocalizationProxy.getLocalization().getLocalization(localKey, args));
    }

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
    public static class MyPermissionManagerCommands {
        @Command(
                name = "group",
                permission = "mypermissions.cmd.group",
                parentName = "mypermissions.cmd",
                syntax = "/perm group <command>")
        public static CommandResponse groupCommand(ICommandSender sender, List<String> args) {
            return CommandResponse.SEND_HELP_MESSAGE;
        }

        @Command(
                name = "add",
                permission = "mypermissions.cmd.group.add",
                parentName = "mypermissions.cmd.group",
                syntax = "/perm group add <name> [parents]")
        public static CommandResponse groupAddCommand(ICommandSender sender, List<String> args) {
            if (args.size() < 1) {
                return CommandResponse.SEND_SYNTAX;
            }

            Group group = new Group(args.get(0), null, null, null);
            getManager().groups.add(group);
            getManager().saveGroups();
            sendChat(sender, "mypermissions.notification.group.added");
            return CommandResponse.DONE;
        }

        @Command(
                name = "remove",
                permission = "mypermissions.cmd.group.remove",
                parentName = "mypermissions.cmd.group",
                syntax = "/perm group remove <name>")
        public static CommandResponse groupRemoveCommand(ICommandSender sender, List<String> args) {
            if (args.size() < 1) {
                return CommandResponse.SEND_SYNTAX;
            }

            Group group = getGroupFromName(args.get(0));
            getManager().groups.remove(group);
            getManager().saveGroups();
            sendChat(sender, "mypermissions.notification.group.removed");
            return CommandResponse.DONE;
        }

        @Command(
                name = "rename",
                permission = "mypermissions.cmd.group.rename",
                parentName = "mypermissions.cmd.group",
                syntax = "/perm group rename <group> <name>")
        public static CommandResponse groupRenameCommand(ICommandSender sender, List<String> args) {
            if (args.size() < 2) {
                return CommandResponse.SEND_SYNTAX;
            }

            Group group = getGroupFromName(args.get(0));
            group.setName(args.get(1));
            getManager().saveGroups();

            return CommandResponse.DONE;
        }

        @Command(
                name = "list",
                permission = "mypermissions.cmd.group.list",
                parentName = "mypermissions.cmd.group",
                syntax = "/perm group list")
        public static CommandResponse groupListCommand(ICommandSender sender, List<String> args) {
            sendChat(sender, "mypermissions.notification.group.list", getManager().groups.toString());
            return CommandResponse.DONE;
        }

        @Command(
                name = "perm",
                permission = "mypermissions.cmd.group.perm",
                parentName = "mypermissions.cmd.group",
                syntax = "/perm group perm <command>")
        public static CommandResponse groupPermCommand(ICommandSender sender, List<String> args) {
            return CommandResponse.SEND_HELP_MESSAGE;
        }

        @Command(
                name = "add",
                permission = "mypermissions.cmd.group.perm.add",
                parentName = "mypermissions.cmd.group.perm",
                syntax = "/perm group perm add <group> <perm>")
        public static CommandResponse groupPermAddCommand(ICommandSender sender, List<String> args) {
            if(args.size() < 2) {
                return CommandResponse.SEND_SYNTAX;
            }

            Group group = getGroupFromName(args.get(0));
            group.permsContainer.add(args.get(1));
            getManager().saveGroups();
            sendChat(sender, "mypermissions.notification.group.perm.added");

            return CommandResponse.DONE;
        }

        @Command(
                name = "remove",
                permission = "mypermissions.cmd.group.perm.remove",
                parentName = "mypermissions.cmd.group.perm",
                syntax = "/perm group perm remove <group> <perm>")
        public static CommandResponse groupPermRemoveCommand(ICommandSender sender, List<String> args) {
            if(args.size() < 2) {
                return CommandResponse.SEND_SYNTAX;
            }

            Group group = getGroupFromName(args.get(0));
            group.permsContainer.remove(args.get(1));
            getManager().saveGroups();
            sendChat(sender, "mypermissions.notification.group.perm.removed");

            return CommandResponse.DONE;
        }

        @Command(
                name = "list",
                permission = "mypermissions.cmd.group.perm.list",
                parentName = "mypermissions.cmd.group.perm",
                syntax = "/perm group perm list <group>")
        public static CommandResponse groupPermListCommand(ICommandSender sender, List<String> args) {
            if(args.size() < 1) {
                return CommandResponse.SEND_SYNTAX;
            }

            Group group = getGroupFromName(args.get(0));
            sendChat(sender, "mypermissions.notification.group.perm.list", group.getName(), group.permsContainer.toString());
            return CommandResponse.DONE;
        }

        @Command(
                name = "user",
                permission = "mypermissions.cmd.user",
                parentName = "mypermissions.cmd",
                syntax = "/perm user <command>")
        public static CommandResponse userCommand(ICommandSender sender, List<String> args) {
            return CommandResponse.SEND_HELP_MESSAGE;
        }

        @Command(
                name = "group",
                permission = "mypermissions.cmd.user.group",
                parentName = "mypermissions.cmd.user",
                syntax = "/perm user group <player> <group>")
        public static CommandResponse userGroupCommand(ICommandSender sender, List<String> args) {
            if(args.size() < 2) {
                return CommandResponse.SEND_SYNTAX;
            }

            UUID uuid = getUUIDFromUsername(args.get(0));
            Group group = getGroupFromName(args.get(1));

            User user = getManager().users.get(uuid);
            if(user == null) {
                getManager().users.add(new User(uuid, group));
            } else {
                user.setGroup(group);
            }
            getManager().saveUsers();
            sendChat(sender, "mypermissions.notification.user.group.set");

            return CommandResponse.DONE;
        }

        @Command(
                name = "perm",
                permission = "mypermissions.cmd.user.perm",
                parentName = "mypermissions.cmd.user",
                syntax = "/perm user perm <command>")
        public static CommandResponse userPermCommand(ICommandSender sender, List<String> args) {
            return CommandResponse.SEND_HELP_MESSAGE;
        }

        @Command(
                name = "add",
                permission = "mypermissions.cmd.user.perm.add",
                parentName = "mypermissions.cmd.user.perm",
                syntax = "/perm user perm add <player> <perm>")
        public static CommandResponse userPermAddCommand(ICommandSender sender, List<String> args) {
            if(args.size() < 2) {
                return CommandResponse.SEND_SYNTAX;
            }

            UUID uuid = getUUIDFromUsername(args.get(0));
            User user = getManager().users.get(uuid);
            user.permsContainer.add(args.get(1));
            getManager().saveUsers();
            sendChat(sender, "mypermissions.notification.user.perm.added");

            return CommandResponse.DONE;
        }

        @Command(
                name = "remove",
                permission = "mypermissions.cmd.user.perm.remove",
                parentName = "mypermissions.cmd.user.perm",
                syntax = "/perm user perm remove <player> <perm>")
        public static CommandResponse userPermRemoveCommand(ICommandSender sender, List<String> args) {
            if(args.size() < 2) {
                return CommandResponse.SEND_SYNTAX;
            }

            UUID uuid = getUUIDFromUsername(args.get(0));
            User user = getManager().users.get(uuid);
            user.permsContainer.remove(args.get(1));
            getManager().saveUsers();
            sendChat(sender, "mypermissions.notification.user.perm.removed");

            return CommandResponse.DONE;
        }

        @Command(
                name = "list",
                permission = "mypermissions.cmd.user.perm.list",
                parentName = "mypermissions.cmd.user.perm",
                syntax = "/perm user perm list <player>")
        public static CommandResponse userPermListCommand(ICommandSender sender, List<String> args) {
            if(args.size() < 1) {
                return CommandResponse.SEND_SYNTAX;
            }

            UUID uuid = getUUIDFromUsername(args.get(0));
            User user = getManager().users.get(uuid);
            getManager().saveUsers();
            sendChat(sender, "mypermissions.notification.user.perm.list", user.permsContainer.toString());

            return CommandResponse.DONE;
        }
    }

    private static MyPermissionsManager getManager() {
        return (MyPermissionsManager) PermissionProxy.getPermissionManager();
    }
}
