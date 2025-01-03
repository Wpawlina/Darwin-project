package darwin.model;

import darwin.util.AnimalState;
import darwin.util.Directions;
import darwin.util.SimulationConfig;



public class AbstractAnimal implements  WorldElement{
    private final AnimalProperties properties;


    public AbstractAnimal(AnimalProperties properties) {
        this.properties = properties;
    }

    public void mutate(int min ,int max){

    }

    public boolean move(WorldMap map){

    }

    public void eat(int energy){
        this.properties.setEnergy(this.properties.getEnergy()+energy);

    }

    public void addChildren(AbstractAnimal animal){

    }

    public void subtractEnergy(int energy){
        this.properties.setEnergy(this.properties.getEnergy()-energy);
    }
    public AbstractAnimal createChildren(AbstractAnimal partner,int energyCost){

    }

    public int[] getGenome(){

    }

    public void setState(AnimalState state)
    {

    }









}
