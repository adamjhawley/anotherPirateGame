package uk.ac.york.sepr4.object.building;


import lombok.Data;

@Data
public class MinigameBuilding extends Building{
    private Float buildingRange = 500f;
    public MinigameBuilding(){
        // Empty constructor for JSON DAO
    }
}
