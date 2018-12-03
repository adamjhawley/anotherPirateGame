package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;
import uk.ac.york.sepr4.object.entity.NPCBoat;
import uk.ac.york.sepr4.screen.GameScreen;

import java.util.Optional;

@Data
public class BuildingManager {

    private Array<College> colleges;
    private Array<Department> departments;

    private GameScreen gameScreen;

    private float spawnDelta;

    public BuildingManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.spawnDelta = 0f;

        if(gameScreen.getPirateMap().isObjectsEnabled()) {
            this.colleges = new Array<>();
            this.departments = new Array<>();
            Json json = new Json();
            loadBuildings(json.fromJson(Array.class, College.class, Gdx.files.internal("colleges.json")));
            loadBuildings(json.fromJson(Array.class, Department.class, Gdx.files.internal("departments.json")));
        } else {
            Gdx.app.log("Building Manager", "Objects not enabled, not loading buildings!");
        }
    }

    public void spawnCollegeEnemies(float delta) {
        spawnDelta+=delta;
        if(spawnDelta >= 1f) {
            for (College college : this.colleges) {
                Optional<NPCBoat> optionalEnemy = college.spawnEnemy(gameScreen.getEntityManager().getNextEnemyID());
                if (optionalEnemy.isPresent()) {
                    Gdx.app.log("Building Manager", "Spawning an enemy at " + college.getName());
                    gameScreen.getEntityManager().addNPC(optionalEnemy.get());
                }
            }
            spawnDelta = 0f;
        }
    }

    private void loadBuildings(Array<Building> buildings) {
        for(Building building : buildings) {
            if (building.load(gameScreen.getPirateMap())) {
                if (building instanceof College) {
                    colleges.add((College) building);
                    Gdx.app.log("BuildingManager", "Loaded " + building.getName());
                } else {
                    Gdx.app.error("BuildingManager", "Failed to load " + building.getName());
                }
                if (building instanceof Department) {
                    departments.add((Department) building);
                    Gdx.app.log("BuildingManager", "Loaded " + building.getName());
                } else {
                    Gdx.app.error("BuildingManager", "Failed to load " + building.getName());

                }
            }

        }
    }
}
