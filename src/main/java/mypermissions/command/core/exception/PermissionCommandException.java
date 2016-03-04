package mypermissions.command.core.exception;

import mypermissions.MyPermissions;
import net.minecraft.command.CommandException;

public class PermissionCommandException extends CommandException {
    public PermissionCommandException(String localKey, Object... args) {
        super(MyPermissions.instance.LOCAL.getLocalization(localKey, args).getUnformattedText());
    }
}
