package mypermissions.manager;

import myessentials.utils.PlayerUtils;
import mypermissions.Constants;
import mypermissions.api.IPermissionManager;
import mypermissions.api.entities.Group;
import mypermissions.api.entities.User;
import mypermissions.config.json.GroupConfig;
import mypermissions.config.json.UserConfig;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MyPermissionsManager implements IPermissionManager {

    private static final String DEFAULT_GROUP_NAME = "default";

    public final List<Group> groups = new ArrayList<Group>();
    public final List<User> users = new ArrayList<User>();

    public final GroupConfig groupConfig = new GroupConfig(Constants.CONFIG_FOLDER + "GroupConfig.json", this);
    public final UserConfig userConfig = new UserConfig(Constants.CONFIG_FOLDER + "UserConfig.json", this);

    public MyPermissionsManager() {
    }

    public void loadConfigs() {
        groupConfig.init();
        userConfig.init();
    }

    public void saveConfigs() {
        groupConfig.write(groups);
        userConfig.write(users);
    }

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        EntityPlayer player = PlayerUtils.getPlayerFromUUID(uuid);
        Group group = getPlayerGroup(player);
        return group.permsContainer.hasSuperPermission(permission);
    }

    public void removeGroup(String groupName) {
        for(Iterator<Group> it = groups.iterator(); it.hasNext();) {
            Group group = it.next();
            if(group.name.equals(groupName)) {
                it.remove();
                return;
            }
        }
    }

    public boolean hasGroup(String groupName) {
        for (Group group : groups) {
            if (group.name.equals(groupName))
                return true;
        }
        return false;
    }

    public Group getGroup(String groupName) {
        for(Group group : groups) {
            if(group.name.equals(groupName))
                return group;
        }
        return null;
    }

    public Group getPlayerGroup(EntityPlayer player) {

        for(User user : users) {
            if(user.uuid.equals(player.getPersistentID())) {
                return user.group;
            }
        }

        User user = new User(player.getPersistentID(), getGroup(DEFAULT_GROUP_NAME));
        users.add(user);
        userConfig.write(users);

        return user.group;
    }
}
