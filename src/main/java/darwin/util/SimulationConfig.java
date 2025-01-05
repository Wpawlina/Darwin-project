package darwin.util;

public record SimulationConfig(
        int mapHeight,
        int mapWidth,
        boolean optionE,
        int spawnPlantPerDay,
        int initialPlantSpawn,
        int plantEnergy,
        int initialAnimalSpawn,
        int initialAnimalEnergy,
        int ReproductionEnergySufficient,
        int ReproductionEnergyCost,
        int minMutation,
        int maxMutation,
        boolean crazy,
        int genomeLength
) {
}
