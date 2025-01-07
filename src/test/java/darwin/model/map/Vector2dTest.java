package darwin.model.map;

import darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {

    @Test
    public void equalsSameVectors() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 =v1;

        assertEquals(v1, v2);
    }
    @Test
    public void equalsDifferentVectorsSameValues() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(1, 2);

        assertEquals(v1, v2);
    }
    @Test
    public void equalsDifferentVectorsDifferentValues() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(2, 1);

        assertNotEquals(v1, v2);
    }

    @Test
    public void toStringVector() {
        Vector2d v1 = new Vector2d(1, 2);

        assertEquals("(1,2)", v1.toString());
    }

    @Test
    public void precedesTrue() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(2, 3);

        assertTrue(v1.precedes(v2));
    }
    @Test
    public void precedesFalse() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(0, 1);
        assertFalse(v1.precedes(v2));
    }

    @Test
    public void followsTrue() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(2, 3);


        assertTrue(v2.follows(v1));

    }

    @Test
    public void followsFalse() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(0, 1);

        assertFalse(v2.follows(v1));

    }

    @Test
    public void upperRight() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(2, 3);

        assertEquals((new Vector2d(2, 3)), v1.upperRight(v2));
    }

    @Test
    public void lowerLeft() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(2, 3);

        assertEquals((new Vector2d(1, 2)), v1.lowerLeft(v2));
    }

    @Test
    public void add()
    {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(2, 3);

        Vector2d result = v1.add(v2);

        assertEquals((new Vector2d(3, 5)), result);
    }

    @Test
    public void subtract()
    {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(2, 3);

        Vector2d result = v1.subtract(v2);

        assertEquals((new Vector2d(-1, -1)), result);
    }

    @Test
    public  void opposite()
    {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d result = v1.opposite();
        assertEquals((new Vector2d(-1, -2)), result);
    }




}