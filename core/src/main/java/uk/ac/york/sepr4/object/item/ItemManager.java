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
        Gdx.app.log("ItemManager", "Loaded "+items.size+" items!");

    }

    /**
     * Added for Assessment 4: Items can now be added to rewards by passing them as a parameter or null if no item is needed.
     * @param item
     * @return Reward generated
     */
    public Reward generateReward(Item item) {
        Random random = new Random();
        Double scale = random.nextDouble();
        Reward reward = new Reward(baseXP + (scale.intValue()*100), baseGold + (scale.intValue()*10));
        if (item != null) {
            reward.addItem(item);
        }
        return reward;
    }

    /**
     Added for Assessment 4: So that items can be specified by their name (especially for use with generateReward()
     @param itemName
     @return item
     @throws NullPointerException when item not found
     */
    public Item  retrieveItem (String itemName){
        for (Item item : items){
            if (item.getName().equals(itemName)){
                return item;
            }
        }
        throw new NullPointerException("No item with the name " + itemName  +" found in items.json");
    }

}
