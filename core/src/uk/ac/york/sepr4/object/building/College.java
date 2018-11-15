package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.sepr4.object.entity.Enemy;
import uk.ac.york.sepr4.object.entity.EnemyBuilder;
import uk.ac.york.sepr4.object.projectile.ProjectileType;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class College extends Building {

    private boolean captured;
    private Enemy boss;
    private float spawnRange;
    private Double spawnChance, enemyDifficulty;
    private Integer maxEntities;

    private List<String> projectileTypesStr;
    private List<ProjectileType> projectileTypes;

    private List<String> requiresCollegeStr;
    private List<College> requiresCollege;

    public College() {
        // Empty constructor for JSON DAO.
    }

    public Optional<Enemy> spawnEnemy(Integer id) {
        Random random = new Random();
        if(random.nextDouble() <= spawnChance){
            return Optional.of(new EnemyBuilder().generateRandomEnemy(id, getRandomSpawnVector(), enemyDifficulty, projectileTypes));
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
