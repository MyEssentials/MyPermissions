package mypermissions.command.core.exception;

import myessentials.exception.FormattedException;

public class CommandException extends FormattedException {

    public CommandException(String localizationKey, Object... args) {
        super(localizationKey, args);
    }
}
