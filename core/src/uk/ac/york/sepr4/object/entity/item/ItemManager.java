package uk.ac.york.sepr4.object.entity.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;

@Data
public class ItemManager {

    private Array<EquipItem> equipItems;
    private Array<Item> consumeItems;

    public ItemManager() {
        Json json = new Json();
        equipItems = json.fromJson(Array.class, EquipItem.class, Gdx.files.internal("equip_items.json"));
       // consumeItems = json.fromJson(Array.class, ConsumeItem.class, Gdx.files.internal("consume_items.json"));
        for(EquipItem item : equipItems){
            System.out.println(item.getName());
            System.out.println(item.getAttributes().getHealth());

        }

    }

}
