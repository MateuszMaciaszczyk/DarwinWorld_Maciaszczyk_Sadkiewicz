package world.model;

import world.basic.Vector2d;
import world.entities.Animal;
import world.maps.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation extends Thread{
    private final List<Animal> animals = new ArrayList<>();
    private final WorldMap map;
    private final List<Integer> moves = new ArrayList<>();

    public Simulation(List<Integer> moves, List<Vector2d> vector_list, WorldMap map) throws PositionAlreadyOccupiedException {
        this.map = map;
        this.initializeAnimals(vector_list);
        this.moves.addAll(moves);
    }

    private void initializeAnimals(List<Vector2d> positionList) throws PositionAlreadyOccupiedException {
        for (Vector2d vector : positionList) {
            Animal animal = new Animal(vector);
            try {
                map.place(animal);
                animals.add(animal);
            }
            catch (PositionAlreadyOccupiedException ignored) {
                System.out.println(ignored.getMessage());
            }
        }
    }

    public void run() {
        int i = 0;
        for (int move : moves) {
            try {
                Thread.sleep(3000);
                map.move(animals.get(i % animals.size()));
                map.eatGrass(animals.get(i++ % animals.size()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Animal> getAnimals() { return animals; }
}
