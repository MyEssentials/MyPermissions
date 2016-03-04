package mypermissions.permissions;

import junit.framework.Assert;
import metest.api.BaseTest;
import metest.api.TestPlayer;
import mypermissions.permission.api.proxy.PermissionProxy;
import mypermissions.permission.core.bridge.MyPermissionsBridge;
import mypermissions.permission.core.entities.Group;
import mypermissions.permission.core.entities.User;
import net.minecraft.entity.player.EntityPlayerMP;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class PermissionsTest extends BaseTest {

    private EntityPlayerMP player1, player2;
    private User user1, user2;
    private Group defaultGroup;

    @Before
    public void shouldInitPlayers() {

        player1 = new TestPlayer(server, "Permissions Tester 1");
        player2 = new TestPlayer(server, "Permissions Tester 2");

        user1 = registerPlayer(player1.getPersistentID());
        user2 = registerPlayer(player2.getPersistentID());

        defaultGroup = user1.group;

        Assert.assertEquals("User1 should have been registered", getManager().users.get(player1.getPersistentID()), user1);
        Assert.assertEquals("User2 should have been registered", getManager().users.get(player2.getPersistentID()), user2);

    }

    private User registerPlayer(UUID uuid) {
        getManager().users.add(uuid);
        return getManager().users.get(uuid);
    }

    @Test
    public void shouldNotRegisterUsersTwice() {

        getManager().users.add(user1);

        int user1Counter = 0;
        for(User user : getManager().users) {
            if(user == user1) {
                user1Counter++;
            }
        }
        Assert.assertEquals("Users should not be able to be registered twice", 1, user1Counter);

    }

    @Test
    public void shouldNotHavePermissions() {

        Assert.assertFalse("Player1 should not have permission to modexample.perm1", getManager().hasPermission(player1.getPersistentID(), "modexample.perm1"));
        Assert.assertFalse("Player2 should not have permission to modexample.perm1", getManager().hasPermission(player2.getPersistentID(), "modexample.perm1"));

    }

    @Test
    public void shouldNotHaveParentPermission() {

        user1.permsContainer.add("modexample.parent.child1");
        Assert.assertFalse("Player1 should not have permission to modexample.parent while having permission to modexample.parent.child1", getManager().hasPermission(player1.getPersistentID(), "modexample.parent"));
        user1.permsContainer.remove("modexample.parent.child1");

    }

    @Test
    public void shouldHaveChildPermission() {

        user1.permsContainer.add("modexample.parent.*");
        Assert.assertTrue("Player1 should have permission to modexample.parent.child1 while having permission to modexample.parent.*", getManager().hasPermission(player1.getPersistentID(), "modexample.parent.child1"));
        user1.permsContainer.remove("modexample.parent.*");

    }

    @Test
    public void shouldShareGroupPermissions() {

        defaultGroup.permsContainer.add("modexample.parent.child1");
        Assert.assertTrue("Player1 should have permission to modexample.parent.child1 from the default group", getManager().hasPermission(player1.getPersistentID(), "modexample.parent.child1"));
        Assert.assertTrue("Player2 should have permission to modexample.parent.child1 from the default group", getManager().hasPermission(player2.getPersistentID(), "modexample.parent.child1"));
        defaultGroup.permsContainer.remove("modexample.parent.child1");

    }

    @Test
    public void shouldFollowProperPermissionHierarchyGroupToUser() {

        defaultGroup.permsContainer.add("modexample.parent.child1");
        user1.permsContainer.add("-modexample.parent.child1");
        Assert.assertFalse("Player1 should not have permission to modexample.parent.child1 due to explicit permission removal in user perms", getManager().hasPermission(player1.getPersistentID(), "modexample.parent.child1"));
        Assert.assertTrue("Player2 should have permission to modexample.parent.child1 while Player1 does not", getManager().hasPermission(player2.getPersistentID(), "modexample.parent.child1"));
        user1.permsContainer.remove("-modexample.parent.child1");
        defaultGroup.permsContainer.remove("modexample.parent.child1");

    }

    @Test
    public void shouldFollowProperPermissionHierarchyParentGroupToChildGroup() {

        Group childGroup = new Group("child");
        childGroup.parents.add(defaultGroup);
        getManager().groups.add(childGroup);

        user1.group = childGroup;
        defaultGroup.permsContainer.add("modexample.parent.child1");
        childGroup.permsContainer.add("-modexample.parent.child1");

        Assert.assertFalse("Player1 should not have permission to modexample.parent.child1 due to explicit permission removal in group perms", getManager().hasPermission(player1.getPersistentID(), "modexample.parent.child1"));
        Assert.assertTrue("Player2 should have permission to modexample.parent.child1 while Player1 does not", getManager().hasPermission(player2.getPersistentID(), "modexample.parent.child1"));

        getManager().groups.remove("child");
        defaultGroup.permsContainer.remove("modexample.parent.child1");
        user1.group = defaultGroup;

    }



    // REF: make an easier way to get the permissions manager
    private MyPermissionsBridge getManager() {
        return ((MyPermissionsBridge)PermissionProxy.getPermissionManager());
    }


}
