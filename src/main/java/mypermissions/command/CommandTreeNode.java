package mypermissions.command;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import myessentials.Localization;
import myessentials.MyEssentialsCore;
import myessentials.chat.HelpMenu;
import mypermissions.api.command.CommandCompletion;
import mypermissions.api.command.CommandManager;
import mypermissions.api.command.CommandResponse;
import mypermissions.api.command.annotation.Command;
import myessentials.entities.TreeNode;
import myessentials.utils.StringUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The declaration is a bit difficult to understand.
 * It means that the super class "TreeNode" should work with other "CommandTreeNode" objects.
 */
public class CommandTreeNode extends TreeNode<CommandTreeNode> {

    private Command commandAnnot;
    private Method method;
    private String localizationKey;

    private HelpMenu helpMenu;
    private Supplier<String[]> alias = Suppliers.memoizeWithExpiration(new Supplier<String[]>() {
        @Override
        public String[] get() {
            String key = getLocalizationKey()+".alias";
            return getLocal().hasLocalization(key)? getLocal().getLocalization(key).split("\\s*,\\s*"): getAnnotation().alias();
        }
    }, 5, TimeUnit.MINUTES);

    private Supplier<String> name = Suppliers.memoizeWithExpiration(new Supplier<String>() {
        @Override
        public String get() {
            String key = getLocalizationKey()+".name";
            return getLocal().hasLocalization(key)? getLocal().getLocalization(key): getAnnotation().name();
        }
    }, 5, TimeUnit.MINUTES);

    private Supplier<String> syntax = Suppliers.memoizeWithExpiration(new Supplier<String>() {
        @Override
        public String get() {
            String key = getLocalizationKey()+".syntax";
            return getLocal().hasLocalization(key)? getLocal().getLocalization(key): getAnnotation().syntax();
        }
    }, 5, TimeUnit.MINUTES);

    public CommandTreeNode(Command commandAnnot, Method method) {
        this(null, commandAnnot, method);
    }

    public CommandTreeNode(CommandTreeNode parent, Command commandAnnot, Method method) {
        this.parent = parent;
        this.commandAnnot = commandAnnot;
        this.method = method;

        String name = getAnnotation().name();
        CommandTreeNode parentNode = this;
        while ((parentNode = parentNode.getParent()) != null) {
            name = parentNode.getAnnotation().name() + "." + name;
        }
        localizationKey = "command."+name;
    }

    public Command getAnnotation() {
        return commandAnnot;
    }

    public Method getMethod() {
        return method;
    }

    public void commandCall(ICommandSender sender, List<String> args) {


        /*
        // Check if the player has access to the command using the firstpermissionbreach method first
        Method permMethod = firstPermissionBreaches.get(permission);
        if(permMethod != null) {
            Boolean result = true;
            try {
                result = (Boolean)permMethod.invoke(null, permission, sender);
            } catch (Exception e) {
                MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e));
            }
            if(!result) {
                // If the first permission breach did not allow the method to be called then call is aborted
                throw new CommandException("commands.generic.permission");
            }
        }
        */

        try {
            CommandResponse response = (CommandResponse)method.invoke(null, sender, args);
            if(response == CommandResponse.SEND_HELP_MESSAGE) {
                int page = 1;
                if(!args.isEmpty() && StringUtils.tryParseInt(args.get(0)))
                    page = Integer.parseInt(args.get(0));
                sendHelpMessage(sender, page);
            } else if(response == CommandResponse.SEND_SYNTAX) {
                sendSyntax(sender);
            }
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException)
                throw (RuntimeException) e.getCause();
            else
                MyEssentialsCore.instance.LOG.info(ExceptionUtils.getStackTrace(e));
        } catch (Exception e2) {
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e2));
        }
    }

    public List<String> getTabCompletionList(int argumentNumber, String argumentStart) {
        List<String> completion = new ArrayList<String>();
        if(commandAnnot.completionKeys().length == 0) {
            for(CommandTreeNode child : getChildren()) {
                String localizedCommand = child.getLocalizedName();
                if(localizedCommand.startsWith(argumentStart)) {
                    completion.add(localizedCommand);
                }
            }
        } else {
            if(argumentNumber < commandAnnot.completionKeys().length) {
                for(String s : CommandCompletion.getCompletionList(commandAnnot.completionKeys()[argumentNumber])) {
                    if(s.startsWith(argumentStart)) {
                        completion.add(s);
                    }
                }
            }
        }
        return completion;
    }

    public void sendHelpMessage(ICommandSender sender, int page) {
        if(helpMenu == null)
            constructHelpMenu();
        helpMenu.sendHelpPage(sender, page);
    }

    public void sendSyntax(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + getLocalizedSyntax()));
    }

    public String getLocalizationKey() {
        return localizationKey;
    }

    public String getLocalizedSyntax() {
        return syntax.get();
    }

    public String getLocalizedName() {
        return name.get();
    }

    public String[] getLocalizedAlias() {
        return alias.get();
    }

    public CommandTreeNode getChild(String name) {
        for(CommandTreeNode child : getChildren()) {
            if(child.getLocalizedName().equals(name))
                return child;
            for(String alias : getLocalizedAlias())
                if(alias.equals(name))
                    return child;
        }
        return null;
    }

    public String getCommandLine() {
        if(getParent() == null)
            return "/" + getLocalizedName();
        else
            return getParent().getCommandLine() + " " + getLocalizedName();
    }

    private void constructHelpMenu() {
        String commandLine = getCommandLine();
        helpMenu = new HelpMenu(getLocalizedSyntax());
        if(getChildren().isEmpty()) {
            helpMenu.addLine(getLocal().getLocalization(getAnnotation().permission() + ".help"));
        } else {
            for (CommandTreeNode child : getChildren()) {
                helpMenu.addLineWithHoverText(commandLine + " " + child.getLocalizedName(), getLocal().getLocalization(child.getAnnotation().permission() + ".help"));
            }
        }
    }

    public Localization getLocal() {
        return getCommandTree().getLocal();
    }

    public CommandTree getCommandTree() {
        CommandTreeNode node = this;

        while(node.getParent() != null) {
            node = node.getParent();
        }

        return CommandManager.getTree(node.getAnnotation().permission());
    }
}
