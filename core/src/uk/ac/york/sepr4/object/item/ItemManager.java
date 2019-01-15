package uk.ac.york.sepr4.object.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;

import java.util.Random;

@Data
public class ItemManager {

    private Array<Item> items;


    private Integer baseXP = 10, baseGold = 100;

    public ItemManager() {
        Json json = new Json();
        items = json.fromJson(Array.class, Item.class, Gdx.files.internal("items.json"));
        for(Item item : items){
            System.out.println(item.getName());
            //System.out.println(item.getAttribute().getHealth());

        }

    }

    public Reward generateReward(Double enemyDifficulty) {
        Double scale = enemyDifficulty + 1;

        Integer xp =  (baseXP * scale.intValue());

        return new Reward(baseXP * scale.intValue(), baseGold*scale.intValue());
    }

}
