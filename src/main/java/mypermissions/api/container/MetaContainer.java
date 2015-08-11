package mypermissions.api.container;


import myessentials.entities.Container;
import mypermissions.api.entities.Meta;

public class MetaContainer extends Container<Meta> {

    public Meta get(String permission) {
        for(Meta item : items) {
            if(item.permission.equals(permission)) {
                return item;
            }
        }
        return null;
    }

}
