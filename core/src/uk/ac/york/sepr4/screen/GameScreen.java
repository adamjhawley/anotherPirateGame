package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Getter;
import uk.ac.york.sepr4.PirateGame;
import uk.ac.york.sepr4.object.PirateMap;
import uk.ac.york.sepr4.object.building.BuildingManager;
import uk.ac.york.sepr4.object.quest.QuestManager;
import uk.ac.york.sepr4.screen.hud.HUD;
import uk.ac.york.sepr4.screen.hud.HealthBar;
import uk.ac.york.sepr4.object.entity.*;
import uk.ac.york.sepr4.object.item.ItemManager;
import uk.ac.york.sepr4.object.projectile.Projectile;
import uk.ac.york.sepr4.object.projectile.ProjectileManager;

/**
 * GameScreen is main game class. Holds data related to current player including the
 * {@link uk.ac.york.sepr4.object.building.BuildingManager}, {@link uk.ac.york.sepr4.object.item.ItemManager},
 * {@link uk.ac.york.sepr4.object.quest.QuestManager} and {@link uk.ac.york.sepr4.object.entity.EntityManager}
 *
 * Responds to keyboard and mouse input by the player. InputMultiplexer used to combine input processing in both
 * this class (mouse clicks) and {@link uk.ac.york.sepr4.object.entity.Player} class (key press).
 */
public class GameScreen implements Screen, InputProcessor {

    private PirateGame pirateGame;
    private Stage stage;
    private Stage hudStage;
    private SpriteBatch batch;

    @Getter
    private OrthographicCamera orthographicCamera;

    @Getter
    PirateMap pirateMap;
    TiledMapRenderer tiledMapRenderer;

    private ItemManager itemManager;
    @Getter
    private EntityManager entityManager;
    @Getter
    private ProjectileManager projectileManager;
    @Getter
    private QuestManager questManager;
    private BuildingManager buildingManager;

    private InputMultiplexer inputMultiplexer;

    private HUD hud;

    private static GameScreen gameScreen;

    public static GameScreen getInstance() {
        return gameScreen;
    }

    /**
     * GameScreen Constructor
     *
     * Sets base game parameters, sets up camera, map, view port and stage(s).
     * Initializes Item, Entity, Building and Quest managers and InputMultiplexer.
     *
     * Adds the player as an actor to the stage.
     *
     * @param pirateGame
     */
    public GameScreen(PirateGame pirateGame) {
        // Local widths and heights.
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        this.pirateGame = pirateGame;
        gameScreen = this;

        // Set up camera.
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, w, h);
        orthographicCamera.update();

        // Set up stages (for entities and HUD).
        StretchViewport stretchViewport = new StretchViewport(w, h, orthographicCamera);
        batch = new SpriteBatch();
        stage = new Stage(stretchViewport, batch);
        hudStage = new Stage(new FitViewport(w,h, new OrthographicCamera()));

        // Locate and set up tile map.
        pirateMap = new PirateMap(new TmxMapLoader().load("PirateMap/PirateMap.tmx"));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(pirateMap.getTiledMap(), 1 / 2f);


        this.itemManager = new ItemManager();
        this.entityManager = new EntityManager(this);
        this.projectileManager = new ProjectileManager(entityManager);
        this.questManager = new QuestManager(entityManager);
        this.buildingManager = new BuildingManager(this);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(entityManager.getOrCreatePlayer());

        this.hud = new HUD(this);
        hudStage.addActor(this.hud.getTable());

        Gdx.input.setInputProcessor(inputMultiplexer);

