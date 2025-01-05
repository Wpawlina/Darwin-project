package darwin.model;

import darwin.model.animal.AbstractAnimal;
import darwin.model.animal.Animal;
import darwin.model.animal.AnimalCrazy;
import darwin.model.animal.AnimalProperties;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.MyRandom;

import java.util.*;


abstract public class AbstractMap implements WorldMap{
    protected final Boundary mapBoundary;
    protected final Boundary jungleBoundary;
    protected final MapInitialProperties mapInitialProperties;

    protected final  HashMap<Vector2d, HashSet<AbstractAnimal>> animals = new HashMap<>();
    protected final  HashMap<Vector2d, Plant> plants = new HashMap<>();

    private int plantCount = 0;
    private int emptyPositionsCount = 0;
    public AbstractMap(MapInitialProperties mapInitialProperties, Boundary mapBoundary, Boundary jungleBoundary){
        this.mapBoundary = mapBoundary;
        this.jungleBoundary = jungleBoundary;
        this.mapInitialProperties = mapInitialProperties;
    }

    @Override
    public void place(AbstractAnimal animal, Vector2d position) {
        if (canMoveTo(position)){
            if (animals.containsKey(position)){
                animals.get(position).add(animal);
            }
            else{
                animals.put(position, new HashSet<>(Set.of(animal)));
                emptyPositionsCount ++;
            }
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
        animals.get(old_p).remove(animal);
        animal.move(this);
        Vector2d new_p = animal.getPosition();
        animals.get(new_p).add(animal);
    }

    @Override
    public void eat() {
        for (Plant plant: plants.values()){
            Vector2d position = plant.getPosition();
            HashSet<AbstractAnimal> willing = animals.get(position);
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
        for(HashSet<AbstractAnimal> space : animals.values()){
            int spaceSize = space.size();
            if (spaceSize > 1){
                Iterator<AbstractAnimal> iter = space.iterator();
                while (iter.hasNext()){
                    AbstractAnimal mother = iter.next();
                    int motherEnergy = mother.getProperties().getEnergy();
                    if (motherEnergy >= sufficientEnergy && iter.hasNext()){
                        AbstractAnimal father = iter.next();
                        int fatherEnergy = father.getProperties().getEnergy();
                        if (fatherEnergy >= sufficientEnergy) {
                            Vector2d position = father.getPosition();
                            AbstractAnimal child = mother.createChildren(father, energyCost, min, max);
                            this.place(child, position);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void subtractEnergy() {
        for(HashSet<AbstractAnimal> space : animals.values()){
            for(AbstractAnimal animal : space){
                animal.subtractEnergy(1);
                if (animal.getProperties().getEnergy() <= 0) {
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

    public void spawnPlantNo(int no, ArrayList<ArrayList<Vector2d>> possible){
        MyRandom random = new MyRandom();
        ArrayList<Vector2d> possible_fertile = possible.get(0);
        ArrayList<Vector2d> possible_infertile = possible.get(1);
        for(int i = 0; i < no; i++){
            float firstDraw = random.RandomFrac();
            if(firstDraw <0.2){
               int secondDraw = random.RandomInt(0, possible_fertile.size());
               Plant newplant = new Plant(possible_fertile.get(secondDraw));
               place(newplant, newplant.getPosition());
            }
            else {
                int secondDraw = random.RandomInt(0, possible_infertile.size());
                Plant newplant = new Plant(possible_infertile.get(secondDraw));
                place(newplant, newplant.getPosition());
            }
        }
    }

    public void spawnAnimalNo(int no, boolean crazy, int genomeLength){
        MyRandom random = new MyRandom();
        ArrayList<Vector2d> possible = mapBoundary.generateArraySpaces();

        for(int i = 0; i < no; i++){
                int draw = random.RandomInt(0, possible.size());
                if(crazy){
                    AbstractAnimal animal = new AnimalCrazy(genomeLength, possible.get(draw));
                }
                else {
                    AbstractAnimal animal = new Animal(genomeLength, possible.get(draw));
                }
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
    public int countPlant() {
        return plants.size();
    }

    @Override
    public int countEmptyPositions() {
        return mapBoundary.size() - countPlant();
    }

    @Override
    public boolean canMoveTo(Vector2d position){
        return (this.mapBoundary.lowerLeft().getY() <= position.getY()
                && position.getY() < this.mapBoundary.upperRight().getY());
    }

    @Override
    public Vector2d correctPosition(Vector2d position){
        int x = position.getX();
        int y = position.getY();
        int left = mapBoundary.lowerLeft().getX();
        int right = mapBoundary.upperRight().getX();
        int top = mapBoundary.upperRight().getY();
        int bottom = mapBoundary.lowerLeft().getY();

        if (x < left) x = x - left + right;
        else if (x >= right) x = x - right + left;

        return new Vector2d(x, y);
    }
}
