package mypermissions.config.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import myessentials.json.JSONConfig;
import mypermissions.MyPermissions;
import mypermissions.api.entities.Group;
import mypermissions.manager.MyPermissionsManager;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GroupConfig extends JSONConfig<Group> {

    private MyPermissionsManager permissionManager;

    public GroupConfig(String path, MyPermissionsManager permissionManager) {
        super(path, "GroupConfig");
        this.permissionManager = permissionManager;
        this.gsonType = new TypeToken<List<Group>>() {}.getType();
        this.gson = new GsonBuilder().registerTypeAdapter(gsonType, new GroupTypeAdapter()).setPrettyPrinting().create();
    }

    @Override
    protected void create(List<Group> items) {
        items.add(new Group("default", null, null, Group.Type.DEFAULT));
        super.create(items);
    }

    @Override
    protected List<Group> read() {
        List<Group> groups = super.read();

        permissionManager.groups.addAll(groups);

        return groups;
    }

    @Override
    protected boolean validate(List<Group> items) {

        if(items.size() == 0) {
            items.add(new Group("default", null, null, Group.Type.DEFAULT));
            return false;
        }

        boolean valid = true;
        boolean defaultGroup = false;

        for(Group group : items) {
            if(group.getType() == Group.Type.DEFAULT) {
                if(defaultGroup) {
                    group.setType(Group.Type.NORMAL);
                    valid = false;
                } else {
                    defaultGroup = true;
                }
            }
        }

        if(!defaultGroup) {
            for(Group group : items) {
                if(group.getName().equals("default")) {
                    group.setType(Group.Type.DEFAULT);
                    defaultGroup = true;
                    valid = false;
                }
            }
        }

        if(!defaultGroup) {
            items.add(new Group("default", null, null, Group.Type.DEFAULT));
            valid = false;
        }

        return valid;
    }
}