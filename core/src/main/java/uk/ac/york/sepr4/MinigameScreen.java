package uk.ac.york.sepr4;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;


/**
 * Added for assessment 3, manages instances of the minigame.
 */
public class MinigameScreen implements Screen, InputProcessor {

	private PirateGame pirateGame;
	private GameScreen gameScreen;
	private Stage stage;
	private String state = "menu";
	private SpriteBatch spriteBatch;
	private InputMultiplexer inputMultiplexer;

	//minigame variables
	private boolean playerAlive = true;
	private boolean enemyAlive = true;
	private boolean playerWon = false;
	private boolean playerDisqualified = false;
	private boolean gameStarted = false;
	private float countdown;
	private float enemyShotCountdown;
	private String difficulty; //easy, medium, hard, very hard

	public MinigameScreen(PirateGame pirateGame, GameScreen gameScreen){
		this.pirateGame = pirateGame;
		this.gameScreen = gameScreen;
		this.spriteBatch = new SpriteBatch();

		// create stage and set it as input processor
		stage = new Stage(new ScreenViewport());

		// use input multiplexer to enable scene2d inputs and keyboard inputs at the same time
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void show(){

		showMenu();

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * Display the minigame's main menu.
	 */
	private void showMenu(){
		// clear existing UI, initialise scene2d objects
		stage.clear();
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"));

		// instantiate labels and buttons
		Label minigameText = new Label("How difficult do you want your minigame to be?", skin);
		minigameText.setColor(0f, 0f, 0f, 1f);
		Label instructionText = new Label("Wait for the signal, then use the Z key to shoot before your opponent does.", skin);
		instructionText.setColor(0f, 0f, 0f, 1f);

		TextButton quitMinigame = new TextButton("Go back to game", skin);
		TextButton easyMinigame = new TextButton("Easy", skin);
		TextButton mediumMinigame = new TextButton("Medium", skin);
		TextButton hardMinigame = new TextButton("Hard", skin);
		TextButton veryhardMinigame = new TextButton("Very Hard", skin);

		// declare UI layout
		table.add(minigameText).fillX().uniformX();
		table.row().pad(20, 0, 0, 0);
		table.add(easyMinigame).fillX().uniformX();
		table.row();
		table.add(mediumMinigame).fillX().uniformX();
		table.row();
		table.add(hardMinigame).fillX().uniformX();
		table.row();
		table.add(veryhardMinigame).fillX().uniformX();
		table.row().pad(20, 0, 10, 0);
		table.add(quitMinigame).fillX().uniformX();
		table.row().pad(20,0,0,0);
		table.add(instructionText).fillX().uniformX();
		table.row();

		// declare button functionality
		quitMinigame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pirateGame.switchScreen(gameScreen);
			}
		});

