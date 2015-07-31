package mypermissions.manager;

import com.google.common.collect.ImmutableList;
import myessentials.utils.PlayerUtils;
import mypermissions.api.IPermissionManager;
import mypermissions.entities.Group;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class MyPermissionsManager implements IPermissionManager {

    private List<Group> groups = new ArrayList<Group>();
    private Map<EntityPlayer, Group> playerGroup = new HashMap<EntityPlayer, Group>();

    public MyPermissionsManager() {
    }

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        EntityPlayer player = PlayerUtils.getPlayerFromUUID(uuid);
        Group group = getPlayerGroup(player);
        return group.hasPermission(permission);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }

    public void removeGroup(String groupName) {
        for(Iterator<Group> it = groups.iterator(); it.hasNext();) {
            Group group = it.next();
            if(group.getName().equals(groupName)) {
                it.remove();
                return;
            }
        }
    }

    public boolean hasGroup(Group group) {
        return groups.contains(group);
    }

    public boolean hasGroup(String groupName) {
        for (Group group : groups) {
            if (group.getName().equals(groupName))
                return true;
        }
        return false;
    }

    public Group getGroup(String groupName) {
        for(Group group : groups) {
            if(group.getName().equals(groupName))
                return group;
        }
        return null;
    }

    public ImmutableList<Group> getGroups() {
        return ImmutableList.copyOf(groups);
    }

    public Group getPlayerGroup(EntityPlayer player) {
        return playerGroup.get(player);
    }

    public void linkPlayer(EntityPlayer player, Group group) {
        playerGroup.put(player, group);
    }

    public void unlinkPlayer(EntityPlayer player) {
        playerGroup.remove(player);
    }
}
