package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.GameScreen;
import uk.ac.york.sepr4.object.entity.Player;
import lombok.Getter;

import javax.naming.NameNotFoundException;


public class ShopUI {

    private GameScreen gameScreen;
    private Player player;
    @Getter
    private Table table;
    private String name;
    @Getter
    private Stage stage;

    public ShopUI (GameScreen gameScreen, String name) throws NameNotFoundException {
        this.name = name;
        this.gameScreen = gameScreen;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.stage = new Stage(new ScreenViewport());

        table = new Table();
        table.setFillParent(true);
        String[] itemNames;
        switch (name){
            case "biology":
                itemNames = new String[]{"Full heal : 100g", "Increase Max Health : 200g"};
                break;
            case "computer science":
                itemNames = new String[]{"Increase shot damage : 100g", "Piercing shots : 200g"};
                break;
            case "physics":
                itemNames = new String[]{"Increase max speed : 100g", "Increase maneuverability: 200g"};
                break;
            default:
                throw new NameNotFoundException();
        }


        Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"));

        Label shopLabel = new Label("Department of " + name, new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        table.add(shopLabel).fillX().uniformX();
        table.row().pad(10,0,10,0);
        TextButton[] items = new TextButton[itemNames.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = new TextButton(itemNames[i], skin);
            int finalI = i;
            items[i].addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    shopEvent(name, finalI);
                }
            });
            table.add(items[i]).fillX().uniformX();
            table.row().pad(10,0,10,0);
        }
        TextButton exit = new TextButton("Exit shop", skin);
        table.add(exit).fillX().uniformX();
    }

    private void shopEvent(String name, int finalI) {
        switch(name) {
            case "biology":
                if (finalI == 0) {
                    if (player.deduceBalance(100) && player.getHealth() != player.getMaxHealth()) {
                        player.setHealth(player.getMaxHealth());
                    }
                }
                else if (finalI == 1) {
                    if (player.deduceBalance(200)) {
                        player.setMaxHealth(player.getMaxHealth() * 1.25);
                        player.setHealth(player.getHealth() * 1.25);
                    }
                }
                break;
            case "physics":
                if (finalI == 0) {
                    if (player.deduceBalance(100)) {
                        player.setMaxSpeed(player.getMaxSpeed() * 1.25f);
                    }
                }
                else if (finalI == 0) {
                    if (player.deduceBalance(200)) {
                        player.setTurningSpeed(player.getTurningSpeed() * 1.25f);
                    }
                }
                break;
            case "computer science":
               break;
        }
    }


}