		easyMinigame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				difficulty = "easy";
				setMinigameStateGame();
			}
		});

		mediumMinigame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				difficulty = "medium";
				setMinigameStateGame();
			}
		});

		hardMinigame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				difficulty = "hard";
				setMinigameStateGame();
			}
		});

		veryhardMinigame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				difficulty = "very hard";
				setMinigameStateGame();
			}
		});
	}

	@Override
	public void render(float delta) {
		// clear the screen ready for next set of images to be drawn
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// renders player and enemy sprites on screen once the minigame is started
		if(state.equals("game")){
			// displays game interface when the minigame is started
			showGame();

			// counts down time from "Ready..." to "FIRE!"
			if(this.countdown > 0){
				this.countdown -= Gdx.graphics.getDeltaTime();
			}

			// if the game has started, counts down enemy's shot time
			if (gameStarted){
				if(enemyShotCountdown > 0){
					enemyShotCountdown -= Gdx.graphics.getDeltaTime();
				} else {
					handleShot("enemy");
				}
			}

			// selects sprite for player and enemy depending on game state and difficulty
			Texture player;
			Texture enemy;
			if (playerAlive  && !enemyAlive) {
				player = new Texture(Gdx.files.internal("shootpirate/pirate_shooting.png"));
				enemy = new Texture(Gdx.files.internal("shootpirate/pirate_defeated.png"));
			} else if (playerAlive && enemyAlive){
				player = new Texture(Gdx.files.internal("shootpirate/pirate_holstered.png"));

				if(difficulty.equals("easy")){
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_holstered_right_easy.png"));
				} else if(difficulty.equals("medium")){
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_holstered_right_medium.png"));
				} else if(difficulty.equals("very hard")){
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_holstered_right_veryhard.png"));
				} else {
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_holstered_right.png"));
				}

			} else if (!playerAlive && enemyAlive){
				player = new Texture(Gdx.files.internal("shootpirate/pirate_defeated.png"));

				if(difficulty.equals("easy")){
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_shooting_right_easy.png"));
				} else if (difficulty.equals("medium")){
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_shooting_right_medium.png"));
				} else if (difficulty.equals("very hard")){
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_shooting_right_veryhard.png"));
				} else {
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_shooting_right.png"));
				}

			} else {
				player = new Texture(Gdx.files.internal("shootpirate/pirate_holstered.png"));

				if(difficulty.equals("easy")){
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_holstered_right_easy.png"));
				} else if(difficulty.equals("medium")){
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_holstered_right_medium.png"));
				} else if(difficulty.equals("very hard")){
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_holstered_right_veryhard.png"));
				} else {
					enemy = new Texture(Gdx.files.internal("shootpirate/pirate_holstered_right.png"));
				}
			}

			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			spriteBatch.begin();
			spriteBatch.draw(player, 100f, h-player.getHeight()-200f);
			spriteBatch.draw(enemy, w-enemy.getWidth()-100, h-enemy.getHeight()-200);
			spriteBatch.end();
		}

		// tell our stage to do actions and draw itself
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	private void showGame(){
		stage.clear();
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"));

		if(!gameStarted){
			Label readyText = new Label("Press SPACE when you're ready! Press Z to shoot!", skin);
			readyText.setColor(0, 0, 0, 1);

			table.add(readyText);
		} else {
			if(!playerWon && !playerDisqualified && playerAlive){
				if(countdown > 0){
					Label readyText = new Label("Ready...", skin);
					readyText.setColor(0, 0, 0, 1);

					table.add(readyText);
				} else {
					Texture fireTextTexture;
					fireTextTexture = new Texture(Gdx.files.internal("shootpirate/fire_text_white.png"));

					Image fireText;
					fireText = new Image(fireTextTexture);
					fireText.setColor(1, 0, 0, 1);


					table.add(fireText);
				}
			} else if (playerDisqualified){
				Label dqText = new Label("You shot early! Disqualified!", skin);
				dqText.setColor(0, 0, 0, 1);

				Label menuText = new Label("Press SPACE to go back to the menu.", skin);
				menuText.setColor(0, 0, 0, 1);

				table.add(dqText);
				table.row();
				table.add(menuText);
			} else if (playerWon){
				Label winText = new Label("You win! Press SPACE to go back to the menu.", skin);
				winText.setColor(0, 0, 0, 1);

				table.add(winText);
			} else if (!playerAlive){
				Label loseText = new Label("You lose! Press SPACE to go back to the menu.", skin);
				loseText.setColor(0, 0, 0, 1);

				table.add(loseText);
			}
		}
	}

	public void setMinigameStateMenu(){
		this.state = "menu";
		stage.clear();
		gameStarted = false;
		showMenu();
	}

	public void setMinigameStateGame(){
		this.state = "game";
		this.playerAlive = true;
		this.enemyAlive = true;
		this.playerDisqualified = false;
		this.playerWon = false;
		stage.clear();
		showGame();
	}

	@Override
	public boolean keyDown(int keycode){
		if(!gameStarted){
			if(keycode == Input.Keys.SPACE){
				startGame();
			}
		}
		else
		{
			if(keycode == Input.Keys.Z){
				if(!playerWon && !playerDisqualified && playerAlive) {
					handleShot("player");
				}
			}
			if(keycode == Input.Keys.SPACE){
				setMinigameStateMenu();
			}
		}
		return false;
	}

	/**
	 * Method used to resolve shooting.
	 * @param shooter Has to be a string, "player" or "enemy". Other arguments do nothing.
	 */
	private void handleShot(String shooter){
		if(shooter.equals("player")){
			// Disqualify the player if they fire before the signal, have them win otherwise.
			if(countdown > 0) {
				playerDisqualified = true;
			} else {
				playerWon = true;
				enemyAlive = false;
			}
		} else if (shooter.equals("enemy")){
			if(enemyAlive){
				playerAlive = false;
			}
		}

	}

	private void startGame(){
		gameStarted = true;
		Random randomiser = new Random();
		countdown = randomiser.nextInt(4)+1;

		switch(difficulty){
			case "easy":
				enemyShotCountdown = countdown+20;
				break;
			case "medium":
				enemyShotCountdown = countdown+0.3f;
				break;
			case "hard":
				enemyShotCountdown = countdown+0.25f;
				break;
			case "very hard":
				enemyShotCountdown = countdown+0.2f;
				break;
		}
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
