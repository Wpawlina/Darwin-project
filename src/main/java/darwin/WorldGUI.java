package darwin;

import javafx.application.Application;

public class WorldGUI {
    public static void main(String[] args) {
        String[] arguments = new String[14];
        for(int i = 0; i < 14; i++) {
            arguments[i] = "10";
        }
        arguments[0] = "10";
        arguments[1] = "10";
        arguments[6]="20";



        Application.launch(MapApplication.class, arguments);
    }
}
