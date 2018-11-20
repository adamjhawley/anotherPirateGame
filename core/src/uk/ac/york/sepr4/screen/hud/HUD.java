package uk.ac.york.sepr4.screen.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Getter;
import uk.ac.york.sepr4.screen.GameScreen;

public class HUD {

    private GameScreen gameScreen;

    private Label goldLabel;
    private Label goldValueLabel;
    @Getter
    private Table table;

    private Integer goldValue = 0;

    public HUD(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        //define a table used to organize our hud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        goldLabel = new Label("GOLD", new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        goldValueLabel = new Label(""+goldValue, new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        table.add(goldLabel).expandX().padTop(10);
        table.row();
        table.add(goldValueLabel).expandX();

    }

    public void update() {
        goldValue = gameScreen.getEntityManager().getOrCreatePlayer().getBalance();

        goldValueLabel.setText(""+goldValue);
    }

}
