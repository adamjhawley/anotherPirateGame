package uk.ac.york.sepr4.object.building;

import java.util.ArrayList;
import java.util.List;

public class BuildingManager {

    private List<College> colleges;
    private List<Department> departments;

    public BuildingManager() {
        this.colleges = new ArrayList<College>();
        this.departments = new ArrayList<Department>();
    }

}
