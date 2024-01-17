package world.simulation;

import world.basic.Vector2d;
import world.entities.Animal;
import world.basic.Boundary;
import world.maps.WorldMap;
import world.statistics.SimulationStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation extends Thread{
    private final List<Animal> animals = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    private final WorldMap map;
    private final SimulationStatistics stats;
    private final List<Animal> childs = new ArrayList<>();
    private int energy;
    private int genesNumber;
    private int energyToReproduce;
    private int costOfReproduction;
    private int minGeneMutation;
    private int maxGeneMutation;
    private int numberOfSpawningPlants;
    private int day = 0;
    private int simulationLength;
    private String mutationVariant;
    private volatile boolean running = true;

    public Simulation(int animalAmount, WorldMap map, int energy, int genesNumber, int energyToReproduce, int costOfReproduction, int minGeneMutation, int maxGeneMutation, int numberOfSpawningPlants, String mutationVariant, int simulationLength) {
        this.map = map;
        this.energy = energy;
        this.genesNumber = genesNumber;
        this.energyToReproduce = energyToReproduce;
        this.costOfReproduction = costOfReproduction;
        this.minGeneMutation = minGeneMutation;
        this.maxGeneMutation = maxGeneMutation;
        this.numberOfSpawningPlants = numberOfSpawningPlants;
        this.mutationVariant = mutationVariant;
        this.simulationLength = simulationLength;
        initializeAnimals(animalAmount);
        this.stats = new SimulationStatistics(this, map, animals, deadAnimals, childs);
    }

    private void initializeAnimals(int animalAmount) {
        Boundary worldBoundary = map.getCurrentBounds();
        Random random = new Random();
        for (int i = 0; i < animalAmount; i++) {
            int x = random.nextInt(worldBoundary.upperRight().getX());
            int y = random.nextInt(worldBoundary.upperRight().getY());
            Vector2d randomPosition = new Vector2d(x, y);
            Animal animal = new Animal(randomPosition, energy, randomGenes(genesNumber), energyToReproduce, costOfReproduction, minGeneMutation, maxGeneMutation, mutationVariant);
            map.place(animal);
            animals.add(animal);
        }
    }

    private int[] randomGenes(int n){
        int[] genes = new int[n];
        for(int i = 0; i < n; i++){
            genes[i] = (int)(Math.random() * 8);
        }
        return genes;
    }

    private void breedAnimals() {
        List <Vector2d> positionsReserved = new ArrayList<>();

        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).canBreed()) {
                for (int j = i + 1; j < animals.size(); j++) {
                    if (animals.get(j).canBreed() && animals.get(i).position().equals(animals.get(j).position()) && !positionsReserved.contains(animals.get(i).position())) {
                        List<Animal> strongestAnimals = getTwoStrongestAnimals(animals.get(j));
                        positionsReserved.add(strongestAnimals.get(0).position());
                        System.out.println("Nastąpiło rozmnożenie!");
                        Animal parent1 = strongestAnimals.get(0);
                        Animal parent2 = strongestAnimals.get(1);
                        Animal child = parent1.breed(parent2);
                        child.setParents(parent1, parent2);
                        parent1.updateChilds();
                        parent2.updateChilds();
                        map.place(child);
                        childs.add(child);
                    }
                }
            }
        }
        animals.addAll(childs);
        childs.clear();
    }

    private void removeDeadAnimals() {
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getEnergy() <= 0) {
                System.out.println("Zwierzę zginęło!");
                deadAnimals.add(animals.get(i));
                map.removeDeadAnimal(animals.get(i));
                animals.remove(animals.get(i));
            }
        }
        if (animals.isEmpty()) {
            System.out.println("Wszystkie zwierzęta zginęły!");
            pauseSimulation();
        }
    }

    public Animal findStrongestAnimal(Vector2d position, Animal excludeAnimal) {
        Animal strongest = null;

        for (Animal animal : animals) {
            if (animal.position().equals(position) && animal != excludeAnimal) {
                if (strongest == null ||
                        animal.getEnergy() > strongest.getEnergy() ||
                        (animal.getEnergy() == strongest.getEnergy() && (
                                animal.getAge() > strongest.getAge() ||
                                        (animal.getAge() == strongest.getAge() && animal.getChilds() > strongest.getChilds()) ||
                                        (animal.getAge() == strongest.getAge() && animal.getChilds() == strongest.getChilds() && Math.random() <= 0.5)
                        ))) {
                    strongest = animal;
                }
            }
        }

        return strongest;
    }

    public List<Animal> getTwoStrongestAnimals(Animal animal1) {
        Vector2d position = animal1.position();
        Animal strongest1 = findStrongestAnimal(position, animal1);
        Animal strongest2 = findStrongestAnimal(position, strongest1);

        List<Animal> strongestAnimals = new ArrayList<>();
        if (strongest1 != null) {
            strongestAnimals.add(strongest1);
        }
        if (strongest2 != null && strongest2 != strongest1) {
            strongestAnimals.add(strongest2);
        }
        return strongestAnimals;
    }


    public int getDay() {
        return day;
    }

    private void generateGrass() {
        if (map.getFreeSpaceNumber() == 0) {
            return;
        }
        for (int i = 0; i < numberOfSpawningPlants; i++) {
            if (map.getFreeSpaceNumber() == 0) {
                return;
            }
            map.placePlants();
        }
    }

    private void moveAnimals() {
        for (Animal animal : animals) {
            map.move(animal);
        }
    }

    private void eatGrass() {
        for (Animal animal : animals) {
            map.eatGrass(animal);
        }
    }

    public List<Animal> getAnimals() { return animals; }

    public boolean isRunning() {
        return running;
    }

    public void pauseSimulation() {
        running = false;
    }

    public void resumeSimulation() {
        running = true;
        synchronized (this) {
            this.notify();
        }
    }

    public List<Animal> getDeadAnimals() {
        return deadAnimals;
    }

    public List<Animal> getChilds() {
        return childs;
    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (this) {
                    while (!running) {
                            this.wait();
                    }
                }
                Thread.sleep(simulationLength);
                stats.updateStatistics(this, map, animals, deadAnimals, childs);
                removeDeadAnimals();
                moveAnimals();
                eatGrass();
                breedAnimals();
                generateGrass();
                day++;

                if (animals.isEmpty()) {
                    System.out.println("All animals are dead. Ending simulation.");
                    break;
                }
            }
        } catch (InterruptedException e) {
                throw new RuntimeException(e);}
    }
}
