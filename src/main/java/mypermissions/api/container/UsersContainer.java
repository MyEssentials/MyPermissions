package mypermissions.api.container;

import myessentials.utils.ColorUtils;
import myessentials.utils.PlayerUtils;
import mypermissions.api.entities.Group;
import mypermissions.api.entities.User;

import java.util.ArrayList;
import java.util.UUID;

public class UsersContainer extends ArrayList<User> {

    private Group defaultGroup;

    public boolean add(UUID uuid) {
        if(get(uuid) == null) {
            User newUser = new User(uuid, defaultGroup);
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
