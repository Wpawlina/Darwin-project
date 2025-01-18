package darwin;

import javafx.application.Application;

public class WorldGUI {
    public static void main(String[] args) {
        String[] arguments = new String[14];
        for(int i = 0; i < 14; i++) {
            arguments[i] = "30";
        }
        arguments[0] = "350";
        arguments[1] = "350";
        arguments[6]="10000";
        arguments[7]="100";



        Application.launch(MenuApplication.class, null);
    }
}
