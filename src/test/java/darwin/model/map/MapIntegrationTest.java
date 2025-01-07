package darwin.model.map;

import darwin.model.*;
import darwin.model.animal.AbstractAnimal;
import darwin.model.animal.AnimalFactory;
import darwin.util.AnimalState;
import darwin.util.Boundary;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class MapIntegrationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void placeAnimal() {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,     10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        AbstractAnimal animal = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(2, 2));
        map.place(animal, new Vector2d(2, 2));
        assert map.countEmptyPositions() == mapBoundary.size() - 1;
        try {
            Field animals = map.getClass().getSuperclass().getDeclaredField("animals");
            animals.setAccessible(true);

            HashMap<Vector2d, HashSet<AbstractAnimal>> animalsMap = (HashMap<Vector2d, HashSet<AbstractAnimal>>) animals.get(map);


            assertTrue(animalsMap.get(new Vector2d(2, 2)).contains(animal));
        } catch (NoSuchFieldException | IllegalAccessException e) {

            assert false;
        }
    }

    @Test
    public void AnimalEatsTest() {
        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
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
        map.subtractEnergy();
        assert animal.getProperties().getEnergy() == 9;
        assert animal2.getProperties().getEnergy() == 0;
        assertEquals(animal2.getProperties().getState(), AnimalState.RECENTLY_DIED);
    }


    @Test
    public void plantsCountTest() {

        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
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

        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
        Boundary mapBoundary = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Boundary jungleBoundary = new Boundary(new Vector2d(0, 1), new Vector2d(4, 2));
        Map map = new Map(mapInitialProperties, mapBoundary, jungleBoundary);

        assert map.countEmptyPositions() == mapBoundary.size();

        AbstractAnimal animal = AnimalFactory.createAnimal(false, 7, 10, new Vector2d(2, 2));
        map.place(animal, new Vector2d(2, 2));
       // assert map.countEmptyPositions() == mapBoundary.size() - 1;

        Plant plant = new Plant(new Vector2d(3, 3));
        map.place(plant, new Vector2d(3, 3));

       // assert map.countEmptyPositions() == mapBoundary.size() - 2;

    }


    @Test
    public void canMoveToTest() {

        MapInitialProperties mapInitialProperties = new MapInitialProperties(0,  10);
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





}
