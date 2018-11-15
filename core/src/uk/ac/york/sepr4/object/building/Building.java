package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import lombok.Data;

@Data
public abstract class Building {

    private String name;
    private String objectName;
    private RectangleMapObject mapObject;

    public boolean load(MapObjects objects) {
        for(MapObject mapObject : objects) {
            if(mapObject.getName().equalsIgnoreCase(objectName)){
                if(mapObject instanceof RectangleMapObject) {
                    this.mapObject = (RectangleMapObject)mapObject;
                    return true;
                }
            }
        }
        return false;
    }
}
