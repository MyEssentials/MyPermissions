package mypermissions.api.entities;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;

import myessentials.json.SerializerTemplate;
import myessentials.utils.ColorUtils;
import mypermissions.api.container.PermissionsContainer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A set of permissions that is assigned to players.
 * Each player can only have one group assigned to.
 * Groups have a hierarchy.
 */
public class Group {

    private String name;

    public final PermissionsContainer permsContainer = new PermissionsContainer();
    public final Meta.Container metaContainer = new Meta.Container();
    public final Container parents = new Container();

    public Group() {
        this.name = "default";
        this.permsContainer.add("cmd.*");
        this.permsContainer.add("mytown.cmd");
        this.permsContainer.add("mytown.cmd.outsider.*");
    }

    public Group(String name) {
        this.name = name;
    }

    public PermissionLevel hasPermission(String permission) {
        PermissionLevel permLevel = permsContainer.hasPermission(permission);

        if (permLevel == PermissionLevel.DENIED || permLevel == PermissionLevel.ALLOWED) {
            return permLevel;
        }

        // If nothing was found search the inherited permissions

        for (Group parent : parents) {
            permLevel = parent.hasPermission(permission);
            if (permLevel == PermissionLevel.DENIED || permLevel == PermissionLevel.ALLOWED) {
                return permLevel;
            }
        }

        return permLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Serializer extends SerializerTemplate<Group> {

        @Override
        public void register(GsonBuilder builder) {
            builder.registerTypeAdapter(Group.class, this);
            new Meta.Container.Serializer().register(builder);
        }

        @Override
        public Group deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            Group group = new Group(name);
            if (jsonObject.has("permissions")) {
                group.permsContainer.addAll(ImmutableList.copyOf(context.<String[]>deserialize(jsonObject.get("permissions"), String[].class)));
            }
            if (jsonObject.has("meta")) {
                group.metaContainer.addAll(context.<Meta.Container>deserialize(jsonObject.get("meta"), Meta.Container.class));
            }
            return group;
        }

        @Override
        public JsonElement serialize(Group group, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("name", group.getName());
            json.add("permissions", context.serialize(group.permsContainer));
            json.add("meta", context.serialize(group.metaContainer));
            return json;
        }
    }

    public static class Container extends ArrayList<Group> {

        public void remove(String groupName) {
            for (Iterator<Group> it = iterator(); it.hasNext();) {
                Group group = it.next();
                if (group.getName().equals(groupName)) {
                    it.remove();
                    return;
                }
            }
        }

        public boolean contains(String groupName) {
            for (Group group : this) {
                if (group.getName().equals(groupName))
                    return true;
            }
            return false;
        }

        public Group get(String groupName) {
            for (Group group : this) {
                if (group.getName().equals(groupName))
                    return group;
            }
            return null;
        }

        @Override
        public String toString() {
            String formattedList = "";
            for (Group group : this) {
                String parents = "";
                for (Group parent : group.parents) {
                    if (parents.equals("")) {
                        parents += parent.getName();
                    } else {
                        parents += ", " +  parent.getName();
                    }
                }

                String formattedGroup = group.getName() + ColorUtils.colorComma + " ( " + ColorUtils.colorGroupText + (group.parents.isEmpty() ? "" : ColorUtils.colorComma + " | " + ColorUtils.colorGroupText + "P: " + ColorUtils.colorGroupParents + parents) + ColorUtils.colorComma + " )";
                if (formattedList.equals("")) {
                    formattedList += formattedGroup;
                } else {
                    formattedList += "\\n" + formattedGroup;
                }
            }
            return formattedList;
        }
    }
}
