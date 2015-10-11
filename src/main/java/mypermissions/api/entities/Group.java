package mypermissions.api.entities;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import mypermissions.api.container.GroupsContainer;
import mypermissions.api.container.MetaContainer;
import mypermissions.api.container.PermissionsContainer;
import scala.actors.threadpool.Arrays;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A set of permissions that is assigned to players.
 * Each player can only have one group assigned to.
 * Groups have a hierarchy.
 */
public class Group {

    private String name;

    public final PermissionsContainer permsContainer = new PermissionsContainer();
    public final MetaContainer metaContainer = new MetaContainer();
    public final GroupsContainer parents = new GroupsContainer();

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

    public static class Serializer implements JsonSerializer<Group>, JsonDeserializer<Group> {

        @Override
        public Group deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            Group group = new Group(name);
            if (jsonObject.has("permissions")) {
                group.permsContainer.addAll(ImmutableList.copyOf(context.<String[]>deserialize(jsonObject.get("permissions"), String[].class)));
            }
            if (jsonObject.has("meta")) {
                group.metaContainer.addAll(context.<MetaContainer>deserialize(jsonObject.get("meta"), MetaContainer.class));
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
}
