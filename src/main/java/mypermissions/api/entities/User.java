package mypermissions.api.entities;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;

import myessentials.json.SerializerTemplate;
import myessentials.utils.ColorUtils;
import myessentials.utils.PlayerUtils;
import mypermissions.api.container.PermissionsContainer;
import mypermissions.config.Config;
import mypermissions.manager.MyPermissionsManager;
import mypermissions.proxies.PermissionProxy;
import net.minecraftforge.common.UsernameCache;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A wrapper around the EntityPlayer with additional objects for permissions
 */
public class User {

    public Group group;

    public final UUID uuid;
    public String lastPlayerName = null;
    public final PermissionsContainer permsContainer = new PermissionsContainer();
    public final Meta.Container metaContainer = new Meta.Container();

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

        return (group != null && group.hasPermission(permission) == PermissionLevel.ALLOWED) ||
               (Config.instance.fullAccessForOPS.get() && PlayerUtils.isOp(uuid));
    }

    public static class Serializer extends SerializerTemplate<User> {

        @Override
        public void register(GsonBuilder builder) {
            builder.registerTypeAdapter(User.class, this);
            new Meta.Container.Serializer().register(builder);
        }

        @Override
        public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
            User user = new User(uuid);
            JsonElement lastPlayerName = jsonObject.get("player");
            if (lastPlayerName != null) {
                user.lastPlayerName = lastPlayerName.getAsString();
            }
            JsonElement group = jsonObject.get("group");
            if (group != null) {
                user.group = ((MyPermissionsManager)PermissionProxy.getPermissionManager()).groups.get(group.getAsString());
            }
            if (jsonObject.has("permissions")) {
                user.permsContainer.addAll(ImmutableList.copyOf(context.<String[]>deserialize(jsonObject.get("permissions"), String[].class)));
            }
            if (jsonObject.has("meta")) {
                user.metaContainer.addAll(context.<Meta.Container>deserialize(jsonObject.get("meta"), Meta.Container.class));
            }

            return user;
        }

        @Override
        public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();

            json.addProperty("uuid", user.uuid.toString());
            if (user.lastPlayerName != null) {
                json.addProperty("player", user.lastPlayerName);
            }
            json.addProperty("group", user.group.getName());
            if (!user.permsContainer.isEmpty()) {
                json.add("permissions", context.serialize(user.permsContainer));
            }
            if (!user.metaContainer.isEmpty()) {
                json.add("meta", context.serialize(user.metaContainer));
            }

            return json;
        }
    }

    public static class Container extends ArrayList<User> {

        private Group defaultGroup;

        public boolean add(UUID uuid) {
            if(get(uuid) == null) {
                Group group = (defaultGroup == null) ? ((MyPermissionsManager)PermissionProxy.getPermissionManager()).groups.get("default") : defaultGroup;
                User newUser = new User(uuid, group);
                add(newUser);
                return true;
            }
            return false;
        }

        public User get(UUID uuid) {
            for(User user : this) {
                if(user.uuid.equals(uuid)) {
                    return user;
                }
            }
            return null;
        }

        public Group getPlayerGroup(UUID uuid) {

            for(User user : this) {
                if(user.uuid.equals(uuid)) {
                    return user.group;
                }
            }

            User user = new User(uuid, defaultGroup);
            add(user);
            return defaultGroup;
        }

        public boolean contains(UUID uuid) {
            for(User user : this) {
                if(user.uuid.equals(uuid)) {
                    return true;
                }
            }
            return false;
        }

        public boolean updateLastPlayerName(UUID uuid) {
            User user = get(uuid);
            String currentPlayerName = UsernameCache.getLastKnownUsername(uuid);

            if (user != null && currentPlayerName != null && !currentPlayerName.equals(user.lastPlayerName)) {
                user.lastPlayerName = currentPlayerName;
                return true;
            }

            return false;
        }

        public Group getDefaultGroup() {
            return defaultGroup;
        }

        public void setDefaultGroup(Group defaultGroup) {
            this.defaultGroup = defaultGroup;
        }

        @Override
        public String toString() {
            String formattedList = "";

            for(User user : this) {
                String toAdd = String.format(ColorUtils.colorPlayer + "%s" + ColorUtils.colorComma + " {" + ColorUtils.colorGroupText + ColorUtils.colorComma + "}", PlayerUtils.getUsernameFromUUID(user.uuid));
                if(formattedList.equals("")) {
                    formattedList += toAdd;
                } else {
                    formattedList += "\\n" + toAdd;
                }
            }

            return formattedList;
        }
    }
}
