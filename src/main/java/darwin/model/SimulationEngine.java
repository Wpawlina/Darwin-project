package darwin.model;

import darwin.Simulation;

import java.util.concurrent.ExecutorService;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    final Simulation simulation;
    private ArrayList<Thread> threads = new ArrayList<>();


    public  SimulationEngine(Simulation simulations) {
        this.simulation = simulations;
    }





    public void runAsync() throws InterruptedException
    {


        this.threads.add(new Thread(this.simulation));

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : this.threads) {
            thread.join();
        }


    }







}
