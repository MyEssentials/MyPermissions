package mypermissions.exception;

import myessentials.exception.CommandException;
import mypermissions.localization.LocalizationProxy;

public class PermissionCommandException extends CommandException {
    public PermissionCommandException(String localKey, Object... args) {
        super(LocalizationProxy.getLocalization().getLocalization(localKey, args));
    }
}
