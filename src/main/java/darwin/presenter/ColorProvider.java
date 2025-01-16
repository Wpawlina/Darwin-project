package darwin.presenter;

public class ColorProvider{


    public static   String getJungleColor() {
        return "#006D06";
    }


    public  static String getSteppeColor() {
        return "#8CB353";
    }


    public static String getAnimalColor(int energy, int maxEnergy) {
        if(energy < maxEnergy/4){
            return "#EAB676";
        }
        if(energy < maxEnergy/2){
            return "#E28743";
        }
        if(energy < 3*maxEnergy/4){
            return "#873E23";
        }
        return "#21130D";
    }


    public  static  String getPlantColor() {
        return  "#288F12";
    }


}
