package darwin.model.animal;

import darwin.model.Map;
import darwin.model.MapInitialProperties;
import darwin.model.Vector2d;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.Directions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
class AbstractAnimalTest {
    MapInitialProperties mconfig = new MapInitialProperties(1,1);
    Boundary mapb = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
    Boundary junb = new Boundary(new Vector2d(0,4), new Vector2d(6,10));
    Vector2d position = new Vector2d(5, 5);
    AnimalProperties properties = new AnimalProperties(new int[]{0},
            0,
            new HashSet<>(),
            new HashSet<>(),
            0,
            0,
            Directions.NORTH,
            1,
            position,
            AnimalState.ALIVE);


    @Test
    void move() {
        Map map = new Map(mconfig, mapb, junb);
        AbstractAnimal animal = new Animal(properties);
        map.place(animal, new Vector2d(5, 5));
        assertTrue(animal.move(map));
        assertEquals(new Vector2d(5,6), animal.getPosition());
    }

    @Test
    void moveCrazy() {
        Map map = new Map(mconfig, mapb, junb);
        AbstractAnimal animalCrazy = new AnimalCrazy(properties);
        map.place(animalCrazy, new Vector2d(5, 5));
        assertTrue(animalCrazy.move(map));
        assertEquals(new Vector2d(5,6), animalCrazy.getPosition());
    }

    @Test
    void eat() {
        AbstractAnimal animal = new Animal(properties);
        animal.eat(1);
        assertEquals(2, animal.getProperties().getEnergy());
        assertEquals(1, animal.getProperties().getPlantsEaten());
    }

    @Test
    void eatCrazy() {
        AbstractAnimal animal = new AnimalCrazy(properties);
        animal.eat(1);
        assertEquals(2, animal.getProperties().getEnergy());
        assertEquals(1, animal.getProperties().getPlantsEaten());
    }

    @Test
    void subtractEnergy() {
        AbstractAnimal animal = new Animal(properties);
        animal.eat(1);
        animal.subtractEnergy(1);
        assertEquals(1, animal.getProperties().getEnergy());
        assertEquals(1, animal.getProperties().getPlantsEaten());
    }

    @Test
    void subtractEnergyCrazy() {
        AbstractAnimal animal = new AnimalCrazy(properties);
        animal.eat(1);
        animal.subtractEnergy(1);
        assertEquals(1, animal.getProperties().getEnergy());
        assertEquals(1, animal.getProperties().getPlantsEaten());
    }

    @Test
    void getProperties() {
        AbstractAnimal animal = new Animal(properties);
        assertEquals(properties, animal.getProperties());
    }

    @Test
    void getPropertiesCrazy() {
        AbstractAnimal animal = new AnimalCrazy(properties);
        assertEquals(properties, animal.getProperties());
    }

    @Test
    void setState() {
        AbstractAnimal animal = new Animal(properties);
        animal.setState(AnimalState.DEAD);
        assertEquals(AnimalState.DEAD, animal.getProperties().getState());
    }

    @Test
    void setStateCrazy() {
        AbstractAnimal animal = new AnimalCrazy(properties);
        animal.setState(AnimalState.DEAD);
        assertEquals(AnimalState.DEAD, animal.getProperties().getState());
    }

    @Test
    void getPosition() {
        AbstractAnimal animal = new Animal(properties);
        assertEquals(position, animal.getPosition());
    }

    @Test
    void getPositionCrazy() {
        AbstractAnimal animal = new AnimalCrazy(properties);
        assertEquals(position, animal.getPosition());
    }

    @Test
    void addChildren() {
        AbstractAnimal grandpa = new Animal(properties);
        AbstractAnimal dad = new Animal(properties);
        AbstractAnimal child = new Animal(properties);

        grandpa.addChildren(dad);
        dad.addChildren(child);
        assertTrue(grandpa.getProperties().getChildren().contains(dad));
        assertTrue(grandpa.getProperties().getChildren().contains(child));
        assertTrue(dad.getProperties().getChildren().contains(child));
    }

    @Test
    void addChildrenCrazy() {
        AbstractAnimal grandpa = new AnimalCrazy(properties);
        AbstractAnimal dad = new AnimalCrazy(properties);
        AbstractAnimal child = new AnimalCrazy(properties);

        grandpa.addChildren(dad);
        dad.addChildren(child);
        assertTrue(grandpa.getProperties().getChildren().contains(dad));
        assertTrue(grandpa.getProperties().getChildren().contains(child));
        assertTrue(dad.getProperties().getChildren().contains(child));
    }

    @Test
    void mutate() {
        AbstractAnimal animal = new Animal(properties);
        int[] oldGenome = animal.getProperties().getGenome();
        animal.mutate(1,1);
        assertEquals(1, animal.getProperties().getGenome().length);
    }

    @Test
    void mutateCrazy() {
        AbstractAnimal animal = new AnimalCrazy(properties);
        int[] oldGenome = animal.getProperties().getGenome();
        animal.mutate(1,1);
        assertEquals(1, animal.getProperties().getGenome().length);
    }

    @Test
    void createChildren() {
    }
}