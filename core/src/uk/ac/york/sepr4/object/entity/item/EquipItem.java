package uk.ac.york.sepr4.object.entity.item;

import lombok.Data;
import uk.ac.york.sepr4.object.entity.attribute.Attributes;


@Data
public class EquipItem extends Item {

    private Attributes attributes;

    public EquipItem() {
        this("item name", "item lore", new Attributes());
    }

    public EquipItem(String name, String lore, Attributes attributes) {
        super(name, lore);

        this.attributes = attributes;
    }

    private Attributes getBoost(){
        return this.attributes;
    }
}
