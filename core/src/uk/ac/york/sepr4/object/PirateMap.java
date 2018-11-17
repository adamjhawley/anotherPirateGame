package uk.ac.york.sepr4.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

import java.util.Optional;

public class PirateMap {

    @Getter
    private TiledMap tiledMap;

    private final String objectLayerName = "objects";
    private final String spawnPointObject = "spawn";

    private MapLayer objectLayer;
    @Getter
    private Vector2 spawnPoint;

    @Getter
    private boolean objectsEnabled;

    public PirateMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        if(checkObjectLayer()) {
            checkSpawnObject();
            this.objectsEnabled = true;
        } else {
            Gdx.app.log("Pirate Map", "Map does NOT contain object layer!");
            this.objectsEnabled = false;
        }


    }

    private boolean checkObjectLayer() {
        this.objectLayer = tiledMap.getLayers().get(objectLayerName);
        return (this.objectLayer != null);
    }

    public Optional<RectangleMapObject> getRectObject(String objectName) {
        MapObject mapObject = objectLayer.getObjects().get(objectName);
        if(mapObject instanceof RectangleMapObject) {
            return Optional.of((RectangleMapObject) mapObject);
        }
        return Optional.empty();
    }

    private boolean checkSpawnObject() {
        MapObject mapObject = objectLayer.getObjects().get(spawnPointObject);
        if(mapObject instanceof RectangleMapObject) {
            RectangleMapObject object = (RectangleMapObject) mapObject;
            this.spawnPoint = new Vector2(object.getRectangle().x, object.getRectangle().y);
            return true;
        }
        return false;
    }

}
