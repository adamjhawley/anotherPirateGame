package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import lombok.Getter;
import uk.ac.york.sepr4.GameScreen;

import javax.naming.NameNotFoundException;

public class ShopUI {

    private GameScreen gameScreen;
    @Getter
    private Table table;
    private String name;

    public ShopUI (GameScreen gameScreen, String name) throws NameNotFoundException {
        this.name = name;
        this.gameScreen = gameScreen;

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
                throw new NameNotFoundException("Wrong department name");
        }


        Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"));

        Label shopLabel = new Label("Department of " + name, new Label.LabelStyle(new BitmapFont(), Color.GOLD));

        TextButton[] items = new TextButton[itemNames.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = new TextButton(itemNames[i], skin);
        }

    }



}
