package uk.ac.york.sepr4.object.item;

import lombok.Data;

import java.util.List;

@Data
public class Reward {

    private Integer xp;
    private Integer gold;

    public Reward() {}

    public Reward(Integer xp, Integer gold) {
        this.xp = xp;
        this.gold = gold;
    }

}
