import darwin.util.MyRandom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyRandomTest {

    @Test
    public void RandomIntTest() {
        MyRandom myRandom = new MyRandom();
        int min = 0;
        int max = 10;
        int randomInt = myRandom.RandomInt(min, max);
        assertTrue( randomInt >= min && randomInt < max);
    }

    @Test
    public void RandomFracTest() {
        MyRandom myRandom = new MyRandom();
        float randomFrac = myRandom.RandomFrac();
        assertTrue( randomFrac >= 0 && randomFrac < 1);
    }
}
