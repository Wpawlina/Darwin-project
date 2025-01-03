package darwin.model;

import java.util.HashSet;

public class AnimalProperties {
    public int[] genome;
    public int index;
    public final HashSet<AbstractAnimal> children;
    public final HashSet<AbstractAnimal> parents;
    public int age;
    public int deathDate;

    public AnimalProperties(int[] genome, int index, HashSet<AbstractAnimal> children, HashSet<AbstractAnimal> parents, int age, int deathDate) {
        this.genome = genome;
        this.index = index;
        this.children = children;
        this.parents = parents;
        this.age = age;
        this.deathDate = deathDate;
    }
}
