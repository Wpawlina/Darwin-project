package darwin.model.animal;

import darwin.model.Vector2d;
import darwin.util.AnimalState;
import darwin.util.Directions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class AnimalCrazyTest {
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
    AnimalProperties properties2 = new AnimalProperties(new int[]{0},
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
    void newIndexAfterMove() {
        AnimalCrazy animal = new AnimalCrazy(properties);
        int index = animal.newIndexAfterMove(0);
        assertTrue(0 <= index);
        assertTrue(index >= animal.getProperties().getGenome().length);
    }
    @Test
    void createChildren() {
        AbstractAnimal mother = new Animal(properties);
        AbstractAnimal father = new Animal(properties2);
        AbstractAnimal child = mother.createChildren(father, 1, 1,1);

        assertEquals(1, mother.getProperties().getEnergy());
        assertEquals(1, father.getProperties().getEnergy());
        assertEquals(2, child.getProperties().getEnergy());
        assert child.getProperties().getParents().contains(mother);
        assert child.getProperties().getParents().contains(father);
        assert mother.getProperties().getChildren().contains(child);
        assert father.getProperties().getChildren().contains(child);
        assertEquals(1, child.getProperties().getGenome().length);
        assertNotEquals(0,child.getProperties().getGenome()[0]);
    }
}