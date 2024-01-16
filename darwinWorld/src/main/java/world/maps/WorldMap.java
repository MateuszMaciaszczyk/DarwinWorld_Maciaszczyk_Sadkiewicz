package world.maps;

import world.entities.Animal;
import world.basic.Vector2d;
import world.entities.WorldElement;

import java.util.Set;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place an animal on the map.
     *
     * @param animal The animal to place on the map.
     */
    void place(Animal animal);

    void eatGrass(Animal animal);

    void removeDeadAnimal(Animal animal);

    int getGrassesNumber();

    int getFreeSpaceNumber();

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);

    Set<WorldElement> getElements();

    Boundary getCurrentBounds();

    void addMapChangeListener(MapChangeListener consoleMapDisplay);

    UUID getId();

    void placePlants();

    boolean hasGrassAt(Vector2d position);
}