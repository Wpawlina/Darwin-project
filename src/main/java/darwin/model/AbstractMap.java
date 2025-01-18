package darwin.model;

import darwin.model.animal.*;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.MyRandom;

import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;


abstract public class AbstractMap implements WorldMap{
    protected final Boundary mapBoundary;
    protected final Boundary jungleBoundary;
    protected final MapInitialProperties mapInitialProperties;

    protected final  HashMap<Vector2d, HashSet<AbstractAnimal>> living_animals = new HashMap<>();
    protected final ArrayList<AbstractAnimal> animals = new ArrayList<>();
    protected final  HashMap<Vector2d, Plant> plants = new HashMap<>();
    protected final ArrayList<MapChangeListener> observers = new ArrayList<>();

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

    public void registerObserver(MapChangeListener observer){
        this.observers.add(observer);

    }

    public void notifyObservers(){
        for(MapChangeListener observer : observers){
            observer.mapChanged(this);
        }
    }

    public void notifyAllAnimalsDead(){
        for(MapChangeListener observer : observers){
            observer.AllAnimalsDead(this);
        }
    }


    public void place(Plant plant, Vector2d position){
        if (!plants.containsKey(position)){
            plants.put(position, plant);
            plantCount++;
        }
    }

    @Override
    public void move(AbstractAnimal animal) {
        Vector2d old_p = animal.getPosition();
        living_animals.get(old_p).remove(animal);
        animal.move(this);
        Vector2d new_p = animal.getPosition();
        if (living_animals.containsKey(new_p)){
            living_animals.get(new_p).add(animal);
        }
        else{
            living_animals.put(new_p, new HashSet<>(Set.of(animal)));
        }
    }

    @Override
    public void eat() {
        for (Plant plant: new ArrayList<>(plants.values())){
            Vector2d position = plant.getPosition();
            HashSet<AbstractAnimal> willing = living_animals.get(position);
            if (willing != null && !willing.isEmpty()) {
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
                        System.out.println("reproduced");
                    }
                }

                for(AbstractAnimal animal : toPlace){
                    place(animal, animal.getPosition());
                }
            }
        }
    }


    @Override
    public void subtractEnergy(int day) {
        HashMap<Vector2d, AbstractAnimal> toDelete = new HashMap<>();
        for(HashSet<AbstractAnimal> space : living_animals.values()){
            for(AbstractAnimal animal : space){
                animal.subtractEnergy(1);
                if (animal.getProperties().getState().equals(AnimalState.RECENTLY_DIED)){
                    animal.setState(AnimalState.DEAD, day);
                    toDelete.put(animal.getPosition(), animal);
                }
                else if (animal.getProperties().getEnergy() <= 0) {
                    animal.setState(AnimalState.RECENTLY_DIED,day);
                }
            }
        }
        for(Map.Entry<Vector2d, AbstractAnimal> entry : toDelete.entrySet()){
            living_animals.get(entry.getKey()).remove(entry.getValue());
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
            if(firstDraw >0.2 && !possible_fertile.isEmpty()){
               int secondDraw = random.RandomInt(0, possible_fertile.size());
               Plant newplant = new Plant(possible_fertile.remove(secondDraw));
               place(newplant, newplant.getPosition());
            }
            else if(!possible_infertile.isEmpty()) {
                int secondDraw = random.RandomInt(0, possible_infertile.size());
                Plant newplant = new Plant(possible_infertile.remove(secondDraw));
                place(newplant, newplant.getPosition());
            }
        }
    }

    @Override
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
            if (!plants.containsKey(space) && ( !living_animals.containsKey(space) || living_animals.get(space).isEmpty() )) result ++;

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

    @Override
    public ArrayList<AbstractAnimal> getAnimals(){return animals;}

    @Override
    public ArrayList<AbstractAnimal> getAliveAnimals(){
        return new ArrayList<>(
                animals.stream().filter(
                        animal -> animal.getProperties().getState().equals(AnimalState.ALIVE)).toList()
        );
    }

    @Override
    public Optional<HashSet<AbstractAnimal>> getAnimalsOnSpace(Vector2d position){return Optional.ofNullable(living_animals.get(position));}

    @Override
    public Optional<Plant> getPlantOnSpace(Vector2d position){return Optional.ofNullable( plants.get(position));}

    @Override
    public boolean anybodyAlive(){
        for(AbstractAnimal animal : animals){
            if (animal.getProperties().getState().equals(AnimalState.ALIVE)) return true;
        }
        return false;
    }

    @Override
    public void increaseAge(){
        animals.stream().filter(
                animal -> animal.getProperties().getState().equals(AnimalState.ALIVE)
        ).forEach(AbstractAnimal::age);

    }

    @Override
    public Boundary getMapBoundary(){return mapBoundary;}

    @Override
    public Boundary getJungleBoundary(){return jungleBoundary;}


    @Override
    public int getMaxEnergy(){
        return animals.stream().filter(
                animal -> animal.getProperties().getState().equals(AnimalState.ALIVE)
        ).mapToInt(animal->animal.getProperties().getEnergy()).max().orElse(1);
    }

    @Override
    public Optional<AbstractAnimal> getFirstAnimalOnSpace(Vector2d position){
        HashSet<AbstractAnimal> animals = living_animals.get(position);
        if (animals != null && !animals.isEmpty()){
            return Optional.of(animals.stream().max(Comparator.comparingInt(animal->animal.getProperties().getEnergy())).get());
        }
        return Optional.empty();
    }

    @Override
    public int[] getMostPopularGenome()
    {
        return animals.stream().map(animal->animal.getProperties().getGenome()).collect(Collectors.groupingBy(
                genome -> genome, Collectors.counting()
        )).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(new int[]{});
    }

    @Override
    public String showMostPopularGenome() {
        int[] genome = getMostPopularGenome();
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < genome.length; i++){
            result.append(genome[i]);
            if (i != genome.length - 1) result.append(", ");
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public int getAverageEnergy(){
        return (int) animals.stream().filter(
                animal -> animal.getProperties().getState().equals(AnimalState.ALIVE)
        ).mapToInt(animal->animal.getProperties().getEnergy()).average().orElse(0);
    }

    @Override
    public int getAverageLifeSpan(){
        return (int) animals.stream().filter(
                animal -> animal.getProperties().getState().equals(AnimalState.DEAD)|| animal.getProperties().getState().equals(AnimalState.RECENTLY_DIED)
        ).mapToInt(animal->animal.getProperties().getAge()).average().orElse(0);
    }

    @Override
    public double getAverageChildren(){
        return  animals.stream().filter(
                animal -> animal.getProperties().getState().equals(AnimalState.ALIVE)
        ).mapToInt(AbstractAnimal::countChildren).average().orElse(0);
    }


}
