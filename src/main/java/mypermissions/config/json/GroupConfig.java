package mypermissions.config.json;

import com.google.common.reflect.TypeToken;
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
                List<Group> parents = new ArrayList<Group>();
                Group group;
                if(wrapper.parents != null) {
                    for (String parentName : wrapper.parents) {
                        Group parent = permissionManager.getGroup(parentName);
                        if (parent == null) {
                            MyPermissions.instance.LOG.error("Group " + wrapper.name + " has an invalid parent " + parentName + ". Ignoring parent...");
                        } else {
                            parents.add(parent);
                        }
                    }
                }
                if(permissionManager.getGroup(wrapper.name) != null) {
                    MyPermissions.instance.LOG.error("Group with name " + wrapper.name + " is registered twice. Ignoring everything but the first instance...");
                } else {
                    permissionManager.addGroup(new Group(wrapper.name, wrapper.permissions, parents));
                }
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

    public List<Wrapper> convert(List<Group> items) {
        List<Wrapper> wrappedList = new ArrayList<Wrapper>();
        for(Group group : items) {
            List<String> parentNames = new ArrayList<String>();
            for(Group parent : group.getParents()) {
                parentNames.add(parent.getName());
            }

            wrappedList.add(new Wrapper(group.getName(), group.getPermissions(), parentNames));
        }
        return wrappedList;
    }

    public class Wrapper {
        public String name;
        public List<String> permissions;
        public List<String> parents;

        public Wrapper(String name, List<String> permissions, List<String> parents) {
            this.name = name;
            this.parents = parents;
            this.permissions = permissions;
        }
    }
}
