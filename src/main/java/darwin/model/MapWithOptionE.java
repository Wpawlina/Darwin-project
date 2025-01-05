package darwin.model;

import darwin.model.animal.AbstractAnimal;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.Directions;
import java.util.ArrayList;
import java.util.HashSet;

public class MapWithOptionE extends  AbstractMap{
    public MapWithOptionE(MapInitialProperties mapInitialProperties, Boundary mapBoundary, Boundary jungleBoundary){
        super(mapInitialProperties, mapBoundary, jungleBoundary);
    }

    private HashSet<Vector2d> vitalCorpses(){
        HashSet<Vector2d> result = new HashSet<>();
        for(Vector2d space: super.animals.keySet()){
            boolean found = false;
            for(AbstractAnimal animal : animals.get(space)){
                if (animal.getProperties().getState().equals(AnimalState.RECENTLY_DIED)){
                    found = true;
                    break;
                }
            }
            if (found){
                for(Directions dir : Directions.values()){
                    Vector2d candidate = space.add(dir.toUnitVector());
                    if(!super.jungleBoundary.contains(candidate)){
                        result.add(candidate);
                    }
                }
            }
        }
        return result;
    }

    public void spawnPlants() {
        HashSet<Vector2d> spaces = mapBoundary.generateSpaces();
        HashSet<Vector2d> fertile_spaces = jungleBoundary.generateSpaces();
        fertile_spaces.addAll(vitalCorpses());
        ArrayList<ArrayList<Vector2d>> possible = super.partition(spaces, fertile_spaces);
        super.spawnPlantNo(mapInitialProperties.spawnPlantPerDay(), possible);
    }
}
