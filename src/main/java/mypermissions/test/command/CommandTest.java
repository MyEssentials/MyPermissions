package mypermissions.test.command;

import com.mojang.authlib.GameProfile;
import junit.framework.Assert;
import metest.BaseTest;
import myessentials.exception.CommandException;
import mypermissions.MyPermissions;
import mypermissions.api.command.CommandManager;
import mypermissions.api.entities.User;
import mypermissions.command.CommandTree;
import mypermissions.command.Commands;
import mypermissions.manager.MyPermissionsManager;
import mypermissions.proxies.PermissionProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class CommandTest extends BaseTest {

    private CommandTree commandTree;
    private EntityPlayerMP player;

    @Before
    public void shouldInitCommands() {
        player = new FakePlayer(server.worldServers[0], new GameProfile(UUID.randomUUID(), "CommandTester"));
        User user = new User(player.getPersistentID());
        user.permsContainer.add("mypermissions.test");
        ((MyPermissionsManager)PermissionProxy.getPermissionManager()).users.add(user);
        // REF: The conversion to the default MyPermissionsManager happens a LOT in the code. Pull some of the methods used in the common interface.
        CommandManager.registerCommands(FakeCommands.class, null, MyPermissions.instance.LOCAL, null);
        commandTree = CommandManager.getTree("mypermissions.test");

        Assert.assertNotNull("CommandTree did not get created", commandTree);
        Assert.assertNotNull("CommandTree does not have a root command registered", commandTree.getRoot());
        Assert.assertEquals("CommandTree does not have all the sub commands registered", 2, commandTree.getRoot().getChildren().size());
    }

    @Test
    public void shouldProcessCommand() {
        // REF: A layer with which the commands can interact is missing
        sendCommand("/test add 12 13");
        Assert.assertEquals("Command did not get processed!", 25, FakeCommands.lastResult);
    }

    @Test(expected = CommandException.class)
    public void shouldFailToProcessCommand() {
        sendCommand("/test add 12 blah");
    }

    private void sendCommand(String command) {
        try {
            server.getCommandManager().executeCommand(player, command);
        } catch (NullPointerException ex) {
            // Do nothing since this is because we're using FakePlayers
        }
    }
}
