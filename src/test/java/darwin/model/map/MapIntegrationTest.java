package darwin.model.map;

import darwin.model.*;
import darwin.model.animal.AbstractAnimal;
import darwin.model.animal.Animal;
import darwin.model.animal.AnimalFactory;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import darwin.util.Directions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class MapIntegrationTest {

    @Test
    public void placeAnimal() {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        AbstractAnimal animal = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(2, 2));
        map.place(animal, new Vector2d(2, 2));

        HashSet<AbstractAnimal> animalsOnSpace = map.getAnimalsOnSpace(new Vector2d(2, 2)).orElse(new HashSet<>());
        assertTrue(animalsOnSpace.contains(animal));

        ArrayList<AbstractAnimal> animals = map.getAnimals();
        assertTrue(animals.contains(animal));

    }

    @Test
    public void placePlants()
    {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        Plant plant = new Plant(new Vector2d(2,2));

        map.place(plant,plant.getPosition());


        assertEquals( plant,map.getPlantOnSpace(new Vector2d(2,2)).orElse(new Plant(new Vector2d(0,0))));

    }

    @Test
    public void AnimalEatsTest() {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0, 10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        AbstractAnimal animal = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(2, 2));
        map.place(animal, new Vector2d(2, 2));
        Plant plant = new Plant(new Vector2d(2, 2));
        map.place(plant, new Vector2d(2, 2));
        map.eat();
        assert map.countPlant() == 0;
        assert animal.getProperties().getEnergy() == 20;


        AbstractAnimal animal2 = AnimalFactory.createAnimal(false, 7, 20, new Vector2d(3, 3));
        AbstractAnimal animal3 = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(3, 3));

        map.place(animal2, new Vector2d(3, 3));
        map.place(animal3, new Vector2d(3, 3));
        Plant plant2 = new Plant(new Vector2d(3, 3));
        map.place(plant2, new Vector2d(3, 3));
        map.eat();
        assert map.countPlant() == 0;
        assert animal2.getProperties().getEnergy() == 30;
        assert animal3.getProperties().getEnergy() == 10;
    }


    @Test
    public void subtractEnergyTest() {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0, 10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        AbstractAnimal animal = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(2, 2));
        map.place(animal, new Vector2d(2, 2));
        AbstractAnimal animal2 = AnimalFactory.createAnimal(false, 7, 1, new Vector2d(3, 3));
        map.place(animal2, new Vector2d(3, 3));
        map.subtractEnergy(1);
        assert animal.getProperties().getEnergy() == 9;
        assert animal2.getProperties().getEnergy() == 0;
        assertEquals(animal2.getProperties().getState(), AnimalState.RECENTLY_DIED);
    }

    @Test
    public  void animalReproductionTest()
    {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        for (int i=0;i<5;i++)
        {
            Animal animal=(Animal) AnimalFactory.createAnimal(false,7,10,new Vector2d(2,2));
            map.place(animal,new Vector2d(2,2));
        }
        Animal animal=(Animal) AnimalFactory.createAnimal(false,7,5,new Vector2d(2,2));
        map.place(animal,new Vector2d(2,2));

        map.reproduce(0,0,7,6);

        HashSet<AbstractAnimal> animals = map.getAnimalsOnSpace(new Vector2d(2,2)).orElse(new HashSet<>());
        assertEquals( 8,animals.size());


        int energySum=0;
        for (AbstractAnimal abstractAnimal : animals) {
            energySum+=abstractAnimal.getProperties().getEnergy();
        }
        assertEquals(55,energySum);
    }


    @Test
    public void plantsCountTest() {

        MapInitialProperties mapInitialProperties = new MapInitialProperties(0, 10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        Plant plant = new Plant(new Vector2d(2, 2));
        map.place(plant, new Vector2d(2, 2));
        Plant plant2 = new Plant(new Vector2d(3, 3));
        map.place(plant2, new Vector2d(3, 3));
        assert map.countPlant() == 2;

    }

    @Test
    public void countEmptyPositionsTest() {

        MapInitialProperties mapInitialProperties = new MapInitialProperties(0, 10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        assert map.countEmptyPositions() == mapBoundary.size();

        AbstractAnimal animal = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(2, 2));
        map.place(animal, new Vector2d(2, 2));
        assert map.countEmptyPositions() == mapBoundary.size() - 1;

        Plant plant = new Plant(new Vector2d(3, 3));
        map.place(plant, new Vector2d(3, 3));

        assert map.countEmptyPositions() == mapBoundary.size() - 2;

    }


    @Test
    public void canMoveToTest() {

        MapInitialProperties mapInitialProperties = new MapInitialProperties(0, 10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        assertTrue(map.canMoveTo(new Vector2d(2, 2)));
        assertTrue(map.canMoveTo(new Vector2d(5, 3)));
        assertFalse(map.canMoveTo(new Vector2d(3, 5)));

    }

    @Test
    public void correctPositionTest()
    {
        MapInitialProperties mapInitialProperties=new MapInitialProperties(0,10);
        Boundary mapBoundary = new Boundary(new Vector2d(0,0),new Vector2d(4,4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0,1),new Vector2d(4,2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        Vector2d position = new Vector2d(2,2);
        Vector2d newPosition = map.correctPosition(position);
        assertEquals(position,newPosition);

        Vector2d position2 = new Vector2d(5,3);
        Vector2d newPosition2 = map.correctPosition(position2);
        assertEquals(new Vector2d(0,3),newPosition2);

        Vector2d position3 = new Vector2d(-1,3);
        Vector2d newPosition3 = map.correctPosition(position3);
        assertEquals(new Vector2d(4,3),newPosition3);

    }

    @Test
    public void spawnPlantsTest()
    {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(5,  10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        map.spawnPlants();

        HashSet<Vector2d> spaces = mapBoundary.generateSpaces();
        int plantsCount=0;
        for (Vector2d space : spaces) {
            if(map.getPlantOnSpace(space).isPresent())
            {
                plantsCount+=1;
            }
        }
        assertEquals(5,plantsCount);

    }

    @Test
    public void spawnAnimalNoTest()
    {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        map.spawnAnimalNo(5,false,7,10);

        assertEquals(5,map.getAnimals().size());

        HashSet<Vector2d> spaces = mapBoundary.generateSpaces();
        int animalCount=0;
        for (Vector2d space : spaces) {
            if(map.getAnimalsOnSpace(space).isPresent())
            {
                animalCount+=map.getAnimalsOnSpace(space).orElse(new HashSet<>()).size();
            }
        }

        assertEquals(5,animalCount);
    }

    @Test
    public void animalsMoveTest()
    {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);


        AbstractAnimal animal = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(1, 1));
        animal.getProperties().getGenome()[0]=0;

        AbstractAnimal animal2 = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(3, 3));
        animal2.getProperties().getGenome()[0]=0;

        map.place(animal, new Vector2d(1, 1));
        map.place(animal2, new Vector2d(3, 3));

        map.move(animal);
        map.move(animal2);

        assertEquals(new Vector2d(1,2),animal.getProperties().getPosition());
        assertEquals(new Vector2d(3,4),animal2.getProperties().getPosition());
        assertEquals(map.getAnimalsOnSpace(new Vector2d(1,2)).orElse(new HashSet<>()).size(),1);
        assertEquals(map.getAnimalsOnSpace(new Vector2d(3,4)).orElse(new HashSet<>()).size(),1);






    }

    @Test
    public void getMostPopularGenomeTest()
    {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);


        AbstractAnimal animal = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(1, 1));

        map.place(animal, new Vector2d(1, 1));

        assertEquals(animal.getProperties().getGenome(),map.getMostPopularGenome());



    }











}
