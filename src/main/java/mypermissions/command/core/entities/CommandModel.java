package mypermissions.command.core.entities;

import mypermissions.permission.api.proxy.PermissionProxy;
import mypermissions.permission.core.bridge.ForgeEssentialsPermissionBridge;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.permission.PermissionLevel;
import net.minecraftforge.permission.PermissionObject;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Command model which instantiates all base commands that need to be registered to Minecraft
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "net.minecraftforge.permission.PermissionObject", modid = "ForgeEssentials")})
public class CommandModel extends CommandBase implements PermissionObject {

    private CommandTree commandTree;

    public CommandModel(CommandTree commandTree) {
        this.commandTree = commandTree;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList(commandTree.getRoot().getAnnotation().alias());
    }

    /**
     * Processes the command by calling the method that was linked to it.
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        commandTree.commandCall(sender, Arrays.asList(args));
    }

    @Override
    public String getCommandName() {
        return commandTree.getRoot().getLocalizedName();
    }

    public String getCommandUsage(ICommandSender sender) {
        return commandTree.getRoot().getLocalizedSyntax();
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        CommandTreeNode node = commandTree.getNodeFromArgs(Arrays.asList(args));

        int argumentNumber = commandTree.getArgumentNumber(Arrays.asList(args));
        if(argumentNumber < 0)
            return null;

        return node.getTabCompletionList(argumentNumber, args[args.length - 1]);
    }

    /**
     * This method does not have enough arguments to check for subcommands down the command trees therefore it always returns true.
     * The check is moved directly to the execute method.
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getPermissionNode() {
        return commandTree.getRoot().getAnnotation().permission();
    }

    @Override
    @Optional.Method(modid = "ForgeEssentials")
    public PermissionLevel getPermissionLevel() {
        return PermissionLevel.fromBoolean(!(PermissionProxy.getPermissionManager() instanceof ForgeEssentialsPermissionBridge));
    }
}
