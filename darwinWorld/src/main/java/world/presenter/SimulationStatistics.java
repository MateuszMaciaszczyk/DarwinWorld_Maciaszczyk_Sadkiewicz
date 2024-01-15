package world.presenter;

import java.util.List;

public class SimulationStatistics {
    private final Simulation simulation;
    private int day = 0;
    private int animalAmount = 0;
    private int grassAmount = 0;
    private int freeSpace = 0;
    private int averageEnergy = 0;
    private int averageLifeLength = 0;
    private int averageChildrenNumber = 0;
    private List<Integer> popularGenes;

    public SimulationStatistics(Simulation simulation) {
        this.simulation = simulation;
        this.animalAmount = simulation.getAnimalAmount();
        this.grassAmount = simulation.getGrassAmount();
        this.freeSpace = simulation.getFreeSpace();
        this.averageEnergy = simulation.getAverageEnergy();
        this.averageLifeLength = simulation.getAverageLifeLength();
        this.averageChildrenNumber = simulation.getAverageChildrenNumber();
        this.popularGenes = simulation.getPopularGenes();
    }
}
