package uk.ac.york.sepr4.screen.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Getter;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.screen.GameScreen;

public class HUD {

    private GameScreen gameScreen;

    private Label goldLabel, goldValueLabel, xpLabel, xpValueLabel;
    @Getter
    private Table table;

    public HUD(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        //define a table used to organize our hud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        goldLabel = new Label("GOLD", new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        goldValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        xpLabel = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        xpValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        table.add(goldLabel).expandX().padTop(10);
        table.add(xpLabel).expandX().padTop(10);
        table.row();
        table.add(goldValueLabel).expandX();
        table.add(xpValueLabel).expandX();

    }

    public void update() {
        Player player = gameScreen.getEntityManager().getOrCreatePlayer();

        goldValueLabel.setText(""+player.getBalance());
        xpValueLabel.setText(""+player.getLevel()+", "+player.getXp()+ " : "+(player.levelProgress())+"%");

    }

}
