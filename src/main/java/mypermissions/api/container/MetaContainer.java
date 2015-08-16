package mypermissions.api.container;

import mypermissions.api.entities.Meta;

import java.util.ArrayList;

public class MetaContainer extends ArrayList<Meta> {

    public Meta get(String permission) {
        for(Meta item : this) {
            if(item.permission.equals(permission)) {
                return item;
            }
        }
        return null;
    }

}
