package uk.ac.york.sepr4;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


/**
 * Added for assessment 3, manages instances of the minigame.
 */
public class MinigameScreen implements Screen {

	private PirateGame pirateGame;
	private GameScreen gameScreen;
	private Stage stage;

	public MinigameScreen(PirateGame pirateGame, GameScreen gameScreen){
		this.pirateGame = pirateGame;
		this.gameScreen = gameScreen;

		// create stage and set it as input processor
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show(){
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"));

		Label minigameText = new Label("How difficult do you want your minigame to be?", skin);

		TextButton quitMinigame = new TextButton("Go back to game", skin);
		TextButton easyMinigame = new TextButton("Easy", skin);
		TextButton mediumMinigame = new TextButton("Medium", skin);
		TextButton hardMinigame = new TextButton("Hard", skin);
		TextButton reallyhardMinigame = new TextButton("Very Hard", skin);

		table.add(minigameText).fillX().uniformX();
		table.row().pad(20, 0, 0, 0);
		table.add(easyMinigame).fillX().uniformX();
		table.row();
		table.add(mediumMinigame).fillX().uniformX();
		table.row();
		table.add(hardMinigame).fillX().uniformX();
		table.row();
		table.add(reallyhardMinigame).fillX().uniformX();
		table.row().pad(20, 0, 10, 0);
		table.add(quitMinigame).fillX().uniformX();

		quitMinigame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pirateGame.switchScreen(gameScreen);
			}
		});

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		// clear the screen ready for next set of images to be drawn
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell our stage to do actions and draw itself
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// change the stage's viewport when teh screen size is changed
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
