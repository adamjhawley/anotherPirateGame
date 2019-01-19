package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.object.PirateMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class College extends Building {

    //Must be defined in file
    private String bossName;
    private Double bossDifficulty;

    //Can be set optionally in file to change college parameters
    private float spawnRange = 500f;
    private Double spawnChance = 0.1, enemyDifficulty = 0.1;
    private Integer maxEntities = 2;
    private List<String> requiresCollegeStr = new ArrayList<>(); //yet to be implemented (questing)

    //Set from college file variables
    private List<College> requiresCollege; //yet to be implemented (questing)
    private boolean bossSpawned = false;


    public College() {
        // Empty constructor for JSON DAO.
    }

    public boolean load(PirateMap pirateMap){
        Gdx.app.debug("College", "Load "+getName());
        if(super.load(pirateMap)) {
            //map object exists and has been loaded
            if(checkBoss()) {
                return true;
            } else {
                Gdx.app.debug("College", "Boss details not present!");
            }
        }
        return false;
    }

    /***
     * Check if the boss specified by file is defined and can be created.
     * @return
     */
    private boolean checkBoss() {
        return(bossName != null && bossDifficulty != null);
    }


    public Rectangle getCollegeSpawnZone() {
        Vector2 pos = getMapLocation();
        return new Rectangle(pos.x-(spawnRange/2), pos.y-(spawnRange/2), spawnRange, spawnRange);
    }

    public Vector2 getRandomSpawnVector() {
        //TODO: Very unsure if this works / is the best (or even good) way to do this.
        Random random = new Random();
        Rectangle rectangle = getCollegeSpawnZone();
        float randX = rectangle.x+(random.nextFloat() * (rectangle.width));
        float randY = rectangle.y+(random.nextFloat() * (rectangle.height));
        Gdx.app.debug("RandSpawn", "x:"+randX+", y:"+randY);
        Gdx.app.debug("RandSpawn", "w:"+rectangle.width+", h:"+rectangle.height);

        return new Vector2(randX, randY);
    }

}
