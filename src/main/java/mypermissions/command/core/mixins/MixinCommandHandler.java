package mypermissions.command.core.mixins;
import net.minecraft.command.ICommand;

import java.util.*;

//@Mixin({CommandHandler.class})
public abstract class MixinCommandHandler {

    //@Shadow
    private final Map<String, ICommand> commandMap = new HashMap<String, ICommand>();

    //@Shadow
    private final Set<ICommand> commandSet = new HashSet<ICommand>();

    /*
    @Overwrite
    public List<String> getPossibleCommands(ICommandSender sender, String p_71558_2_)
    {
        String[] cmdLine = p_71558_2_.split(" ", -1);
        String commandName = cmdLine[0];

        if (cmdLine.length == 1)
        {
            List commandNames = new ArrayList();
            Set<ICommand> commandSet = new HashSet<ICommand>();
            for (Map.Entry<String, ICommand> cmd : commandMap.entrySet())
                if (!commandSet.contains(cmd) && CommandBase.doesStringStartWith(commandName, cmd.getKey())
                        && PermissionManager.checkPermission(sender, cmd.getValue()))
                {
                    commandNames.add(cmd.getKey());
                    commandSet.add(cmd.getValue());
                }
            return commandNames;
        }
        else
        {
            if (cmdLine.length > 1)
            {
                ICommand cmd = commandMap.get(commandName);
                if (cmd != null)
                    return cmd.addTabCompletionOptions(sender, ServerUtil.dropFirst(cmdLine));
            }
            return null;
        }
    }
    */
    /*

    @Overwrite
    public List<ICommand> getPossibleCommands(ICommandSender sender)
    {
        MyPermissions.instance.LOG.info("Worksssssssssssssssss");
        List<ICommand> commands = new ArrayList<ICommand>();
        if(sender instanceof EntityPlayer) {
            UUID uuid = ((EntityPlayer) sender).getGameProfile().getId();
            for (ICommand command : new HashSet<ICommand>(commandMap.values())) {
                if (PermissionProxy.getPermissionManager().hasPermission(uuid, "cmd." + command.getCommandName())) {
                    commands.add(command);
                }
            }
        }

        return commands;
    }
    */

}

