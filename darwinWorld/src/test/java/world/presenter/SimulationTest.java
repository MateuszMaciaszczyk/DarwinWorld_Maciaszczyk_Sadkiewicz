package world.presenter;

import world.entities.Animal;
import world.maps.Globe;
import world.maps.WorldMap;
import world.basic.Vector2d;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    private Simulation simulation;
    private WorldMap map;
    private List<Animal> animals;
    private final int mapWidth = 10;
    private final int mapHeight = 10;
    private final int grassNumber = 5;
    private final int plantsEnergy = 20;
    private int reproductionEnergyCost = 10;


    @BeforeEach
    public void setUp() {
        map = new Globe(mapWidth, mapHeight, grassNumber, reproductionEnergyCost, plantsEnergy);

        simulation = new Simulation(10, map, 50, 8, 30, 20, 1, 3, 5, "default");

        animals = simulation.getAnimals();
        for (int i = 0; i < 10; i++) {
            animals.add(new Animal(new Vector2d(i, i), 50, new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 30, 20, 1, 3, "default"));
        }


    }

    @Test
    public void testFindStrongestAnimal() {
        Vector2d testPosition = new Vector2d(5, 5);
        Animal expectedStrongest = new Animal(testPosition, 100, new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 30, 20, 1, 3, "default");
        animals.add(expectedStrongest);

        Animal actualStrongest = simulation.findStrongestAnimal(testPosition, null);
        assertEquals(expectedStrongest, actualStrongest, "Should return the strongest animal at the position");
    }

    @Test
    public void testGetTwoStrongestAnimals() {
        Vector2d testPosition = new Vector2d(5, 5);
        Animal strongest = new Animal(testPosition, 100, new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 30, 20, 1, 3, "default");
        Animal secondStrongest = new Animal(testPosition, 90, new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 30, 20, 1, 3, "default");
        animals.add(strongest);
        animals.add(secondStrongest);

        List<Animal> strongestAnimals = simulation.getTwoStrongestAnimals(strongest);
        assertTrue(strongestAnimals.contains(strongest) && strongestAnimals.contains(secondStrongest), "Should return the two strongest animals");
    }
}
