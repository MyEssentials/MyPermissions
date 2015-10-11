package mypermissions.api.container;

import com.google.gson.*;
import mypermissions.api.entities.Meta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class MetaContainer extends ArrayList<Meta> {

    public Meta get(String permission) {
        for(Meta item : this) {
            if(item.permission.equals(permission)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Since Meta is represented by a "key":value format in Json it needs to stay in the Container rather than in the Meta class
     */
    public static class Serializer implements JsonSerializer<MetaContainer>, JsonDeserializer<MetaContainer> {
        @Override
        public MetaContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            MetaContainer container = new MetaContainer();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                container.add(new Meta(entry.getKey(), entry.getValue().getAsInt()));
            }

            return container;
        }

        @Override
        public JsonElement serialize(MetaContainer container, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            for (Meta meta : container) {
                jsonObject.addProperty(meta.permission, meta.metadata);
            }

            return jsonObject;
        }
    }
}
