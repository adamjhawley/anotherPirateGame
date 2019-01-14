package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.object.entity.NPCBoat;
import uk.ac.york.sepr4.object.entity.NPCBuilder;
import uk.ac.york.sepr4.object.projectile.ProjectileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Data
public class College extends Building {

    private NPCBoat boss;
    private float spawnRange;
    private Double spawnChance, enemyDifficulty;
    private Integer maxEntities = 5;

    private List<String> projectileTypesStr;
    private List<ProjectileType> projectileTypes;

    private List<String> requiresCollegeStr = new ArrayList<>();
    private List<College> requiresCollege;

    public College() {
        // Empty constructor for JSON DAO.
    }

    /***
     * 
     * @param id
     * @return
     */
    public Optional<NPCBoat> spawnCollegeNPC(Integer id) {
        Random random = new Random();
        if(random.nextDouble() <= spawnChance){
            NPCBoat boat = new NPCBuilder().generateRandomEnemy(id, getRandomSpawnVector(), enemyDifficulty, projectileTypes);
            boat.setAllied(this);
            return Optional.of(boat);
        }
        return Optional.empty();
    }

    private Vector2 getRandomSpawnVector() {
        //TODO: Very unsure if this works / is the best (or even good) way to do this.
        Rectangle rectangle = getMapObject().getRectangle();
        Random random = new Random();

        //top left
        Vector2 minVector = new Vector2(rectangle.x-(rectangle.width/2), rectangle.y+(rectangle.height/2));
        //bottom right
        Vector2 maxVector = new Vector2(rectangle.x+(rectangle.width/2), rectangle.y-(rectangle.height/2));

        return new Vector2((maxVector.x-minVector.x)*random.nextFloat()+minVector.x,
                (maxVector.y-minVector.y)*random.nextFloat()+minVector.y);
    }

}
