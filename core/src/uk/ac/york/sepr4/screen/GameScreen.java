package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Getter;
import uk.ac.york.sepr4.PirateGame;
import uk.ac.york.sepr4.object.entity.Enemy;
import uk.ac.york.sepr4.object.entity.EntityManager;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.object.entity.item.ItemManager;
import uk.ac.york.sepr4.object.entity.projectile.Projectile;
import uk.ac.york.sepr4.object.entity.projectile.ProjectileManager;

public class GameScreen implements Screen, InputProcessor {

    private PirateGame pirateGame;
    private Stage stage;
    private SpriteBatch batch;

    private OrthographicCamera orthographicCamera;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    private ItemManager itemManager;
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
        orthographicCamera.setToOrtho(false, w,h);
        orthographicCamera.update();

        StretchViewport stretchViewport = new StretchViewport(w,h,orthographicCamera);
        batch = new SpriteBatch();
        stage = new Stage(stretchViewport, batch);

        tiledMap = new TmxMapLoader().load("TestMap/PP_Sea_100.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/2f);

        this.itemManager = new ItemManager();
        this.entityManager = new EntityManager();
        this.projectileManager = new ProjectileManager(entityManager);

        stage.addActor(entityManager.getOrCreatePlayer());
        stage.addActor(entityManager.getOrCreateEnemy()); //Will need to changed

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
        Enemy enemy = entityManager.getOrCreateEnemy();

        handleProjectiles();
        handleEnemies();
        checkCollisions();

        orthographicCamera.position.set(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f,0);
        orthographicCamera.update();
        batch.setProjectionMatrix(orthographicCamera.combined);
        tiledMapRenderer.setView(orthographicCamera);
        tiledMapRenderer.render();

        stage.act(delta);
        stage.draw();

    }

    private void checkCollisions() {
        //TODO: CHECK!
    }

    private void handleProjectiles() {
        stage.getActors().removeAll(projectileManager.removeNonActiveProjectiles(), true);

        for(Projectile projectile:projectileManager.getProjectileList()) {
            if(!stage.getActors().contains(projectile, true)) {
                Gdx.app.log("Test Log", "Adding new projectile to actors list.");
                stage.addActor(projectile);
            }
        }
    }

    private void handleEnemies() {
        //
    }

    @Override
    public void resize(int width, int height) {
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
        if(button == Input.Buttons.LEFT) {
            Player player = entityManager.getOrCreatePlayer();
            Vector3 clickLoc = orthographicCamera.unproject(new Vector3(screenX, screenY, 0));
            float fireAngle = (float)( -Math.atan2(player.getCentre().x-clickLoc.x, player.getCentre().y-clickLoc.y));
            Gdx.app.log("Test Log","Firing: Click at (rad) "+fireAngle);
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
