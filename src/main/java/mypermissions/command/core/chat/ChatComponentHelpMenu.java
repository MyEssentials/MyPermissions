package mypermissions.command.core.chat;

import myessentials.chat.api.ChatComponentFormatted;
import myessentials.chat.api.ChatComponentMultiPage;
import mypermissions.command.core.entities.CommandTreeNode;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class ChatComponentHelpMenu extends ChatComponentMultiPage {

    private CommandTreeNode command;

    public ChatComponentHelpMenu(int maxComponentsPerPage, CommandTreeNode command) {
        super(maxComponentsPerPage);
        this.command = command;
        this.construct();
    }

    public void construct() {

        for (CommandTreeNode subCommand : command.getChildren()) {
            this.add(new ChatComponentFormatted("{7| %s}", subCommand.getCommandLine()));
        }

    }

    @Override
    public List<IChatComponent> getHeader(int page) {
        List<IChatComponent> header = super.getHeader(page);
        header.add(new ChatComponentFormatted("{9| - Command: }{9o|%s}", command.getLocalizedSyntax()));
        return header;
    }
}
