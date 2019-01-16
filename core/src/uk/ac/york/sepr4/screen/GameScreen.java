package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
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
 * <p>
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
    private QuestManager questManager;
    private BuildingManager buildingManager;

    private InputMultiplexer inputMultiplexer;

    private HUD hud;

    private static GameScreen gameScreen;

    private ShapeRenderer shapeRenderer;

    private static final boolean DEBUG = true;

    public static GameScreen getInstance() {
        return gameScreen;
    }

    /**
     * GameScreen Constructor
     * <p>
     * Sets base game parameters, sets up camera, map, view port and stage(s).
     * Initializes Item, Entity, Building and Quest managers and InputMultiplexer.
     * <p>
     * Adds the player as an actor to the stage.
     *
     * @param pirateGame
     */
    public GameScreen(PirateGame pirateGame) {
        this.pirateGame = pirateGame;
        gameScreen = this;

        // Debug options (extra logging, collision shape renderer (viewing tile object map))
        if(DEBUG) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            shapeRenderer = new ShapeRenderer();
        }

        // Local widths and heights.
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Set up camera.
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, w, h);
        orthographicCamera.update();

        // Set up stages (for entities and HUD).
        StretchViewport stretchViewport = new StretchViewport(w, h, orthographicCamera);
        batch = new SpriteBatch();
        stage = new Stage(stretchViewport, batch);
        hudStage = new Stage(new FitViewport(w, h, new OrthographicCamera()));

        // Locate and set up tile map.
        pirateMap = new PirateMap(new TmxMapLoader().load("PirateMap/PirateMap.tmx"));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(pirateMap.getTiledMap(), 1 / 2f);

        // Initialize game managers
        this.itemManager = new ItemManager();
        this.entityManager = new EntityManager(this);
        this.questManager = new QuestManager(entityManager);
        this.buildingManager = new BuildingManager(this);

        // Create HUD (display for xp, gold, etc..)
        this.hud = new HUD(this);
        hudStage.addActor(this.hud.getTable());

        // Set input processor and focus
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(entityManager.getOrCreatePlayer());
        Gdx.input.setInputProcessor(inputMultiplexer);

        //create and spawnn player
        startGame();
    }

    private void startGame() {
        stage.addActor(entityManager.getOrCreatePlayer());
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

        //if player dead, go to main menu
        Player player = entityManager.getOrCreatePlayer();
        if (player.isDead()) {
            pirateGame.switchScreen(ScreenType.MENU);
            return;
        }

        //spawns/despawns entities, handles animations and projectiles
        entityManager.handleStageEntities(stage, delta);

        if (pirateMap.isObjectsEnabled()) {
            buildingManager.spawnCollegeEnemies(delta);
            buildingManager.checkBossSpawn();
        }

        handleHealthBars();

        this.hud.update();

        checkCollisions();

        orthographicCamera.position.set(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f, 0);
        orthographicCamera.update();
        batch.setProjectionMatrix(orthographicCamera.combined);
        tiledMapRenderer.setView(orthographicCamera);
        tiledMapRenderer.render();

        //debug
        if(DEBUG) {
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);

            for (Polygon polygonMapObject : getPirateMap().getCollisionObjects()) {
                shapeRenderer.polygon(polygonMapObject.getTransformedVertices());
            }
            shapeRenderer.end();
        }

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

        for (NPCBoat NPCBoat : entityManager.getNpcList()) {
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
        //player/npc collision check
        for (NPCBoat NPCBoat : getEntityManager().getNpcList()) {
            if (NPCBoat.getRectBounds().overlaps(player.getRectBounds())) {
                //Double actingMom = NPCBoat.getSpeed() * Math.acos(NPCBoat.getAngleTowardsLE(player));
                //player.setAngle(player.getAngle()+(float)Math.acos(player.getSpeed()/NPCBoat.getSpeed()));
                //NPCBoat.setSpeed(NPCBoat.getSpeed()/2);
            }
        }
        //player/map collision check
        //TODO: Improve to make player a polygon
        for(Polygon polygon : getPirateMap().getCollisionObjects()) {
            float[] points = polygon.getTransformedVertices();
            for (int i=0;i<points.length;i+=2){
                if(player.getRectBounds().contains(points[i],points[i+1])){
                    Gdx.app.log("GameScreen", "Collision");
                    player.setAngle((float)(2*Math.PI - player.getAngle()));
                }
            }
        }
    }

    private void checkProjectileCollisions() {
        Player player = entityManager.getOrCreatePlayer();
        for (Projectile projectile : entityManager.getProjectileManager().getProjectileList()) {
            if (projectile.getShooter() != player
                    && projectile.getRectBounds().overlaps(player.getRectBounds())) {
                //if bullet overlaps player and shooter not player
                if (!(player.isDying() || player.isDead())) {
                    if (!player.damage(projectile.getDamage())) {
                        //is dead
                        Gdx.app.debug("GameScreen", "Player died.");
                    } else {
                        Gdx.app.debug("GameScreen", "Player damaged by projectile.");
                    }
                }
                //kill projectile
                projectile.setActive(false);
            }
        }

        for (NPCBoat npcBoat : entityManager.getNpcList()) {
            for (Projectile projectile : entityManager.getProjectileManager().getProjectileList()) {
                if (projectile.getShooter() != npcBoat && projectile.getRectBounds().overlaps(npcBoat.getRectBounds())) {
                    //if bullet overlaps player and shooter not player
                    if (!(npcBoat.isDying() || npcBoat.isDead())) {
                        if (!npcBoat.damage(projectile.getDamage())) {
                            //is dead
                            Gdx.app.debug("GameScreen", "NPCBoat died.");
                            player.issueReward(itemManager.generateReward());
                            //if dead NPC is a boss then player can capture its respective college
                            if(npcBoat.isBoss() && npcBoat.getAllied().isPresent()) {
                                player.capture(npcBoat.getAllied().get());
                            }
                        }
                        Gdx.app.debug("GameScreen", "NPCBoat damaged by projectile.");
                        //kill projectile
                        projectile.setActive(false);
                    }
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.setToOrtho(false, (float) width, (float) height);
        orthographicCamera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Player player = entityManager.getOrCreatePlayer();
            Vector3 clickLoc = orthographicCamera.unproject(new Vector3(screenX, screenY, 0));
            float fireAngle = (float) (-Math.atan2(player.getCentre().x - clickLoc.x, player.getCentre().y - clickLoc.y));
            Gdx.app.log("Test Log", "Firing: Click at (rad) " + fireAngle);
            if (!player.fire(fireAngle)) {
                Gdx.app.log("Test Log", "Error firing!");
            }
            return true;
        }
        return false;
    }


    // Stub methods for InputProcessor (unused) - must return false
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
