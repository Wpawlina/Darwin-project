package darwin.model.animal;

import darwin.model.Vector2d;
import darwin.model.WorldElement;
import darwin.util.AnimalState;
import darwin.util.Directions;

import java.util.HashSet;


abstract  public class AbstractAnimal implements WorldElement {
    protected final AnimalProperties properties;

    abstract public AbstractAnimal createChildren(AbstractAnimal partner, int energyCost, int min, int max);

    public AbstractAnimal(AnimalProperties properties) {
        this.properties = properties;
    }

     public boolean move(MoveValidator map){
         Vector2d oldPosition = this.getProperties().getPosition();
         Directions direction=this.getProperties().getDirection().changeDirection(this.getProperties().getGenome()[this.getProperties().getIndex()]);
         this.properties.setIndex(this.newIndexAfterMove(this.getProperties().getIndex()));
         Vector2d unitVector=direction.toUnitVector();
         Vector2d newPosition=oldPosition.add(unitVector);
         if(map.canMoveTo(newPosition)){
             Vector2d newPositionInMap=map.correctPosition(newPosition);
             this.properties.setPosition(newPositionInMap);
             this.properties.setDirection(direction);
             return true;
         }
         else{
             this.properties.setDirection(direction.oppositeDirection());
             return false;
         }

     };


    public void eat(int energy) {
        this.properties.setEnergy(this.properties.getEnergy() + energy);
        this.properties.setPlantsEaten(this.properties.getPlantsEaten() + 1);

    }



    public void subtractEnergy(int energy) {
        this.properties.setEnergy(this.properties.getEnergy() - energy);
    }




    public AnimalProperties getProperties() {
        return properties;
    }


    public void setState(AnimalState state)
    {
        this.properties.setState(state);
    }


    public Vector2d getPosition() {
        return this.properties.getPosition();
    }

    void addChildren(AbstractAnimal animal) {
        this.properties.getChildren().add(animal);
        HashSet<AbstractAnimal> parents = this.getProperties().getParents();
        for (AbstractAnimal parent : parents) {
            if (!parent.getProperties().getParents().contains(animal)) {
                parent.getProperties().getChildren().add(animal);
            }
        }
    }

    void mutate(int min, int max) {
        int[] genome = this.properties.getGenome();
        int numberOfGenesToMutate = (int) (Math.random() * (max - min + 1) + min);
        for (int i = 0; i < numberOfGenesToMutate; i++) {
            int index = (int) (Math.random() * genome.length);
            genome[index] = (int) (Math.random() * 8);
        }

    }



    abstract  protected int newIndexAfterMove(int index);

    protected AnimalProperties createAnimalProperties(AbstractAnimal partner, int energyCost) {
        int[] genome = this.properties.getGenome();
        int[] partnerGenome = partner.getProperties().getGenome();
        int[] childGenome = new int[8];
        int parent1Energy = this.properties.getEnergy();
        int parent2Energy = partner.getProperties().getEnergy();
        int totalEnergy = parent1Energy + parent2Energy;
        if (Math.random() < 0.5) {
            int genomsToGive = (int) Math.ceil((double) (parent1Energy / totalEnergy));
            for (int i = 0; i < genomsToGive; i++) {
                childGenome[i] = genome[i];
            }
            for (int i = genomsToGive; i < 8; i++) {
                childGenome[i] = partnerGenome[i];
            }


        } else {
            int genomsToGive = (int) Math.ceil((double) (parent2Energy / totalEnergy));
            for (int i = 0; i < genomsToGive; i++) {
                childGenome[i] = partnerGenome[i];
            }
            for (int i = genomsToGive; i < 8; i++) {
                childGenome[i] = genome[i];
            }
        }
        return new AnimalProperties(childGenome, 0, new HashSet<>(), new HashSet<>(), 0, 0, Directions.randomDirection(), 2 * energyCost, this.properties.getPosition(), AnimalState.ALIVE);
    }










}
