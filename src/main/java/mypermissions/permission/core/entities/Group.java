package mypermissions.permission.core.entities;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;

import myessentials.chat.api.ChatComponentFormatted;
import myessentials.chat.api.ChatFormat;
import myessentials.chat.api.IChatFormat;
import myessentials.json.api.SerializerTemplate;
import myessentials.utils.ColorUtils;
import mypermissions.MyPermissions;
import mypermissions.permission.core.container.PermissionsContainer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A set of permissions that is assigned to players.
 * Each player can only have one group assigned to.
 * Groups have a hierarchy.
 */
public class Group extends ChatFormat {

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

    @Override
    public IChatComponent toChatMessage(boolean shortened) {

        ChatComponentText parents = new ChatComponentText("");
        for (Group parent : this.parents) {
            IChatComponent parentComponent = MyPermissions.instance.LOCAL.getLocalization("mypermissions.format.group.parent", new ChatComponentText(parent.getName()));
            if (parents.getSiblings().size() == 0) {
                parents.appendSibling(new ChatComponentText(parent.getName()).setChatStyle(ColorUtils.styleGroupParents));
            } else {
                parents.appendSibling(new ChatComponentText(", ").setChatStyle(ColorUtils.styleComma))
                       .appendSibling(new ChatComponentText(parent.getName()).setChatStyle(ColorUtils.styleGroupParents));
            }
        }

        return new ChatComponentText("NOT YET IMPLEMENTED");
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

    public static class Container extends ArrayList<Group> implements IChatFormat {

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
        public IChatComponent toChatMessage(boolean shortened) {

            ChatComponentText message = new ChatComponentText("Groups \n");

            for (Group group : this) {
                message.appendSibling(group.toChatMessage());
                message.appendSibling(new ChatComponentText("\n"));
            }

            return message;
        }

        @Override
        public IChatComponent toChatMessage() {
            return toChatMessage(false);
        }
    }
}
