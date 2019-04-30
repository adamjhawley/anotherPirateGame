package uk.ac.york.sepr4.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import lombok.Getter;
import uk.ac.york.sepr4.GameScreen;
import uk.ac.york.sepr4.PirateGame;
import uk.ac.york.sepr4.ScreenType;
import uk.ac.york.sepr4.object.building.Building;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.building.Department;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.object.quest.QuestManager;

import java.util.ArrayList;
import java.util.Optional;

public class HUD {

    private final TextButton btnMenu;
    private GameScreen gameScreen;
    private QuestManager questManager;

    //Added for Assessment 3: Many labels and tables for the different features added in HUD
    //Added for Assessment 4: Weather, quest and new item labels.
    private Label goldLabel, goldValueLabel, xpLabel, pausedLabel, xpValueLabel, locationLabel, captureStatus,
    healthLabel, healthvalueLable, gameoverLabel, inDerwentBeforeEndLabel, haliCollegeLabel, constCollegeLabel,
    jamesCollegeLabel, langCollegeLabel, derwentCollegeLabel, departmentPromptLabel, minigamePromptLabel,
            weatherLabel, questLabel, newItemLabel;

    @Getter
    private Table table, gameoverTable, inDerwentBeforeEndTable, collegeTable, departmentPromptTable, pausedTable, minigamePromptTable;

