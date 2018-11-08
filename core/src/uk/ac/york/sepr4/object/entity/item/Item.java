package uk.ac.york.sepr4.object.entity.item;

import lombok.Data;

@Data
public abstract class Item {

    private String name;
    private String lore;

    public Item(String name, String lore) {
        this.name = name;
        this.lore = lore;
    }
}
