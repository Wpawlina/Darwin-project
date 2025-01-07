package darwin.model;

import darwin.model.animal.AbstractAnimal;

public interface WorldMap extends MoveValidator{
    void place(AbstractAnimal animal, Vector2d position);

    void place(Plant plant, Vector2d position);

    void move (AbstractAnimal animal);

    void eat();

    void reproduce(int min , int max,int sufficientEnergy,int energyCost);

    void subtractEnergy();

    void spawnPlants();

    void initialSpawnPlants(int i);

    int countPlant();

    int countEmptyPositions();
}
