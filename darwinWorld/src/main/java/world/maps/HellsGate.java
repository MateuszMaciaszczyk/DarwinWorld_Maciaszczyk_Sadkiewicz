package world.maps;

import world.basic.Vector2d;
import world.entities.Animal;
import java.util.Random;

public class HellsGate extends AbstractWorldMap{
    private final Random random = new Random();

    public HellsGate(int width, int height, int grassNumber, int costOfReproduction) {
        super(width, height, grassNumber, costOfReproduction);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.getY() <= upperRight.getY() && position.getY() >= lowerLeft.getY() && position.getX() <= upperRight.getX() && position.getX() >= lowerLeft.getX();
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeft, upperRight);
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition = animal.getNextMove();

        if (!canMoveTo(newPosition)) {
            animal.iteratePointer();
            newPosition = getRandomPosition();
            animal.decreaseEnergy(costOfReproduction);
        }

        animals.remove(oldPosition);
        animal.move(this);
        animal.setPosition(newPosition);
        animals.put(animal.getPosition(), animal);

        mapChanged("Animal moved from: " + oldPosition + " to: " + newPosition);
    }

    private Vector2d getRandomPosition() {
        int x = (int)(Math.random() * (upperRight.getX() - lowerLeft.getX() + 1)) + lowerLeft.getX();
        int y = (int)(Math.random() * (upperRight.getY() - lowerLeft.getY() + 1)) + lowerLeft.getY();
        return new Vector2d(x, y);
    }
}
