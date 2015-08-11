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
        items.add(new Group("default", null, null, null));
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
            create(items);
            return false;
        }
        return true;
    }
}