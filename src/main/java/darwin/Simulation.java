package darwin;

import darwin.model.*;
import darwin.model.animal.AbstractAnimal;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.SimulationConfig;

public class Simulation {
    private final AbstractMap map;
    private final SimulationConfig config;

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

    void run(){
        map.spawnAnimalNo(
                config.initialAnimalSpawn(),
                config.crazy(),
                config.genomeLength(),
                config.initialAnimalEnergy());

        map.initialSpawnPlants(config.initialPlantSpawn());

    for(int i = 0; i<10; i++){
            map.subtractEnergy();

            for (AbstractAnimal animal : map.getAnimals()){
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
        }
    }

}
