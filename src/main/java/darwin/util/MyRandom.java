package darwin.util;

import java.util.Random;

public class MyRandom {
    Random random = new Random();

    public int RandomInt(int min, int max){
        return min + this.random.nextInt(max - min);
    }
    public float RandomFrac(){
        return this.random.nextFloat();
    }
}
