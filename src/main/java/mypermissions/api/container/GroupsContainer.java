package mypermissions.api.container;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import myessentials.utils.ColorUtils;
import mypermissions.api.entities.Group;
import mypermissions.api.entities.Meta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GroupsContainer extends ArrayList<Group> {

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
