package mypermissions.config.json;


import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import myessentials.json.JsonConfig;
import mypermissions.api.entities.Meta;
import mypermissions.api.entities.User;
import mypermissions.manager.MyPermissionsManager;

public class UserConfig extends JsonConfig<User, User.Container> {

    private MyPermissionsManager permissionsManager;

    public UserConfig(String path, MyPermissionsManager permissionsManager) {
        super(path, "UserConfig");
        this.permissionsManager = permissionsManager;
        this.gsonType = new TypeToken<User.Container>() {}.getType();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new User.Serializer())
                .registerTypeAdapter(Meta.Container.class, new Meta.Container.Serializer())
                .setPrettyPrinting().create();
    }

    @Override
    protected User.Container newList() {
        return new User.Container();
    }

    @Override
    public User.Container read() {
        User.Container users = super.read();
        permissionsManager.users.addAll(users);
        return users;
    }
}