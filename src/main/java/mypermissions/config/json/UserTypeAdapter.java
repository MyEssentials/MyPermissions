package mypermissions.config.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import mypermissions.api.entities.Group;
import mypermissions.api.entities.Meta;
import mypermissions.api.entities.User;
import mypermissions.localization.PermissionProxy;
import mypermissions.manager.MyPermissionsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserTypeAdapter extends TypeAdapter<List<User>>{

    @Override
    public void write(JsonWriter out, List<User> users) throws IOException {
        out.beginArray();
        for(User user : users) {
            out.beginObject();

            out.name("uuid").value(user.uuid.toString());
            out.name("group").value(user.group.name);

            out.name("permissions").beginArray();
            for(String permission : user.permsContainer.asList()) {
                out.value(permission);
            }
            out.endArray();

            out.name("meta").beginObject();
            for(Meta meta : user.metaContainer.asList()) {
                out.name(meta.permission).value(meta.metadata);
            }
            out.endObject();

            out.endObject();
        }
        out.endArray();
    }

    @Override
    public List<User> read(JsonReader in) throws IOException {
        List<User> users = new ArrayList<User>();

        in.beginArray();
        while(in.peek() != JsonToken.END_ARRAY) {
            UUID uuid = null;
            String groupName = null;
            List<String> permissions = new ArrayList<String>();
            List<Meta> metaList = new ArrayList<Meta>();

            in.beginObject();
            while(in.peek() != JsonToken.END_OBJECT) {
                String nextName = in.nextName();

                if ("uuid".equals(nextName)) {
                    uuid = UUID.fromString(in.nextString());
                }

                if ("group".equals(nextName)) {
                    groupName = in.nextString();
                }

                if ("permisions".equals(nextName)) {
                    in.beginArray();
                    while(in.peek() != JsonToken.END_ARRAY) {
                        permissions.add(in.nextString());
                    }
                    in.endArray();
                }

                if ("meta".equals(nextName)) {
                    in.beginObject();
                    while(in.peek() != JsonToken.END_OBJECT) {
                        metaList.add(new Meta(in.nextName(), in.nextInt()));
                    }
                    in.endObject();
                }
            }
            in.endObject();

            if(uuid == null) {
                throw new IOException("One of the UUIDs are missing from the UserConfig");
            }

            if(groupName == null) {
                throw new IOException("One of the groups are missing from the UserConfig");
            }

            MyPermissionsManager permissionsManager = (MyPermissionsManager) PermissionProxy.getPermissionManager();
            Group group = permissionsManager.getGroup(groupName);

            if(group == null) {
                throw new IOException("Group " + groupName + " does not exist.");
            }

            User user = new User(uuid, group);
            user.metaContainer.add(metaList);
            user.permsContainer.add(permissions);
            users.add(user);
        }
        in.endArray();

        return users;
    }
}
