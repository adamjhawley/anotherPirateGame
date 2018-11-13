package uk.ac.york.sepr4.object.item;

import lombok.Data;


@Data
public class EquipItem extends Item {

    public EquipItem() {
        this("item name", "item lore");
    }

    public EquipItem(String name, String lore) {
        super(name, lore);

    }

}
