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

    @Test
    void newIndexAfterMove() {
        AnimalCrazy animal = new AnimalCrazy(properties);
        int index = animal.newIndexAfterMove(0);
        assertTrue(0 <= index);
        assertTrue(index >= animal.getProperties().getGenome().length);
    }
}