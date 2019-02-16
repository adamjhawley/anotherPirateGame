package uk.ac.york.sepr4;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import javax.xml.soap.Text;


/**
 * Added for assessment 3, manages instances of the minigame.
 */
public class MinigameScreen implements Screen, InputProcessor {

	private PirateGame pirateGame;
	private GameScreen gameScreen;
	private Stage stage;
	private String state = "menu";
	private SpriteBatch spriteBatch;

	//minigame variables
	private boolean playerAlive = true;
	private boolean enemyAlive = true;
	private boolean playerWon = false;

	public MinigameScreen(PirateGame pirateGame, GameScreen gameScreen){
		this.pirateGame = pirateGame;
		this.gameScreen = gameScreen;
		this.spriteBatch = new SpriteBatch();

		// create stage and set it as input processor
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show(){

		showMenu();

		Gdx.input.setInputProcessor(stage);
	}

	private void showMenu(){
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"));

		Label minigameText = new Label("How difficult do you want your minigame to be?", skin);
		minigameText.setColor(0f, 0f, 0f, 1f);
		Label instructionText = new Label("Wait for the signal, then use the space bar to shoot before your opponent does.", skin);
		instructionText.setColor(0f, 0f, 0f, 1f);

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
		table.row().pad(20,0,0,0);
		table.add(instructionText).fillX().uniformX();
		table.row();

		quitMinigame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pirateGame.switchScreen(gameScreen);
			}
		});

		easyMinigame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setMinigameStateGame();
			}
		});
	}

	@Override
	public void render(float delta) {
		// clear the screen ready for next set of images to be drawn
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(state.equals("game")){
			Texture player;
			Texture enemy;
			if (playerAlive  && !enemyAlive) {
				player = new Texture(Gdx.files.internal("shootpirate/pirate_shooting.png"));
				enemy = new Texture(Gdx.files.internal("shootpirate/pirate_defeated.png"));
			} else if (playerAlive){
				player = new Texture(Gdx.files.internal("shootpirate/pirate_holstered.png"));
				enemy = new Texture(Gdx.files.internal("shootpirate/pirate_holstered_right.png"));
			} else {
				player = new Texture(Gdx.files.internal("shootpirate/pirate_defeated.png"));
				enemy = new Texture(Gdx.files.internal("shootpirate/pirate_shooting_right.png"));
			}

			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			spriteBatch.begin();
			spriteBatch.draw(player, 50f, h-player.getHeight()-50f);
			spriteBatch.draw(enemy, w-enemy.getWidth()-50, h-enemy.getHeight()-50);
			spriteBatch.end();
		}

		// tell our stage to do actions and draw itself
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	private void showGame(){

	}

	public void setMinigameStateMenu(){
		this.state = "menu";
		showMenu();
	}

	public void setMinigameStateGame(){
		this.state = "game";
		stage.clear();
	}

	@Override
	public boolean keyDown(int keycode){

		return false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button){return false;}

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
