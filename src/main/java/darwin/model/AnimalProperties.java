package darwin.model;

import darwin.util.Directions;

import java.util.HashSet;

public class AnimalProperties {
    public int[] genome;
    public int index;
    public final HashSet<AbstractAnimal> children;
    public final HashSet<AbstractAnimal> parents;
    public int age;
    public int deathDate;
    private Directions direction;
    private int energy;
    private int plantsEaten=0;




    public AnimalProperties(int[] genome, int index, HashSet<AbstractAnimal> children, HashSet<AbstractAnimal> parents, int age, int deathDate, Directions direction, int energy) {
        this.genome = genome;
        this.index = index;
        this.children = children;
        this.parents = parents;
        this.age = age;
        this.deathDate = deathDate;
        this.direction = direction;
        this.energy = energy;
    }
}
