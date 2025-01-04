package darwin.model.animal;

import darwin.model.Vector2d;
import darwin.util.AnimalState;
import darwin.util.Directions;

import java.util.HashSet;

public class AnimalProperties {
    private int[] genome;
    private int index;
    private final HashSet<AbstractAnimal> children;
    private final HashSet<AbstractAnimal> parents;
    private int age;
    private int deathDate;
    private Directions direction;
    private int energy;
    private int plantsEaten=0;
    private final Vector2d position;
    private AnimalState state;




    public AnimalProperties(int[] genome, int index, HashSet<AbstractAnimal> children, HashSet<AbstractAnimal> parents, int age, int deathDate, Directions direction, int energy, Vector2d position, AnimalState state) {
        this.genome = genome;
        this.index = index;
        this.children = children;
        this.parents = parents;
        this.age = age;
        this.deathDate = deathDate;
        this.direction = direction;
        this.energy = energy;
        this.position = position;
        this.state= state;
    }

    public int[] getGenome() {
        return genome;
    }

    public Vector2d getPosition() {
        return position;
    }

    public Directions getDirection() {
        return direction;
    }

    public int getIndex() {
        return index;
    }



    public int getEnergy() {
        return energy;
    }

    public int getAge(){return age;}

    public AnimalState getState() {return state;}

    public int getPlantsEaten() {
        return plantsEaten;
    }



    public HashSet<AbstractAnimal> getChildren() {
        return children;
    }

    public HashSet<AbstractAnimal> getParents() {
        return parents;
    }

    void setState(AnimalState state) {
        this.state = state;
    }

    void setIndex(int index) {
        this.index = index;
    }


    void setDirection(Directions direction) {
        this.direction = direction;
    }
    void setEnergy(int energy) {
        this.energy = energy;
    }

    void setPlantsEaten(int plantsEaten) {
        this.plantsEaten = plantsEaten;
    }
}
