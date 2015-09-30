package mypermissions.config.json;


import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import myessentials.json.JsonConfig;
import mypermissions.api.container.UsersContainer;
import mypermissions.api.entities.User;
import mypermissions.manager.MyPermissionsManager;

import java.util.*;

public class UserConfig extends JsonConfig<User, UsersContainer> {

    private MyPermissionsManager permissionsManager;

    public UserConfig(String path, MyPermissionsManager permissionsManager) {
        super(path, "UserConfig");
        this.permissionsManager = permissionsManager;
        this.gsonType = new TypeToken<List<User>>() {}.getType();
        this.gson = new GsonBuilder().registerTypeAdapter(gsonType, new UserTypeAdapter()).setPrettyPrinting().create();
    }

    @Override
    protected UsersContainer newList() {
        return new UsersContainer();
    }

    @Override
    public UsersContainer read() {
        UsersContainer users = super.read();
        permissionsManager.users.addAll(users);
        return users;
    }
}