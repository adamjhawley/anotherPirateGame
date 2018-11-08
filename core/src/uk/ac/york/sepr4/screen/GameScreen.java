package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.PirateGame;
import uk.ac.york.sepr4.object.entity.item.ItemManager;

public class GameScreen implements Screen {

    private PirateGame pirateGame;
    private Stage stage;

    private ItemManager itemManager;

    public GameScreen(PirateGame pirateGame) {
        this.pirateGame = pirateGame;

        // create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.itemManager = new ItemManager();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
