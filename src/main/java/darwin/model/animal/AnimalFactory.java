package darwin.model.animal;

import darwin.model.Vector2d;
import darwin.util.AnimalState;
import darwin.util.Directions;

import java.util.HashSet;

public class AnimalFactory {
    public static AbstractAnimal createAnimal(Boolean crazy, int genomeLength,int initEnergy, Vector2d position){
        AnimalProperties properties = new AnimalProperties(new int[genomeLength], 0,new HashSet<AbstractAnimal>(), new HashSet<AbstractAnimal>(), 0, 0, Directions.NORTH, initEnergy, position, AnimalState.ALIVE);
        if(crazy)
            return new AnimalCrazy(properties);
        else
            return new Animal(properties);
    }
}
