package mypermissions.config.json;


import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import myessentials.MyEssentialsCore;
import myessentials.json.JSONConfig;
import myessentials.utils.PlayerUtils;
import mypermissions.api.entities.Group;
import mypermissions.api.entities.User;
import mypermissions.manager.MyPermissionsManager;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class UserConfig extends JSONConfig<User> {

    private MyPermissionsManager permissionsManager;

    public UserConfig(String path, MyPermissionsManager permissionsManager) {
        super(path, "UserConfig");
        this.permissionsManager = permissionsManager;
        this.gsonType = new TypeToken<List<User>>() {}.getType();
        this.gson = new GsonBuilder().registerTypeAdapter(gsonType, new UserTypeAdapter()).setPrettyPrinting().create();
    }

    @Override
    protected List<User> read() {
        List<User> users = super.read();
        permissionsManager.users.addAll(users);
        return users;
    }
}