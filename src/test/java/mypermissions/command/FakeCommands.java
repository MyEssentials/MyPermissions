package mypermissions.command;


import myessentials.utils.StringUtils;
import mypermissions.command.api.CommandResponse;
import mypermissions.command.api.annotation.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class FakeCommands {

    public static int lastResult = 0;

    @Command(
            name = "test",
            permission = "mypermissions.test",
            syntax = "/test <command>")
    public static CommandResponse testCommand(ICommandSender sender, List<String> args) {
        return CommandResponse.SEND_HELP_MESSAGE;
    }

    @Command(
            name = "child",
            permission = "mypermissions.test.child",
            parentName = "mypermissions.test",
            syntax = "/test child")
    public static CommandResponse testChildCommand(ICommandSender sender, List<String> args) {
        throw new CommandException("Error");
    }

    @Command(
            name = "add",
            permission = "mypermissions.test.add",
            parentName = "mypermissions.test",
            syntax = "/test add <num1> <num2>")
    public static CommandResponse testAddCommand(ICommandSender sender, List<String> args) {
        int num1, num2;
        if (StringUtils.tryParseInt(args.get(0))) {
            num1 = Integer.parseInt(args.get(0));
        } else {
            // REF: Make a suite of exception that can be thrown while processing a command
            throw new CommandException("Num1 is not an integer");
        }

        if (StringUtils.tryParseInt(args.get(1))) {
            num2 = Integer.parseInt(args.get(1));
        } else {
            throw new CommandException("Num2 is not an integer");
        }

        lastResult = num1 + num2;
        return CommandResponse.DONE;
    }

}