    //Added for Assessment 4: Timers for quest management.
    private long endMessageShowTime, startMessageShowTime, gameStartTime;
    /***
     * Class responsible for storing and updating HUD variables.
     * Creates table which is drawn to the stage!
     * @param gameScreen instance of GameScreen from which to get HUD values.
     */
    public HUD(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        //Added for Assessment 4: Quest manager and more timings for quest label.
        this.questManager = gameScreen.getQuestManager();

        //Amount of time in ms to show the end of quest message
        this.gameStartTime = System.currentTimeMillis();
        this.endMessageShowTime = 3000;
        this.startMessageShowTime = 3000;

        //define a table used to organize our hud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //set default label values
        goldLabel = new Label("GOLD", new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        goldValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        xpLabel = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        xpValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        locationLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));
        captureStatus = new Label("", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));

	    //Added for Assessment 3: Health counter
        healthLabel = new Label("Health", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        healthvalueLable= new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLUE));

        //Assessment 4: Weather label
        weatherLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.RED));

        //Added for Assessment 4: Quest label
        questLabel = new Label("QUEST", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));

        //Added for Assessment 4: New Item Label
        newItemLabel = new Label("NEW_ITEM", new Label.LabelStyle(new BitmapFont(), Color.CORAL));

	    //Added for Assessment 3: Menu button
        Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"));
        btnMenu = new TextButton("Menu", skin);
        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PirateGame.PIRATEGAME.switchScreen(ScreenType.MENU);
            }
        });

        table.add(goldLabel).expandX().padTop(5);
        table.add(locationLabel).expandX().padTop(5);
        table.add(xpLabel).expandX().padTop(5);
        table.row();
        table.add(goldValueLabel).expandX();
        table.add(captureStatus).expandX();
        table.add(xpValueLabel).expandX();
        table.add(btnMenu).expandX();
        table.row();

        table.add(healthLabel).expandX();
        table.row();

        table.add(healthvalueLable).expandX();

        //Added for Assessment 4: Weather and quest labels
        table.add(weatherLabel).expandX();

        table.add(questLabel).expandX();

        table.add(newItemLabel).expandX();

        //Assessment 3: print pause during paused state

        pausedLabel = new Label("PAUSED", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        pausedLabel.setFontScale(4);
        pausedTable = new Table();
        pausedTable.center();
        pausedTable.setFillParent(true);
        pausedTable.add(pausedLabel).padBottom(200).expandX();

        //Assessment 3: add college list in paused state
        haliCollegeLabel = new Label("Halifax \n MudMan", new Label.LabelStyle(new BitmapFont(), Color.RED));
        constCollegeLabel = new Label("Constantine \n Big Boi", new Label.LabelStyle(new BitmapFont(), Color.RED));
        jamesCollegeLabel = new Label("James \n PE Teacher", new Label.LabelStyle(new BitmapFont(), Color.RED));
        langCollegeLabel = new Label("Langwith \n Glasshouse Manager", new Label.LabelStyle(new BitmapFont(), Color.RED));
        derwentCollegeLabel = new Label("Derwent \n Asbest-boss", new Label.LabelStyle(new BitmapFont(), Color.RED));
        collegeTable = new Table();
        collegeTable.setFillParent(true);
        collegeTable.add(haliCollegeLabel).expandX().padTop(5);
        collegeTable.add(constCollegeLabel).expandX().padTop(5);
        collegeTable.add(jamesCollegeLabel).expandX().padTop(5);
        collegeTable.add(langCollegeLabel).expandX().padTop(5);
        collegeTable.add(derwentCollegeLabel).expandX().padTop(5);



        // Assessment 3: Add the department prompt
        departmentPromptLabel = new Label("E to enter department", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        departmentPromptTable = new Table();
        departmentPromptTable.center();
        departmentPromptTable.setFillParent(true);
        departmentPromptTable.add(departmentPromptLabel).padBottom(100).expandX();

        // Assessment 3: GameOver screen
        gameoverLabel = new Label("CONGRATULATIONS! YOU WIN!", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        gameoverLabel.setFontScale(4);
        gameoverTable = new Table();
        gameoverTable.center();
        gameoverTable.setFillParent(true);
        gameoverTable.add(gameoverLabel).padBottom(200).expandX();

        // Assessment 3: Minigame screen
        minigamePromptLabel = new Label("E to enter minigame", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        minigamePromptTable = new Table();
        minigamePromptTable.center();
        minigamePromptTable.setFillParent(true);
        minigamePromptTable.add(minigamePromptLabel).padBottom(100).expand();

        //Assessment 3 Derwent forcefield
        inDerwentBeforeEndLabel = new Label("You must defeat Asbest-Boss' subordinates before challenging him", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        inDerwentBeforeEndLabel.setFontScale(2);
        inDerwentBeforeEndTable = new Table();
        inDerwentBeforeEndTable.center();
        inDerwentBeforeEndTable.setFillParent(true);
        inDerwentBeforeEndTable.add(inDerwentBeforeEndLabel).padBottom(200).expandX();
    }

    /***
     * Update label values - called during stage render
     */
    public void update() {
        Player player = gameScreen.getEntityManager().getOrCreatePlayer();

        //balance and xp overheads
        goldValueLabel.setText(""+player.getBalance());
        //+" ("+(player.getLevelProgress())*100+"%)"
        xpValueLabel.setText(""+player.getLevel());

        healthvalueLable.setText(""+player.getHealth().intValue());


        //location overhead
        boolean captured = false;
        Optional<Building> loc = gameScreen.getEntityManager().getPlayerLocation();
        if(loc.isPresent()) {
            locationLabel.setText(loc.get().getName().toUpperCase());
            if(loc.get() instanceof College) {
		//Changed for Assessment 3: Check if all other colleges have been captured to gain access to Derwent
                ArrayList<College> capturedCheck = (ArrayList<College>)gameScreen.getEntityManager().getOrCreatePlayer().getCaptured();

                if (loc.get().getName().equals("Derwent College") && !(capturedCheck.size() >=4)){
                    gameScreen.setInDerwentBeforeEnd(true);
                    player.movePlayer(gameScreen.getPirateMap().getSpawnPoint());
                }
                if (gameScreen.getEntityManager().getOrCreatePlayer().getCaptured().contains(loc.get())) {
                    captured = true;
		    //Added for Assessment 3: Highlight captured colleges in pause screen
                    if (loc.get().getName().equals("Derwent College")){
                        locationLabel.setText("GAMEOVER");
                        gameScreen.paused = true;
                        gameScreen.setGameOver(true);
                    }
                    if (loc.get().getName().equals("Halifax College")){
                        haliCollegeLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.GREEN) );
                    }
                    if (loc.get().getName().equals("Constantine College")){
                        constCollegeLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.GREEN) );
                    }
                    if (loc.get().getName().equals("James College")){
                        jamesCollegeLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.GREEN) );
                    }
                    if (loc.get().getName().equals("Langwith College")){
                        langCollegeLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.GREEN) );
                    }





                }
            }
	    //Added for Assessment 3: allow player to enter department if near
            else if(loc.get() instanceof Department) {
                gameScreen.setNearDepartment(true);
            }
        } else {
            gameScreen.setNearDepartment(false);
            locationLabel.setText("OPEN SEAS");
        }
        if(captured) {
            captureStatus.setText("(CAPTURED)");
        } else {
            captureStatus.setText("");
        }

	//Added for Assessment 3: Set the visibility conditions of each temporary prompt
        departmentPromptTable.setVisible(gameScreen.getNearDepartment());
        pausedTable.setVisible(GameScreen.isPaused() && !gameScreen.getNearDepartment() && !gameScreen.getGameOver());
        collegeTable.setVisible(GameScreen.isPaused() && !gameScreen.getNearDepartment() && !gameScreen.getGameOver());
        gameoverTable.setVisible(gameScreen.getGameOver());
        minigamePromptTable.setVisible(gameScreen.isNearMinigame());
        inDerwentBeforeEndTable.setVisible(gameScreen.isInDerwentBeforeEnd());

        //Assessment 4: Weather label
        if (gameScreen.weatherEffect) {
            weatherLabel.setText("You are in bad weather!");
        } else {
            weatherLabel.setText("");
        }

        //Added for Assessment 4: Quest label, Quest message, New Item label and update New Item messag, New Item label and update New Item message method.
        questLabel.setText(updateQuestMessage());
        newItemLabel.setText(updateNewItem());
    }

    /**
     * Checks to see if the most recently completed quest was completed within the time specified by the
     * endMessageShowTime variable, if it was then it returns the completed message's end message.
     * @return String containing the actual message.
     */
    private String updateQuestMessage(){
        String msg;
        //The whole method works based on comparisons between System.currentTimeMillis() and the two constants
        //start(and end)MessageShowtime
        if (System.currentTimeMillis()<this.gameStartTime+startMessageShowTime){
            //If the game has started but hasn't been running for longer than the startMessageShowTime
            msg = this.questManager.getCurrentQuest().getStartMessage();
        }
        //This section manages the quest completion message from the most recently completed quest.
        else if (this.questManager.getLastQuest() != null){
            long timeSinceLastQuestCompletion = System.currentTimeMillis() - this.questManager.getLastQuest().getTimeCompleted();
            if (timeSinceLastQuestCompletion <endMessageShowTime) {
                msg = this.questManager.getLastQuest().getEndMessage();
            }
            else if (timeSinceLastQuestCompletion < startMessageShowTime + endMessageShowTime && this.questManager.getCurrentQuest() != null){
                msg = this.questManager.getCurrentQuest().getStartMessage();
            }
            else {
                msg = ("Active Quest: " + this.questManager.getQuestStatus());
            }
        }
        else {
            msg = ("Active Quest: " + this.questManager.getQuestStatus());
        }
        return msg;
    }

    private String updateNewItem(){
       String msg = "";
       Player player = gameScreen.getEntityManager().getOrCreatePlayer();
       if (System.currentTimeMillis()< player.getNewItemTime()+startMessageShowTime){
           msg = "NEW ITEM: " + player.getNewItem().getName();
       }
       return msg;
    }
}
