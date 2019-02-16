package uk.ac.york.sepr4.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Getter;
import uk.ac.york.sepr4.GameScreen;
import uk.ac.york.sepr4.object.building.Building;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.building.Department;
import uk.ac.york.sepr4.object.entity.Player;
import java.util.Optional;

public class HUD {

    private GameScreen gameScreen;

    private Label goldLabel, goldValueLabel, xpLabel, pausedLabel, xpValueLabel, locationLabel, captureStatus,
                departmentPromptLabel, minigamePromptLabel;
    @Getter
    private Table table, departmentPromptTable, minigamePromptTable, pausedTable;

    /***
     * Class responsible for storing and updating HUD variables.
     * Creates table which is drawn to the stage!
     * @param gameScreen instance of GameScreen from which to get HUD values.
     */
    public HUD(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

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

        table.add(goldLabel).expandX().padTop(5);
        table.add(locationLabel).expandX().padTop(5);
        table.add(xpLabel).expandX().padTop(5);
        table.row();
        table.add(goldValueLabel).expandX();
        table.add(captureStatus).expandX();
        table.add(xpValueLabel).expandX();

        // Print Pause during paused state

        pausedLabel = new Label("PAUSED", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        pausedLabel.setFontScale(4);
        pausedTable = new Table();
        pausedTable.center();
        pausedTable.setFillParent(true);
        pausedTable.add(pausedLabel).padBottom(200).expandX();

        // Assessment 3: Add the department prompt
        departmentPromptLabel = new Label("E to enter department", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        departmentPromptTable = new Table();
        departmentPromptTable.center();
        departmentPromptTable.setFillParent(true);
        departmentPromptTable.add(departmentPromptLabel).padBottom(100).expandX();

        minigamePromptLabel = new Label("E to enter minigame", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        minigamePromptTable = new Table();
        minigamePromptTable.center();
        minigamePromptTable.setFillParent(true);
        minigamePromptTable.add(minigamePromptLabel).padBottom(100).expand();
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

        //location overhead
        boolean captured = false;
        Optional<Building> loc = gameScreen.getEntityManager().getPlayerLocation();
        if(loc.isPresent()) {
            locationLabel.setText(loc.get().getName().toUpperCase());
            if(loc.get() instanceof College) {
                if (gameScreen.getEntityManager().getOrCreatePlayer().getCaptured().contains(loc.get())) {
                    captured = true;
                }
            }
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
        departmentPromptTable.setVisible(gameScreen.getNearDepartment());
        minigamePromptTable.setVisible(gameScreen.isNearMinigame());
        pausedTable.setVisible(GameScreen.isPaused() && !gameScreen.getNearDepartment() && !gameScreen.isNearMinigame());
    }

}
