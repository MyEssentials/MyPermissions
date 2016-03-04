package mypermissions.command;

import junit.framework.Assert;
import metest.api.BaseTest;
import metest.api.TestPlayer;
import mypermissions.MyPermissions;
import mypermissions.command.api.CommandManager;
import mypermissions.command.core.entities.CommandTree;
import mypermissions.permission.api.proxy.PermissionProxy;
import mypermissions.permission.core.bridge.MyPermissionsBridge;
import mypermissions.permission.core.entities.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class CommandTest extends BaseTest {

    private CommandTree commandTree;
    private TestPlayer player;
    private static User user;

    @Before
    public void shouldInitCommands() {
        player = new TestPlayer(server, "Command Tester");
        // REF: Adding a user directly to the manager does NOT give it a default group
        ((MyPermissionsBridge)PermissionProxy.getPermissionManager()).users.add(player.getPersistentID());
        user = ((MyPermissionsBridge)PermissionProxy.getPermissionManager()).users.get(player.getPersistentID());
        user.permsContainer.add("mypermissions.test.*");
        user.permsContainer.add("mypermissions.test");
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
        sendCommand("test add 12 13");
        Assert.assertEquals("Command did not get processed!", 25, FakeCommands.lastResult);
    }

    @Test
    public void shouldFailToProcessCommand() {
        sendCommand("/test add 12 blah");
        Assert.assertEquals("Command should have failed and the player should get a message", "Num2 is not an integer", player.lastMessage);
    }

    private void sendCommand(String command) {
        server.getCommandManager().executeCommand(player, command);
    }

    @AfterClass
    public static void tearDown() {
        // REF: Removing users by uuid is not yet implemented
        ((MyPermissionsBridge)PermissionProxy.getPermissionManager()).users.remove(user);
    }
}
