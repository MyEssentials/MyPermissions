package mypermissions.config.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import myessentials.json.JsonConfig;
import mypermissions.api.container.GroupsContainer;
import mypermissions.api.container.MetaContainer;
import mypermissions.api.entities.Group;
import mypermissions.config.Config;
import mypermissions.manager.MyPermissionsManager;

import java.util.List;

public class GroupConfig extends JsonConfig<Group, GroupsContainer> {

    private MyPermissionsManager permissionManager;

    public GroupConfig(String path, MyPermissionsManager permissionManager) {
        super(path, "GroupConfig");
        this.permissionManager = permissionManager;
        this.gsonType = new TypeToken<GroupsContainer>() {}.getType();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Group.class, new Group.Serializer())
                .registerTypeAdapter(MetaContainer.class, new MetaContainer.Serializer())
                .setPrettyPrinting().create();
    }

    @Override
    protected GroupsContainer newList() {
        return new GroupsContainer();
    }

    @Override
    public void create(GroupsContainer items) {
        items.add(new Group("default"));
        super.create(items);
    }

    @Override
    public GroupsContainer read() {
        GroupsContainer groups = super.read();

        permissionManager.groups.addAll(groups);

        return groups;
    }

    @Override
    public boolean validate(GroupsContainer items) {
        if (items.size() == 0) {
            items.add(new Group(Config.instance.defaultGroup.get()));
            return false;
        }

        return true;
    }
}