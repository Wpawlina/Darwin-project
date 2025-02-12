package darwin;

import darwin.util.SimulationConfig;

public class World {

    public static void main(String[] args) {
        SimulationConfig testConfig = new SimulationConfig(
                10,
                10,
                true,
                2,
                10,
                1,
                10,
                5,
                3,
                2,
                1,
                4,
                true,
                10,
                false
        );

        Simulation simulation = new Simulation(testConfig);
        try {
            simulation.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
