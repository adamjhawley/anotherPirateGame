package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;
import uk.ac.york.sepr4.object.entity.NPCBoat;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.screen.GameScreen;

import java.util.Optional;

@Data
public class BuildingManager {

    private Array<College> colleges = new Array<>();
    private Array<Department> departments = new Array<>();

    private GameScreen gameScreen;

    //time till next spawn attempt
    private float spawnDelta;

    /***
     * This class handles instances of buildings (Colleges and Departments)
     *
     * It is responsible for loading from file and making sure the map object relating to this building is present.
     * There is a method which arranges spawning of college enemies.
     * @param gameScreen
     */
    public BuildingManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.spawnDelta = 0f;

        if(gameScreen.getPirateMap().isObjectsEnabled()) {
            Json json = new Json();
            Gdx.app.debug("BuildingManager", "Loading Buildings");
            loadColleges(json.fromJson(Array.class, College.class, Gdx.files.internal("colleges.json")));
            loadDepartments(json.fromJson(Array.class, Department.class, Gdx.files.internal("departments.json")));
        } else {
            Gdx.app.log("Building Manager", "Objects not enabled, not loading buildings!");
        }
    }

    //TODO: Could have a cooldown here
    public void checkBossSpawn() {
        for(College college : colleges) {
            if(!college.isBossSpawned()) {
                Player player = gameScreen.getEntityManager().getOrCreatePlayer();
                if (college.getBuildingZone().contains(player.getRectBounds())) {
                    Gdx.app.debug("BuildingManager", "Player entered college zone: " + college.getName());
                    NPCBoat npcBoss = college.spawnBoss();
                    college.setBossSpawned(true);
                    gameScreen.getEntityManager().addNPC(npcBoss);
                }
            }
        }
    }

    public void spawnCollegeEnemies(float delta) {
        spawnDelta+=delta;
        if(spawnDelta >= 1f) {
            for (College college : this.colleges) {
                //check how many entities already exist in college zone (dont spawn too many)
                if(gameScreen.getEntityManager().getLivingEntitiesInArea(college.getBuildingZone()).size
                        < college.getMaxEntities()) {
                    Optional<NPCBoat> optionalEnemy = college.generateCollegeNPC();
                    if (optionalEnemy.isPresent()) {
                        NPCBoat npcBoat = optionalEnemy.get();
                        //checks if spawn spot is valid
                        //TODO: instead of cancelling spawn, get a better spot
                        if(!gameScreen.getPirateMap().isColliding(npcBoat.getCentre())) {
                            Gdx.app.debug("Building Manager", "Spawning an enemy at " + college.getName());
                            gameScreen.getEntityManager().addNPC(optionalEnemy.get());
                        } else {
                            Gdx.app.debug("BuildingManager", "Didnt spawn enemy in collision spot!");

                        }
                    }
                } else {
                    //Gdx.app.debug("BuildingManager", "Max entities @ "+college.getName());
                }
            }
            spawnDelta = 0f;
        }
    }


    //TODO: Make generic method
    private void loadColleges(Array<College> loading) {
        for(College college : loading) {
            if (college.load(gameScreen.getPirateMap())) {
                    colleges.add(college);
                    Gdx.app.debug("BuildingManager", "Loaded " + college.getName());
                } else {
                    Gdx.app.error("BuildingManager", "Failed to load " + college.getName());
                }

        }
    }

    private void loadDepartments(Array<Department> loading) {
        for(Department department : loading) {
            if (department.load(gameScreen.getPirateMap())) {
                    departments.add(department);
                    Gdx.app.debug("BuildingManager", "Loaded " + department.getName());
                } else {
                    Gdx.app.error("BuildingManager", "Failed to load " + department.getName());

                }

        }
    }
}
