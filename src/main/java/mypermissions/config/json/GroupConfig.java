package mypermissions.config.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import myessentials.json.JsonConfig;
import mypermissions.api.container.GroupsContainer;
import mypermissions.api.entities.Group;
import mypermissions.manager.MyPermissionsManager;

import java.util.List;

public class GroupConfig extends JsonConfig<Group, GroupsContainer> {

    private MyPermissionsManager permissionManager;

    public GroupConfig(String path, MyPermissionsManager permissionManager) {
        super(path, "GroupConfig");
        this.permissionManager = permissionManager;
        this.gsonType = new TypeToken<GroupsContainer>() {}.getType();
        this.gson = new GsonBuilder().registerTypeAdapter(gsonType, new GroupTypeAdapter()).setPrettyPrinting().create();
    }

    @Override
    protected GroupsContainer newList() {
        return new GroupsContainer();
    }

    @Override
    public void create(GroupsContainer items) {
        items.add(new Group("default", null, null, Group.Type.DEFAULT));
        super.create(items);
    }

    @Override
    public GroupsContainer read() {
        GroupsContainer groups = super.read();

        permissionManager.groups.addAll(groups);
        for (Group group : groups) {
            if (group.getType() == Group.Type.DEFAULT) {
                permissionManager.users.setDefaultGroup(group);
                break;
            }
        }

        return groups;
    }

    @Override
    public boolean validate(GroupsContainer items) {

        if (items.size() == 0) {
            items.add(new Group("default", null, null, Group.Type.DEFAULT));
            return false;
        }

        boolean valid = true;
        boolean defaultGroup = false;

        for (Group group : items) {
            if (group.getType() == Group.Type.DEFAULT) {
                if (defaultGroup) {
                    group.setType(Group.Type.NORMAL);
                    valid = false;
                } else {
                    defaultGroup = true;
                }
            }
        }

        if (!defaultGroup) {
            for (Group group : items) {
                if (group.getName().equals("default")) {
                    group.setType(Group.Type.DEFAULT);
                    defaultGroup = true;
                    valid = false;
                }
            }
        }

        if (!defaultGroup) {
            items.add(new Group("default", null, null, Group.Type.DEFAULT));
            valid = false;
        }

        return valid;
    }
}