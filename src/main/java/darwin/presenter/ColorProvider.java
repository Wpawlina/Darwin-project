package darwin.presenter;

import javafx.scene.paint.Paint;

import java.awt.*;

public class ColorProvider{


    public static Paint getJungleColor() {
        return Paint.valueOf("#006D06");
    }


    public  static Paint getSteppeColor() {
        return Paint.valueOf("#8CB353");
    }


    public static Paint getAnimalColor(int energy, int maxEnergy) {
        if(energy < maxEnergy/4){
            return Paint.valueOf("#EAB676");
        }
        if(energy < maxEnergy/2){
            return Paint.valueOf("#E28743");
        }
        if(energy < 3*maxEnergy/4){
            return Paint.valueOf("#873E23");
        }
        return Paint.valueOf("#21130D");
    }


    public  static  Paint getPlantColor() {
        return  Paint.valueOf("#288F12");
    }


}
