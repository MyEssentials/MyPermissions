package mypermissions.manager;

import myessentials.utils.PlayerUtils;
import mypermissions.Constants;
import mypermissions.api.IPermissionManager;
import mypermissions.api.container.GroupsContainer;
import mypermissions.api.container.UsersContainer;
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

    public final GroupsContainer groups = new GroupsContainer();
    public final UsersContainer users = new UsersContainer();

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

    public void saveGroups() {
        groupConfig.write(groups);
    }

    public void saveUsers() {
        userConfig.write(users);
    }

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        boolean updateFile = !users.contains(uuid);
        User user = users.get(uuid);

        if(updateFile) {
            saveUsers();
        }

        return user.hasPermission(permission);
    }
}
