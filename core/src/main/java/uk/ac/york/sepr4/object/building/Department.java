package uk.ac.york.sepr4.object.building;

import lombok.Data;
import uk.ac.york.sepr4.object.entity.Player;

@Data
public class Department extends Building {
    private Float buildingRange = 500f;
    public Department() {
        // Empty constructor for JSON DAO
    }

}
