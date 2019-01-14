package uk.ac.york.sepr4.object.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;

@Data
public class ItemManager {

    private Array<Item> items;

    public ItemManager() {
        Json json = new Json();
        items = json.fromJson(Array.class, Item.class, Gdx.files.internal("items.json"));
        for(Item item : items){
            System.out.println(item.getName());
            //System.out.println(item.getAttribute().getHealth());

        }

    }

}
