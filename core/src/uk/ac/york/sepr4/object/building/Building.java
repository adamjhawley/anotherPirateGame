package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import lombok.Data;
import uk.ac.york.sepr4.object.PirateMap;

import java.util.Optional;

@Data
public abstract class Building {

    private String name;
    private String mapObjectStr;
    private RectangleMapObject mapObject;

    public boolean load(PirateMap pirateMap) {
        Optional<RectangleMapObject> objectOptional = pirateMap.getRectObject(mapObjectStr);
        if(objectOptional.isPresent()) {
            this.mapObject = objectOptional.get();
            return true;
        }
        return false;
    }
}
