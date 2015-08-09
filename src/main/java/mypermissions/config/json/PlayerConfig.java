package mypermissions.config.json;


import com.google.common.reflect.TypeToken;
import myessentials.MyEssentialsCore;
import myessentials.json.JSONConfig;
import myessentials.utils.PlayerUtils;
import mypermissions.entities.Group;
import mypermissions.manager.MyPermissionsManager;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class PlayerConfig extends JSONConfig<PlayerConfig.Wrapper> {

    private MyPermissionsManager permissionsManager;

    public PlayerConfig(String path, MyPermissionsManager permissionsManager) {
        super(path, "PlayerConfig");
        this.permissionsManager = permissionsManager;
        this.gsonType = new TypeToken<List<Wrapper>>() {}.getType();
    }

    @Override
    protected List<Wrapper> read() {
        List<Wrapper> items = super.read();

        for(Wrapper item : items) {
            EntityPlayer player = PlayerUtils.getPlayerFromUUID(UUID.fromString(item.uuid));
            Group group = permissionsManager.getGroup(item.groupName);
            permissionsManager.linkPlayer(player, group);
        }

        return items;
    }

    @Override
    protected boolean validate(List<Wrapper> items) {
        boolean isValid = true;

        for(Iterator<Wrapper> it = items.iterator(); it.hasNext(); ) {
            Wrapper item = it.next();
            Group group = permissionsManager.getGroup(item.groupName);

            if(group == null) {
                MyEssentialsCore.instance.LOG.error("Group with name " + item.groupName + " does not exist! Removing...");
                it.remove();
                isValid = false;
            }
        }

        return isValid;
    }

    public List<Wrapper> convert(Map<EntityPlayer, Group> playerGroup) {
        List<Wrapper> wrapperList = new ArrayList<Wrapper>();

        for(Map.Entry<EntityPlayer, Group> entry : playerGroup.entrySet()) {
            wrapperList.add(new Wrapper(entry.getKey().getUniqueID().toString(), entry.getValue().getName()));
        }

        return wrapperList;
    }

    public class Wrapper {
        public String uuid;
        public String groupName;

        public Wrapper(String uuid, String groupName) {
            this.uuid = uuid;
            this.groupName = groupName;
        }
    }
}