        startGame();

    }

    private void startGame(){
        stage.addActor(entityManager.getOrCreatePlayer());
        Vector2 vector2 = getPirateMap().getSpawnPoint();
        NPCBoat enemy = new NPCBuilder()
                .selectedProjectile(projectileManager.getDefaultWeaponType())
                .buildNPC(entityManager.getNextEnemyID(), new Vector2(vector2.x+200f, vector2.y+200f));
        entityManager.addNPC(enemy);
    }

    /**
     * Called when screen becomes active (is switched to).
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /**
     * Method responsible for rendering the GameScreen on each frame. This clears the screen, updates the map and
     * visible entities, then calls the stage act. This causes actors (entities) on the stage to move (act).
     *
     * @param delta Time between last and current frame.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Player player = entityManager.getOrCreatePlayer();
        if(player.isDead()) {
            pirateGame.switchScreen(ScreenType.MENU);
            return;
        }

        entityManager.handleStageEntities(stage);

        if(pirateMap.isObjectsEnabled()) {
            buildingManager.spawnCollegeEnemies(delta);
        }

        handleHealthBars();

        this.hud.update();

        checkCollisions();

        orthographicCamera.position.set(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f, 0);
        orthographicCamera.update();
        batch.setProjectionMatrix(orthographicCamera.combined);
        tiledMapRenderer.setView(orthographicCamera);
        tiledMapRenderer.render();

        stage.act(delta);
        stage.draw();
        hudStage.act();
        hudStage.draw();

    }

    /**
     * Handles HealthBar elements for damaged actors.
     */
    private void handleHealthBars() {
        Player player = entityManager.getOrCreatePlayer();
        if (player.getHealth() < player.getMaxHealth()) {
            if (!stage.getActors().contains(player.getHealthBar(), true)) {
                stage.addActor(player.getHealthBar());
            }
        }

        for (NPCBoat NPCBoat : entityManager.getEnemyList()) {
            if (NPCBoat.getHealth() < NPCBoat.getMaxHealth()) {
                if (!stage.getActors().contains(NPCBoat.getHealthBar(), true)) {
                    stage.addActor(NPCBoat.getHealthBar());
                }
            }
        }
        Array<Actor> toRemove = new Array<Actor>();
        for (Actor actors : stage.getActors()) {
            if (actors instanceof HealthBar) {
                HealthBar healthBar = (HealthBar) actors;
                LivingEntity livingEntity = healthBar.getLivingEntity();
                if (livingEntity.getHealth() == livingEntity.getMaxHealth() || livingEntity.isDead() || livingEntity.isDying()) {
                    toRemove.add(actors);
                }
            }
        }
        stage.getActors().removeAll(toRemove, true);

    }

    /**
     * Checks whether actors have overlapped. In the instance where projectile and entity overlap, deal damage.
     */
    private void checkCollisions() {
        checkProjectileCollisions();
        checkLivingEntityCollisions();
    }

    public void checkLivingEntityCollisions() {
        Player player = getEntityManager().getOrCreatePlayer();
        for(NPCBoat NPCBoat :getEntityManager().getEnemyList()) {
            if(NPCBoat.getRectBounds().overlaps(player.getRectBounds())) {
                //Double actingMom = NPCBoat.getSpeed() * Math.acos(NPCBoat.getAngleTowardsLE(player));
                //player.setAngle(player.getAngle()+(float)Math.acos(player.getSpeed()/NPCBoat.getSpeed()));
                //NPCBoat.setSpeed(NPCBoat.getSpeed()/2);
            }
        }
    }

    private void checkProjectileCollisions() {
        Player player = entityManager.getOrCreatePlayer();
        for (Projectile projectile : projectileManager.getProjectileList()) {
            if (projectile.getShooter() != player && projectile.getRectBounds().overlaps(player.getRectBounds())) {
                //if bullet overlaps player and shooter not player
                if (!player.damage(projectile.getProjectileType().getDamage())) {
                    //is dead
                    Gdx.app.log("Test Log", "Player died. ");
                } else {
                    Gdx.app.log("Test Log", "Player damaged by projectile. ");
                }
                //kill projectile
                projectile.setActive(false);
            }
        }

        for (NPCBoat NPCBoat : entityManager.getEnemyList()) {
            for (Projectile projectile : projectileManager.getProjectileList()) {
                if (projectile.getShooter() != NPCBoat && projectile.getRectBounds().overlaps(NPCBoat.getRectBounds())) {
                    //if bullet overlaps player and shooter not player
                    if (!NPCBoat.damage(projectile.getProjectileType().getDamage())) {
                        //is dead
                        Gdx.app.log("Test Log", "NPCBoat died. ");
                    }
                    Gdx.app.log("Test Log", "NPCBoat damaged by projectile. ");
                    //kill projectile
                    projectile.setActive(false);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        //TODO: Method exists more suited for this? (this works fine though)
        orthographicCamera.setToOrtho(false, (float) width, (float) height);
        orthographicCamera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Player player = entityManager.getOrCreatePlayer();
            Vector3 clickLoc = orthographicCamera.unproject(new Vector3(screenX, screenY, 0));
            float fireAngle = (float) (-Math.atan2(player.getCentre().x - clickLoc.x, player.getCentre().y - clickLoc.y));
            Gdx.app.log("Test Log", "Firing: Click at (rad) " + fireAngle);
            if(!player.fire(fireAngle)) {
                Gdx.app.log("Test Log", "Error firing!");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
