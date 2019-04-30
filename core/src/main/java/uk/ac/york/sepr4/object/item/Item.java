package uk.ac.york.sepr4.object.item;

import lombok.Data;

@Data
public class Item {

    private String name;
    private String lore;
    private Double damageMultiplier;

    public Item() {}

    public Item(String name, String lore, Double damageMultiplier) {
        this.name = name;
        this.lore = lore;
        this.damageMultiplier = damageMultiplier;
    }
}
