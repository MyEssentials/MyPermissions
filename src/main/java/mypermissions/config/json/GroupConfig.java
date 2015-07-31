package mypermissions.config.json;

import com.google.common.reflect.TypeToken;
import myessentials.MyEssentialsCore;
import myessentials.json.JSONConfig;
import mypermissions.MyPermissions;
import mypermissions.entities.Group;
import mypermissions.manager.MyPermissionsManager;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GroupConfig extends JSONConfig<GroupConfig.Wrapper> {

    private MyPermissionsManager permissionManager;

    public GroupConfig(String path, MyPermissionsManager permissionManager) {
        super(path, "GroupConfig");
        this.permissionManager = permissionManager;
        this.gsonType = new TypeToken<List<Wrapper>>() {}.getType();
    }

    @Override
    protected void create(List<Wrapper> items) {
        items.add(new Wrapper("default", new ArrayList<String>(), null));

        super.create(items);
    }

    @Override
    protected List<Wrapper> read() {
        List<Wrapper> wrappers = new ArrayList<Wrapper>();
        try {
            Reader reader = new FileReader(path);
            wrappers = gson.fromJson(reader, gsonType);
            reader.close();



            for (Wrapper wrapper : wrappers) {
                Group group;
                if (wrapper.parentName == null) {
                    group = new Group(wrapper.name, wrapper.permissions);
                } else {
                    Group parent = permissionManager.getGroup(wrapper.parentName);
                    if(parent == null) {
                        MyEssentialsCore.instance.LOG.error("Group " + wrapper.name + " failed to load because it has an invalid parent " + wrapper.parentName);
                        continue;
                    } else {
                        group = new Group(wrapper.name, wrapper.permissions, parent);
                    }
                }
                permissionManager.addGroup(group);
            }


            MyPermissions.instance.LOG.info("Loaded GroupConfig file successfully!");
        } catch (IOException e) {
            MyPermissions.instance.LOG.error("Failed to load GroupConfig file!");
            MyPermissions.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }
        return wrappers;
    }

    @Override
    protected boolean validate(List<Wrapper> items) {
        boolean isValid = true;
        if(items.size() == 0) {
            // Adding a default group
            Wrapper defaultGroup = new Wrapper("default", new ArrayList<String>(), null);
            items.add(defaultGroup);
            isValid = false;
        }
        return isValid;
    }

    public class Wrapper {
        public String name;
        public List<String> permissions;
        public String parentName;

        public Wrapper(String name, List<String> permissions, String parentName) {
            this.name = name;
            this.parentName = parentName;
            this.permissions = permissions;
        }
    }
}
