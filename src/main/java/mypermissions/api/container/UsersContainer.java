package mypermissions.api.container;

import mypermissions.api.entities.Group;
import mypermissions.api.entities.User;

import java.util.ArrayList;
import java.util.UUID;

public class UsersContainer extends ArrayList<User> {

    private Group defaultGroup;

    public User get(UUID uuid) {
        for(User user : this) {
            if(user.uuid.equals(uuid)) {
                return user;
            }
        }

        User newUser = new User(uuid, defaultGroup);
        add(newUser);
        return newUser;
    }

    public Group getPlayerGroup(UUID uuid) {

        for(User user : this) {
            if(user.uuid.equals(uuid)) {
                return user.getGroup();
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
}
