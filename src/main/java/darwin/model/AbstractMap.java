package darwin.model;

import darwin.model.animal.AbstractAnimal;
import darwin.util.Boundary;

import java.util.*;


abstract public class AbstractMap implements WorldMap{
    private final Boundary mapBoundary;
    private final Boundary jungleBoundary;
    private final MapInitialProperties mapInitialProperties;

    private final  HashMap<Vector2d, HashSet<AbstractAnimal>> animals = new HashMap<>();
    private final  HashMap<Vector2d, Plant> plants = new HashMap<>();

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
            }
        }
    }

    @Override
    public void move(AbstractAnimal animal) {
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition = oldPosition + animal.getProperties().direction.toUnitVector();
        Vector2d correctedPosition = correctPosition(newPosition);

        animals.get(oldPosition).remove(animal);
        place(animal, correctedPosition);
    }

    @Override
    public void eat() {
        for (Plant plant: plants.values()){
            Vector2d position = plant.getPosition();
            HashSet<AbstractAnimal> willing = animals.get(position);
            if (!willing.isEmpty()) {
                int no = willing.size();
                if (no == 1){
                }
                else {

                }
            }
        }
    }

    public AbstractAnimal conflict(AbstractAnimal[] willing) {
        return willing[0];//niezaimplementowane
    }

    @Override
    public void reproduce(int min, int max, int sufficientEnergy, int energyCost) {

    }

    @Override
    public void subtractEnergy() {

    }

    @Override
    public void spawnPlants() {

    }

    @Override
    public int countPlant() {
        return 0;
    }

    @Override
    public int countEmptyPositions() {
        return 0;
    }

    @Override
    public boolean canMoveTo(Vector2d position){
        return (this.mapBoundary.lowerLeft().getX() <= position.getX()
                && position.getX() < this.mapBoundary.upperRight().getX()
                && this.mapBoundary.lowerLeft().getY() <= position.getY()
                && position.getY() < this.mapBoundary.upperRight().getY());
    }

    @Override
    public Vector2d correctPosition(Vector2d position){
        if (!canMoveTo(position)){
            int x = position.getX();
            int y = position.getY();
            int left = mapBoundary.lowerLeft().getX();
            int right = mapBoundary.upperRight().getX();
            int top = mapBoundary.upperRight().getY();
            int bottom = mapBoundary.lowerLeft().getY();

            if (x < left) x = x - left + right;
            else if (x >= right) x = x - right + left;

            if (x < bottom) x = x - bottom + top;
            else if (x >= top) x = x - top + bottom;

            position = new Vector2d(x, y);
        }
        return position;
    }










}
