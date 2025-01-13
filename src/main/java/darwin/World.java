package darwin;

import darwin.util.Directions;
import darwin.util.SimulationConfig;

public class World {

    public static void main(String[] args) {
        SimulationConfig testConfig = new SimulationConfig(
                10,
                10,
                true,
                2,
                2,
                1,
                2,
                5,
                4,
                2,
                1,
                4,
                true,
                10
        );

        Simulation simulation = new Simulation(testConfig);
        simulation.run();
    }
}
