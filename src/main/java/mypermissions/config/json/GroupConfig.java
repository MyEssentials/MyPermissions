package mypermissions.config.json;

import com.google.common.reflect.TypeToken;
import myessentials.json.JSONConfig;
import mypermissions.MyPermissions;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GroupConfig extends JSONConfig<GroupConfig.Wrapper> {

    public GroupConfig(String path) {
        super(path);
        gsonType = new TypeToken<List<Wrapper>>() {}.getType();
    }

    @Override
    protected List<Wrapper> create() {
        List<Wrapper> wrappers = new ArrayList<Wrapper>();
        try {
            Writer writer = new FileWriter(path);
            gson.toJson(wrappers, gsonType, writer);
            writer.close();
            MyPermissions.instance.LOG.info("Successfully loaded GroupConfig!");
        } catch (Exception e) {
            MyPermissions.instance.LOG.info("Failed to load GroupConfig!");
            MyPermissions.instance.LOG.info(ExceptionUtils.getStackTrace(e));
        }
        return wrappers;
    }

    @Override
    public void write(List<Wrapper> items) {
        try {
            Writer writer = new FileWriter(path);
            gson.toJson(items, gsonType, writer);
            writer.close();

            MyPermissions.instance.LOG.info("Updated GroupConfig file successfully!");
        } catch (IOException e) {
            MyPermissions.instance.LOG.error("Failed to update GroupConfig file!");
            MyPermissions.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    protected List<Wrapper> read() {
        List<Wrapper> wrappers = new ArrayList<Wrapper>();
        try {
            Reader reader = new FileReader(path);
            wrappers = gson.fromJson(reader, gsonType);
            reader.close();




            MyPermissions.instance.LOG.info("Loaded GroupConfig file successfully!");
        } catch (IOException e) {
            MyPermissions.instance.LOG.error("Failed to load GroupConfig file!");
            MyPermissions.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }
        return wrappers;
    }

    @Override
    protected void update(List<Wrapper> items) {
        write(items);
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
