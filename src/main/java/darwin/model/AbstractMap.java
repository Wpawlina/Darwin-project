package darwin.model;

import darwin.model.animal.*;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.MyRandom;

import java.util.*;


abstract public class AbstractMap implements WorldMap{
    protected final Boundary mapBoundary;
    protected final Boundary jungleBoundary;
    protected final MapInitialProperties mapInitialProperties;

    protected final  HashMap<Vector2d, HashSet<AbstractAnimal>> living_animals = new HashMap<>();
    protected final ArrayList<AbstractAnimal> animals = new ArrayList<>();
    protected final  HashMap<Vector2d, Plant> plants = new HashMap<>();

    private int plantCount = 0;
    private int emptyPositionsCount;
    public AbstractMap(MapInitialProperties mapInitialProperties, Boundary mapBoundary, Boundary jungleBoundary){
        this.mapBoundary = mapBoundary;
        this.jungleBoundary = jungleBoundary;
        this.mapInitialProperties = mapInitialProperties;
        emptyPositionsCount = mapBoundary.size();
    }

    @Override
    public void place(AbstractAnimal animal, Vector2d position) {
        if (canMoveTo(position)){
            if (living_animals.containsKey(position)){
                living_animals.get(position).add(animal);
            }
            else{
                living_animals.put(position, new HashSet<>(Set.of(animal)));
            }
            animals.add(animal);
            emptyPositionsCount --;
        }
    }

    public void place(Plant plant, Vector2d position){
        if (!plants.containsKey(position)){
            plants.put(position, plant);
        }
    }

    @Override
    public void move(AbstractAnimal animal) {
        Vector2d old_p = animal.getPosition();
        living_animals.get(old_p).remove(animal);
        animal.move(this);
        Vector2d new_p = animal.getPosition();
        living_animals.get(new_p).add(animal);
    }

    @Override
    public void eat() {
        for (Plant plant: plants.values()){
            Vector2d position = plant.getPosition();
            HashSet<AbstractAnimal> willing = living_animals.get(position);
            if (!willing.isEmpty()) {
                plants.remove(plant.getPosition());
                plantCount --;
                int no = willing.size();
                Iterator<AbstractAnimal> iter = willing.iterator();
                if (no == 1){
                    iter.next().eat(mapInitialProperties.plantEnergy());
                }
                else {
                    conflict(iter).eat(mapInitialProperties.plantEnergy());
                }
            }
        }
    }

    private AbstractAnimal conflict(Iterator<AbstractAnimal> iter) {
        int max_energy = 0;
        int max_age = 0;
        int max_children = 0;
        ArrayList<AbstractAnimal> winners = new ArrayList<>();
        while (iter.hasNext()){
            AbstractAnimal rival = iter.next();
            int rival_energy = rival.getProperties().getEnergy();
            if (rival_energy >= max_energy) {
                max_energy = rival_energy;
                int rival_age = rival.getProperties().getAge();
                if (rival_age >= max_age){
                    max_age = rival_age;
                    int rival_children = rival.getProperties().getChildren().size();
                    if (rival_children >= max_children){
                        max_children = rival_children;
                        winners.add(rival);
                    }
                }
            }
        }
        MyRandom myRandom = new MyRandom();
        return winners.get(myRandom.RandomInt(0, winners.size()));
    }

    @Override
    public void reproduce(int min, int max, int sufficientEnergy, int energyCost) {
        for(HashSet<AbstractAnimal> space : living_animals.values()){
            int spaceSize = space.size();
            if (spaceSize > 1){
                Iterator<AbstractAnimal> iter = space.iterator();
                ArrayList<AbstractAnimal> parents = new ArrayList<>();
                ArrayList<AbstractAnimal> toPlace = new ArrayList<>();
                while (iter.hasNext()){
                    AbstractAnimal current = iter.next();
                    if (current.getProperties().getEnergy() >= sufficientEnergy){
                        parents.add(current);
                    }
                    if (parents.size() == 2){
                        AbstractAnimal child = parents.get(0).createChildren(parents.get(1), energyCost, min, max);
                        toPlace.add(child);
                        parents.clear();
                    }
                }

                for(AbstractAnimal animal : toPlace){
                    place(animal, animal.getPosition());
                }
            }
        }
    }


