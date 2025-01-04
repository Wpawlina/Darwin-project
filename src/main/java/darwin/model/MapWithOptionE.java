package darwin.model;

import darwin.model.animal.AbstractAnimal;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.Directions;
import darwin.util.MyRandom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void spawnPlants(){
        spawnPlantNo(mapInitialProperties.spawnPlantPerDay());
    }
    private void spawnPlantNo(int no){
        MyRandom random = new MyRandom();
        ArrayList<ArrayList<Vector2d>> possible = partition(mapBoundary, jungleBoundary);
        ArrayList<Vector2d> possible_fertile = possible.get(0);
        ArrayList<Vector2d> possible_infertile = possible.get(1);
        for(int i = 0; i < no; i++){
            float firstDraw = random.RandomFrac();
            if(firstDraw <0.2){
                int secondDraw = random.RandomInt(0, possible_fertile.size());
                Plant newplant = new Plant(possible_fertile.get(secondDraw));
                place(newplant, newplant.getPosition());
            }
            else {
                int secondDraw = random.RandomInt(0, possible_infertile.size());
                Plant newplant = new Plant(possible_infertile.get(secondDraw));
                place(newplant, newplant.getPosition());
            }
        }
    }
    private ArrayList<ArrayList<Vector2d>> partition(Boundary normal, Boundary jungle){
        ArrayList<ArrayList<Vector2d>> result = new ArrayList<>();
        ArrayList<Vector2d> fertile = new ArrayList<>();
        ArrayList<Vector2d> infertile = new ArrayList<>();

        HashSet<Vector2d> spaces = normal.generateSpaces();
        HashSet<Vector2d> fertile_spaces = jungle.generateSpaces();
        fertile_spaces.addAll(vitalCorpses());


        for (Vector2d space : spaces){
            if (!plants.containsKey(space)){
                if(fertile_spaces.contains(space)){
                    fertile.add(space);
                }
                else infertile.add(space);
            }
        }

        result.add(fertile);
        result.add(infertile);
        return result;
    }

}
