package mypermissions.exception;

import mypermissions.localization.LocalizationProxy;
import net.minecraft.command.CommandException;

public class PermissionCommandException extends CommandException {
    public PermissionCommandException(String localKey, Object... args) {
        super(LocalizationProxy.getLocalization().getLocalization(localKey, args));
    }
}
