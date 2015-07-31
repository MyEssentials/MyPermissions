package mypermissions.config.json;


import com.google.common.reflect.TypeToken;
import myessentials.json.JSONConfig;
import mypermissions.MyPermissions;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class PlayerConfig extends JSONConfig<PlayerConfig.Wrapper> {

    public PlayerConfig(String path) {
        super(path);
        gsonType = new TypeToken<List<Wrapper>>() {}.getType();
    }

    @Override
    protected List<PlayerConfig.Wrapper> create() {
        List<Wrapper> wrappers = new ArrayList<Wrapper>();

        try {
            Writer writer = new FileWriter(path);
            gson.toJson(wrappers, gsonType, writer);
            writer.close();
            MyPermissions.instance.LOG.info("Successfully loaded PlayerConfig!");
        } catch (Exception e) {
            MyPermissions.instance.LOG.error("Failed to create PlayerConfig");
            MyPermissions.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }

        return wrappers;
    }

    @Override
    public void write(List<PlayerConfig.Wrapper> items) {
        try {
            Writer writer = new FileWriter(path);
            gson.toJson(items, gsonType, writer);
            writer.close();
            MyPermissions.instance.LOG.info("Successfully loaded PlayerConfig!");
        } catch (Exception e) {
            MyPermissions.instance.LOG.error("Failed to create PlayerConfig");
            MyPermissions.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    protected List<PlayerConfig.Wrapper> read() {
        return null;
    }

    @Override
    protected void update(List<PlayerConfig.Wrapper> items) {

    }

    public class Wrapper {
        public String uuid;
        public String groupName;

        public Wrapper(String uuid, String groupName) {
            this.uuid = uuid;
            this.groupName = groupName;
        }
    }
}

