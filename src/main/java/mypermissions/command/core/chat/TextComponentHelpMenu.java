package mypermissions.command.core.chat;

import myessentials.chat.api.TextComponentContainer;
import myessentials.chat.api.TextComponentFormatted;
import myessentials.chat.api.TextComponentMultiPage;
import myessentials.localization.api.LocalManager;
import mypermissions.command.core.entities.CommandTreeNode;

public class TextComponentHelpMenu extends TextComponentMultiPage {

    private CommandTreeNode command;

    public TextComponentHelpMenu(int maxComponentsPerPage, CommandTreeNode command) {
        super(maxComponentsPerPage);
        this.command = command;
        this.construct();
    }

    public void construct() {

        for (CommandTreeNode subCommand : command.getChildren()) {
            this.add(new TextComponentFormatted("{7| %s << %s}", subCommand.getCommandLine(), LocalManager.get(subCommand.getAnnotation().permission() + ".help")));
        }

    }

    @Override
    public TextComponentContainer getHeader(int page) {
        TextComponentContainer header = super.getHeader(page);
        header.add(new TextComponentFormatted("{9| - Command: }{9o|%s}", command.getLocalizedSyntax()));
        return header;
    }
}
