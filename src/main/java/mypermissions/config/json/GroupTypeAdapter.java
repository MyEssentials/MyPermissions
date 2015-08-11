package mypermissions.config.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import mypermissions.api.entities.Group;
import mypermissions.api.entities.Meta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupTypeAdapter extends TypeAdapter<List<Group>> {

    @Override
    public void write(JsonWriter out, List<Group> groups) throws IOException {
        out.beginArray();
        for(Group group : groups) {
            out.beginObject(); {
                out.name("name").value(group.name);
                out.name("type").value(group.type.toString());
                out.name("permissions").beginArray(); {
                    for (String permission : group.permsContainer.asList()) {
                        out.value(permission);
                    }
                } out.endArray();

                out.name("meta").beginObject(); {
                    for (Meta meta : group.metaContainer.asList()) {
                        out.name(meta.permission).value(meta.metadata);
                    }
                } out.endObject();

            } out.endObject();
        }
        out.endArray();
    }

    @Override
    public List<Group> read(JsonReader in) throws IOException {

        List<Group> groups = new ArrayList<Group>();
        Map<String, List<String>> parentsMap = new HashMap<String, List<String>>();
        in.beginArray();
        while(in.peek() != JsonToken.END_ARRAY) {
            readGroup(in, groups, parentsMap);
        }
        in.endArray();

        convertGroups(groups, parentsMap);

        return groups;
    }

    private void convertGroups(List<Group> groups, Map<String, List<String>> parentsMap) throws IOException {
        for(Map.Entry<String, List<String>> entry : parentsMap.entrySet()) {
            Group group = getGroup(groups, entry.getKey());
            if(group.name.equals(entry.getKey())) {
                List<Group> parents = new ArrayList<Group>();
                for(String parentName : entry.getValue()) {
                    parents.add(getGroup(groups, parentName));
                    if(parentName.equals(entry.getKey())) {
                        throw new IOException("A group cannot be a parent to itself.");
                    }
                }
                group.parents.addAll(parents);
            }
        }
    }

    private void readGroup(JsonReader in, List<Group> groups, Map<String, List<String>> parentsMap) throws IOException {
        String name = null;
        Group.Type type = null;
        List<String> permissions = new ArrayList<String>();
        List<String> parents = new ArrayList<String>();
        List<Meta> metaList = new ArrayList<Meta>();

        in.beginObject();
        while(in.peek() != JsonToken.END_OBJECT) {
            String nextName = in.nextName();
            if ("name".equals(nextName)) {
                name = in.nextString();
            }

            if ("type".equals(nextName)) {
                type = Group.Type.valueOf(in.nextString().toUpperCase());
            }

            if("parents".equals(nextName)) {
                in.beginArray();
                while(in.peek() != JsonToken.END_ARRAY) {
                    parents.add(in.nextString());
                }
                in.endArray();
            }

            if ("permissions".equals(nextName)) {
                in.beginArray();
                while (in.peek() != JsonToken.END_ARRAY) {
                    permissions.add(in.nextString());
                }
                in.endArray();
            }

            if ("meta".equals(nextName)) {
                in.beginObject();
                while (in.peek() != JsonToken.END_OBJECT) {
                    metaList.add(new Meta(in.nextName(), in.nextInt()));
                }
                in.endObject();
            }
        }
        in.endObject();

        if(name == null) {
            throw new IOException("Name in a group was null in file GroupConfig");
        }
        Group group = new Group(name, permissions, null, type);
        group.metaContainer.add(metaList);
        groups.add(group);
        parentsMap.put(name, parents);
    }

    public Group getGroup(List<Group> groups, String name) {
        for(Group group : groups) {
            if(group.name.equals(name)) {
                return group;
            }
        }
        return null;
    }
}
