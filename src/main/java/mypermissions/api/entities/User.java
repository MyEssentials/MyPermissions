package mypermissions.api.entities;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import myessentials.utils.PlayerUtils;
import mypermissions.MyPermissions;
import mypermissions.api.container.MetaContainer;
import mypermissions.api.container.PermissionsContainer;
import mypermissions.config.Config;
import mypermissions.manager.MyPermissionsManager;
import mypermissions.proxies.PermissionProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * A wrapper around the EntityPlayer with additional objects for permissions
 */
public class User {

    public Group group;

    public final UUID uuid;
    public final PermissionsContainer permsContainer = new PermissionsContainer();
    public final MetaContainer metaContainer = new MetaContainer();

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public User(UUID uuid, Group group) {
        this(uuid);
        this.group = group;
    }

    public boolean hasPermission(String permission) {
        PermissionLevel permLevel = permsContainer.hasPermission(permission);

        if (permLevel == PermissionLevel.ALLOWED) {
            return true;
        } else if (permLevel == PermissionLevel.DENIED) {
            return false;
        }

        permLevel = group.hasPermission(permission);

        return permLevel == PermissionLevel.ALLOWED || (Config.instance.fullAccessForOPS.get() && PlayerUtils.isOp(uuid));
    }

    public static class Serializer implements JsonSerializer<User>, JsonDeserializer<User> {

        @Override
        public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
            User user = new User(uuid);
            user.group = ((MyPermissionsManager)PermissionProxy.getPermissionManager()).groups.get(jsonObject.get("group").getAsString());
            if (jsonObject.has("permissions")) {
                user.permsContainer.addAll(ImmutableList.copyOf(context.<String[]>deserialize(jsonObject.get("permissions"), String[].class)));
            }
            if (jsonObject.has("meta")) {
                user.metaContainer.addAll(context.<MetaContainer>deserialize(jsonObject.get("meta"), MetaContainer.class));
            }

            return user;
        }

        @Override
        public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();

            json.addProperty("uuid", user.uuid.toString());
            json.add("group", context.serialize(user.group));
            if (!user.permsContainer.isEmpty()) {
                json.add("permissions", context.serialize(user.permsContainer));
            }
            if (!user.metaContainer.isEmpty()) {
                json.add("meta", context.serialize(user.metaContainer));
            }

            return json;
        }
    }
}
