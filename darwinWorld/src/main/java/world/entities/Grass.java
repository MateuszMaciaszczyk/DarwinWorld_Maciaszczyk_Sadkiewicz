package world.entities;

import world.basic.Vector2d;
import world.entities.WorldElement;

public class Grass implements WorldElement {
    private final Vector2d position;
    private int energy;

    public int getEnergy() {
        return energy;
    }

    public Grass(Vector2d position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }
}
