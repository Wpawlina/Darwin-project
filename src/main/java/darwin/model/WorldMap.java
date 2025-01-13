package darwin.model;

import darwin.model.animal.AbstractAnimal;

import java.util.ArrayList;
import java.util.HashSet;

public interface WorldMap extends MoveValidator{
    void place(AbstractAnimal animal, Vector2d position);

    void place(Plant plant, Vector2d position);

    void move (AbstractAnimal animal);

    void eat();

    void reproduce(int min , int max,int sufficientEnergy,int energyCost);

    void subtractEnergy();

    void spawnPlants();

    void initialSpawnPlants(int i);

    void spawnAnimalNo(int no, boolean crazy, int genomeLength, int initialEnergy);

    int countPlant();

    int countEmptyPositions();

    ArrayList<AbstractAnimal> getAnimals();

    HashSet<AbstractAnimal> getAnimalsOnSpace(Vector2d position);

    Plant getPlantOnSpace(Vector2d position);

    boolean anybodyAlive();
}
