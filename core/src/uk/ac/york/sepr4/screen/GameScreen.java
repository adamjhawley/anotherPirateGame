package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Data;
import lombok.Getter;
import uk.ac.york.sepr4.PirateGame;
import uk.ac.york.sepr4.hud.HealthBar;
import uk.ac.york.sepr4.object.entity.*;
import uk.ac.york.sepr4.object.entity.item.ItemManager;
import uk.ac.york.sepr4.object.entity.projectile.Projectile;
import uk.ac.york.sepr4.object.entity.projectile.ProjectileManager;

public class GameScreen implements Screen, InputProcessor {

    private PirateGame pirateGame;
    private Stage stage;
    private Stage hudStage;
    private SpriteBatch batch;

    private OrthographicCamera orthographicCamera;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    private ItemManager itemManager;
    @Getter
    private EntityManager entityManager;
    @Getter
    private ProjectileManager projectileManager;

    private InputMultiplexer inputMultiplexer;

    private static GameScreen gameScreen;

    public static GameScreen getInstance() {
        return gameScreen;
    }

    public GameScreen(PirateGame pirateGame) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        this.pirateGame = pirateGame;
        gameScreen = this;

        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, w, h);
        orthographicCamera.update();

        StretchViewport stretchViewport = new StretchViewport(w, h, orthographicCamera);
        batch = new SpriteBatch();
        stage = new Stage(stretchViewport, batch);
        hudStage = new Stage(stretchViewport);

        tiledMap = new TmxMapLoader().load("TestMap/PP_Sea_100.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 2f);

        this.itemManager = new ItemManager();
        this.entityManager = new EntityManager();
        this.projectileManager = new ProjectileManager(entityManager);

        stage.addActor(entityManager.getOrCreatePlayer());
        Enemy enemy = new EnemyBuilder().buildEnemy(entityManager.getNextEnemyID(), new Vector2(200f, 200f));
        entityManager.addEnemy(enemy);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(entityManager.getOrCreatePlayer());

        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Player player = entityManager.getOrCreatePlayer();

        handleProjectiles();
        handleEnemies();

        handleHUD();

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

    private void handleHUD() {
        Player player = entityManager.getOrCreatePlayer();
        if (player.getHealth() < player.getMaxHealth()) {
            if (!hudStage.getActors().contains(player.getHealthBar(), true)) {
                hudStage.addActor(player.getHealthBar());

            }
        }

        for (Enemy enemy : entityManager.getEnemyList()) {
            if (enemy.getHealth() < enemy.getMaxHealth()) {
                if (!hudStage.getActors().contains(enemy.getHealthBar(), true)) {
                    hudStage.addActor(enemy.getHealthBar());
                }
            }
        }
        Array<Actor> toRemove = new Array<Actor>();
        for (Actor actors : hudStage.getActors()) {
            if (actors instanceof HealthBar) {
                HealthBar healthBar = (HealthBar) actors;
                LivingEntity livingEntity = healthBar.getLivingEntity();
                if (livingEntity.getHealth() == livingEntity.getMaxHealth() || livingEntity.isDead() || livingEntity.isDying()) {
                    toRemove.add(actors);
                }
            }
        }
        hudStage.getActors().removeAll(toRemove, true);

    }

    private void checkCollisions() {
        Player player = entityManager.getOrCreatePlayer();
        for (Projectile projectile : projectileManager.getProjectileList()) {
            if (projectile.getShooter() != player && projectile.getBounds().overlaps(player.getBounds())) {
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

        for (Enemy enemy : entityManager.getEnemyList()) {
            for (Projectile projectile : projectileManager.getProjectileList()) {
                if (projectile.getShooter() != enemy && projectile.getBounds().overlaps(enemy.getBounds())) {
                    //if bullet overlaps player and shooter not player
                    if (!enemy.damage(projectile.getProjectileType().getDamage())) {
                        //is dead
                        Gdx.app.log("Test Log", "Enemy died. ");
                    }
                    Gdx.app.log("Test Log", "Enemy damaged by projectile. ");
                    //kill projectile
                    projectile.setActive(false);
                }
            }
        }
    }

    private void handleProjectiles() {
        stage.getActors().removeAll(projectileManager.removeNonActiveProjectiles(), true);

        for (Projectile projectile : projectileManager.getProjectileList()) {
            if (!stage.getActors().contains(projectile, true)) {
                Gdx.app.log("Test Log", "Adding new projectile to actors list.");
                stage.addActor(projectile);
            }
        }
    }

    private void handleEnemies() {
        stage.getActors().removeAll(entityManager.removeDeadEnemies(), true);

        for (Enemy enemy : entityManager.getEnemyList()) {
            if (!stage.getActors().contains(enemy, true)) {
                Gdx.app.log("Test Log", "Adding new enemy to actors list.");
                stage.addActor(enemy);
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
            player.fire(fireAngle);
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
