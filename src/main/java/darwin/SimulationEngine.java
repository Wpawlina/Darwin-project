package darwin;

import java.util.ArrayList;

public class SimulationEngine {
    final Simulation simulation;
    private final Thread thread ;


    public  SimulationEngine(Simulation simulations) {
        this.simulation = simulations;
    }





    public void runAsync() throws InterruptedException
    {


        this.thread=new Thread(this.simulation);

        this.thread.start();

        this.thread.join();


    }







}
