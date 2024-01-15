package world.presenter;

import world.basic.Vector2d;
import world.entities.Animal;
import world.maps.Boundary;
import world.maps.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation extends Thread{
    private final List<Animal> animals = new ArrayList<>();
    private final WorldMap map;
    private final List<Animal> childs = new ArrayList<>();
    private int energy;
    private int genesNumber;
    private int animalAmount;
    private int grassAmount;
    private int energyToReproduce;
    private int costOfReproduction;
    private int minGeneMutation;
    private int maxGeneMutation;
    private int numberOfSpawningPlants;

    public Simulation(int animalAmount, WorldMap map, int energy, int genesNumber, int energyToReproduce, int costOfReproduction, int minGeneMutation, int maxGeneMutation, int numberOfSpawningPlants) {
        this.map = map;
        this.energy = energy;
        this.genesNumber = genesNumber;
        this.energyToReproduce = energyToReproduce;
        this.costOfReproduction = costOfReproduction;
        this.minGeneMutation = minGeneMutation;
        this.maxGeneMutation = maxGeneMutation;
        this.numberOfSpawningPlants = numberOfSpawningPlants;
        initializeAnimals(animalAmount);
    }

    public int getAnimalAmount() {
        this.animalAmount = animals.size();
        return animalAmount;
    }

    public int getGrassAmount() {
        this.grassAmount = map.getGrassesNumber();
        return grassAmount;
    }

    public int getFreeSpace() {
        return map.getFreeSpaceNumber();
    }

    public int getAverageEnergy(){  //TODO
        int sum = 0;
        return 0;
    }

    private void initializeAnimals(int animalAmount) {
        Boundary worldBoundary = map.getCurrentBounds();
        Random random = new Random();
        for (int i = 0; i < animalAmount; i++) {
            int x = random.nextInt(worldBoundary.upperRight().getX());
            int y = random.nextInt(worldBoundary.upperRight().getY());
            Vector2d randomPosition = new Vector2d(x, y);
            Animal animal = new Animal(randomPosition, energy, genesNumber, energyToReproduce, costOfReproduction);
            //System.out.println(animal);
            map.place(animal);
            animals.add(animal);
        }
    }

    private void breedAnimals() {
        List <Vector2d> positionsReserved = new ArrayList<>();

        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).canBreed()) {
                for (int j = i + 1; j < animals.size(); j++) {
                    if (animals.get(j).canBreed() && animals.get(i).getPosition().equals(animals.get(j).getPosition()) && !positionsReserved.contains(animals.get(i).getPosition())) {
                        List<Animal> strongestAnimals = getTwoStrongestAnimals(animals.get(j), animals.get(i));
                        positionsReserved.add(strongestAnimals.get(0).getPosition());
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
                map.removeDeadAnimal(animals.get(i));
                animals.remove(animals.get(i));
            }
        }
    }

    private List<Animal> getTwoStrongestAnimals(Animal animal1, Animal animal2) {
        Animal strongest1 = animal1;
        Vector2d position = animal1.getPosition();
        for (int i = 1; i < animals.size(); i++) {
            if (animals.get(i).getPosition().equals(position) && animals.get(i) != animal1) {
                if (animals.get(i).getEnergy() == strongest1.getEnergy()) {
                    if (animals.get(i).getAge() == strongest1.getAge()) {
                        if (animals.get(i).getChilds() == strongest1.getChilds()) {
                            if (Math.random() <= 0.5) {
                                strongest1 = animals.get(i);
                            }
                        }

                        if (animals.get(i).getChilds() > strongest1.getChilds()) {
                            strongest1 = animals.get(i);
                        }
                    }

                    if (animals.get(i).getAge() > strongest1.getAge()) {
                        strongest1 = animals.get(i);
                    }
                }
                if (animals.get(i).getEnergy() > strongest1.getEnergy()) {
                    strongest1= animals.get(i);
                }
            }

        }

        Animal strongest2 = animal2;

        for (int i = 1; i < animals.size(); i++) {
            if (animals.get(i).getPosition().equals(position) && animals.get(i) != animal1 && animals.get(i) != strongest1) {
                if (animals.get(i).getEnergy() == strongest2.getEnergy()) {
                    if (animals.get(i).getAge() == strongest2.getAge()) {
                        if (animals.get(i).getChilds() == strongest2.getChilds()) {
                            if (Math.random() <= 0.5) {
                                strongest2 = animals.get(i);
                            }
                        }

                        if (animals.get(i).getChilds() > strongest2.getChilds()) {
                            strongest2 = animals.get(i);
                        }
                    }

                    if (animals.get(i).getAge() > strongest2.getAge()) {
                        strongest2 = animals.get(i);
                    }
                }
                if (animals.get(i).getEnergy() > strongest2.getEnergy()) {
                    strongest2= animals.get(i);
                }
            }
        }
        List<Animal> strongestAnimals = new ArrayList<>();
        animals.add(strongest1);
        animals.add(strongest2);
        return strongestAnimals;
    }

    private void generateGrass() {
        for (int i = 0; i < numberOfSpawningPlants; i++) {
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

    public void run() {
        int i = 0;
        while (true) {
            try {
                Thread.sleep(3000);
                removeDeadAnimals();
                moveAnimals();
                eatGrass();
                breedAnimals();
                generateGrass();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Animal> getAnimals() { return animals; }
}
