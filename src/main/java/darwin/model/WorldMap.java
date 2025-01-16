package darwin.model;

import darwin.model.animal.AbstractAnimal;
import darwin.presenter.MapPresenter;
import darwin.util.Boundary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public interface WorldMap extends MoveValidator{
    void place(AbstractAnimal animal, Vector2d position);

    void place(Plant plant, Vector2d position);

    void move (AbstractAnimal animal);

    void eat();

    void reproduce(int min , int max,int sufficientEnergy,int energyCost);

    void subtractEnergy(int day);

    void spawnPlants();

    void initialSpawnPlants(int i);

    void spawnAnimalNo(int no, boolean crazy, int genomeLength, int initialEnergy);

    void increaseAge();

    int countPlant();

    int countEmptyPositions();

    ArrayList<AbstractAnimal> getAnimals();

    ArrayList<AbstractAnimal> getAliveAnimals();

    Optional<HashSet<AbstractAnimal>> getAnimalsOnSpace(Vector2d position);

    Optional<Plant> getPlantOnSpace(Vector2d position);

    boolean anybodyAlive();

    Boundary getMapBoundary();

    Boundary getJungleBoundary();


    int getMaxEnergy();

    Optional<AbstractAnimal> getFirstAnimalOnSpace(Vector2d position);


   int[] getMostPopularGenome();

   String showMostPopularGenome();

   int getAverageEnergy();

   int getAverageLifeSpan();

   double getAverageChildren();


    void registerObserver(MapChangeListener observer);

    void notifyObservers();

    void notifyAllAnimalsDead();
}
