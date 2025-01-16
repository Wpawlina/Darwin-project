package darwin;

import darwin.model.*;
import darwin.model.animal.AbstractAnimal;
import darwin.model.animal.Animal;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.SimulationConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import static java.lang.Thread.sleep;

public class Simulation  implements  Runnable{
    private final WorldMap map;
    private final SimulationConfig config;
    private int day;
    private AbstractAnimal trackedAnimal;
    private boolean isRunning = false;



    public Simulation(SimulationConfig config){
        this.config = config;
        MapInitialProperties mapConfig = new MapInitialProperties(
                config.spawnPlantPerDay(),
                config.plantEnergy());
        Boundary mapBoundary = new Boundary(
                new Vector2d(0, 0),
                new Vector2d(config.mapWidth(), config.mapHeight()));

        int equator = config.mapHeight() / 2;
        int width = config.mapHeight() / 5;
        Boundary jungleBoundary = new Boundary(
                new Vector2d(0, equator - width),
                new Vector2d(config.mapWidth(), equator + width));



        if(config.optionE()) {
            this.map = new MapWithOptionE(mapConfig, mapBoundary, jungleBoundary);
        }
        else {
            this.map = new Map(mapConfig, mapBoundary, jungleBoundary);
        }
    }

    public void setTrackedAnimal(AbstractAnimal animal){
        this.trackedAnimal = animal;
    }

    public Optional<AbstractAnimal> getTrackedAnimal(){
        return Optional.ofNullable(trackedAnimal);
    }

    public void setRunning(boolean running){
        isRunning = running;
    }

    public boolean isRunning(){
        return isRunning;
    }

    public int getDay(){return day;}

    public WorldMap getMap(){
        return map;
    }

    @Override
    public  void run(){
        day = 0;
        isRunning = true;
        map.spawnAnimalNo(
                config.initialAnimalSpawn(),
                config.crazy(),
                config.genomeLength(),
                config.initialAnimalEnergy());

        map.initialSpawnPlants(config.initialPlantSpawn());
        int simulationSpeed = map.getMapBoundary().size()/5;

        while(map.anybodyAlive()){
            if(isRunning)
            {
                try {
                    sleep(simulationSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                day++;
                System.out.println(day);
                map.increaseAge();
                for(AbstractAnimal animal : map.getAliveAnimals()){
                    System.out.println(animal.getPosition().toString() + animal.getProperties().getEnergy());
                }
                map.subtractEnergy(day);

                for (AbstractAnimal animal : new ArrayList<>(map.getAnimals())){
                    if (animal.getProperties().getState().equals(AnimalState.ALIVE)){
                        map.move(animal);
                    }
                }

                map.eat();

                map.reproduce(
                        config.minMutation(),
                        config.maxMutation(),
                        config.ReproductionEnergySufficient(),
                        config.ReproductionEnergyCost());

                map.spawnPlants();

                map.notifyObservers();

            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        map.notifyAllAnimalsDead();
    }

}
