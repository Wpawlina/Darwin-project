package darwin;

import darwin.model.*;
import darwin.model.animal.AbstractAnimal;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.SimulationConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static java.lang.Thread.sleep;

public class Simulation  implements  Runnable{
    private final WorldMap map;
    private final SimulationConfig config;
    private int day;
    private AbstractAnimal trackedAnimal;
    private boolean isRunning = false;

    private  boolean stop=false;

    private CSVWriter statWriter;


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

    public void stop()
    {
        stop=true;
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

        if(config.exportStatistics()){
            File stat = new File("stat.csv");

            try{
                if(stat.delete()){
                    stat.createNewFile();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            statWriter = new CSVWriter(stat);
        }

        while(map.anybodyAlive()){
            if(isRunning)
            {
                try {
                    if(stop)
                    {
                        return;
                    }
                    sleep(50);


                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

                if(config.exportStatistics()){
                    statWriter.appendLine(
                            new String[]{
                                    String.valueOf(this.getDay()),
                                    String.valueOf(this.map.getAliveAnimals().size()),
                                    String.valueOf(this.map.countPlant()),
                                    String.valueOf(this.map.countEmptyPositions()),
                                    String.valueOf(this.map.showMostPopularGenome()),
                                    String.valueOf(this.map.getAverageEnergy()),
                                    String.valueOf(this.map.getAverageLifeSpan()),
                                    String.valueOf(Math.floor(this.map.getAverageChildren()*100)/100)
                            }
                    );
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
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        map.notifyAllAnimalsDead();
    }

}
