package darwin.model.animal;

import darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalFactoryTest {
    Vector2d position = new Vector2d(5, 5);

    @Test
    void createAnimal() {
        AbstractAnimal animal = AnimalFactory.createAnimal(false, 1, 1, position);
        assertEquals(1, animal.getProperties().getGenome().length);
        assertEquals(1, animal.getProperties().getEnergy());
        assertEquals(position, animal.getPosition());
    }

    @Test
    void createAnimalCrazy() {
        AbstractAnimal animal = AnimalFactory.createAnimal(true, 1, 1, position);
        assertEquals(1, animal.getProperties().getGenome().length);
        assertEquals(1, animal.getProperties().getEnergy());
        assertEquals(position, animal.getPosition());
    }
}