    @Override
    public void subtractEnergy() {
        for(HashSet<AbstractAnimal> space : living_animals.values()){
            for(AbstractAnimal animal : space){
                animal.subtractEnergy(1);
                if (animal.getProperties().getState().equals(AnimalState.RECENTLY_DIED)){
                    animal.setState(AnimalState.DEAD);
                    living_animals.get(animal.getPosition()).remove(animal);
                }
                else if (animal.getProperties().getEnergy() <= 0) {
                    animal.setState(AnimalState.RECENTLY_DIED);
                }
            }
        }
    }

    @Override
    public void spawnPlants() {
        ArrayList<ArrayList<Vector2d>> possible = partition(mapBoundary.generateSpaces(), jungleBoundary.generateSpaces());
        spawnPlantNo(mapInitialProperties.spawnPlantPerDay(), possible);
    }

    public void initialSpawnPlants(int i) {
        ArrayList<ArrayList<Vector2d>> possible = partition(mapBoundary.generateSpaces(), jungleBoundary.generateSpaces());
        spawnPlantNo(i, possible);
    }

    protected void spawnPlantNo(int no, ArrayList<ArrayList<Vector2d>> possible){
        MyRandom random = new MyRandom();
        ArrayList<Vector2d> possible_fertile = possible.get(0);
        ArrayList<Vector2d> possible_infertile = possible.get(1);
        for(int i = 0; i < no; i++){
            float firstDraw = random.RandomFrac();
            if(firstDraw <0.2){
               int secondDraw = random.RandomInt(0, possible_fertile.size());
               Plant newplant = new Plant(possible_fertile.remove(secondDraw));
               place(newplant, newplant.getPosition());
            }
            else {
                int secondDraw = random.RandomInt(0, possible_infertile.size());
                Plant newplant = new Plant(possible_infertile.remove(secondDraw));
                place(newplant, newplant.getPosition());
            }
        }
    }

    public void spawnAnimalNo(int no, boolean crazy, int genomeLength, int initialEnergy){
        MyRandom random = new MyRandom();
        ArrayList<Vector2d> possible = mapBoundary.generateArraySpaces();

       for(int i = 0; i < no; i++){
                int draw = random.RandomInt(0, possible.size());
                AbstractAnimal animal = AnimalFactory.createAnimal(crazy, genomeLength, initialEnergy, possible.remove(draw));
                place(animal, animal.getPosition());
            }
    }

    protected ArrayList<ArrayList<Vector2d>> partition(HashSet<Vector2d> spaces, HashSet<Vector2d> fertile_spaces){
        ArrayList<ArrayList<Vector2d>> result = new ArrayList<>();
        ArrayList<Vector2d> fertile = new ArrayList<>();
        ArrayList<Vector2d> infertile = new ArrayList<>();

        for (Vector2d space : spaces){
            if (!plants.containsKey(space)){
                if(fertile_spaces.contains(space)){
                    fertile.add(space);
                }
                else infertile.add(space);
            }
        }

        result.add(fertile);
        result.add(infertile);
        return result;
    }

    @Override
    public int countPlant() {return plants.size();}

    @Override
    public int countEmptyPositions() {
        int result = 0;
        ArrayList<Vector2d> spaces = mapBoundary.generateArraySpaces();
        for (Vector2d space : spaces){
            if (!plants.containsKey(space) && !living_animals.containsKey(space)) result ++;
        }
        return result;
    }

    @Override
    public boolean canMoveTo(Vector2d position){
        return (this.mapBoundary.lowerLeft().getY() <= position.getY()
                && position.getY() <= this.mapBoundary.upperRight().getY());
    }

    @Override
    public Vector2d correctPosition(Vector2d position){
        int x = position.getX();
        int left = mapBoundary.lowerLeft().getX();
        int right = mapBoundary.upperRight().getX();

        if (x < left) x = x - left + 1 + right;
        else if (x > right) x = x - right - 1 + left;

        return new Vector2d(x, position.getY());
    }

    public ArrayList<AbstractAnimal> getAnimals(){return animals;}

    public HashSet<AbstractAnimal> getAnimalsOnSpace(Vector2d position){return living_animals.get(position);}

    public Plant getPlantOnSpace(Vector2d position){return plants.get(position);}
}
