package darwin.model;

import darwin.model.animal.AbstractAnimal;

public interface WorldMap extends MoveValidator{
    public void place(AbstractAnimal animal, Vector2d position);

    public  void move (AbstractAnimal animal);


    public void eat();


    public void reproduce(int min , int max,int sufficientEnergy,int energyCost);

    public void subtractEnergy();

    public void spawnPlants();


    public int countPlant();

    public int countEmptyPositions();
}
