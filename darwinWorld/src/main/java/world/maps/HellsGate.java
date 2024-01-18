package world.maps;

import world.basic.Vector2d;
import world.entities.Animal;

public class HellsGate extends AbstractWorldMap{

    public HellsGate(int width, int height, int grassNumber, int costOfReproduction, int plantsEnergy) {
        super(width, height, grassNumber, costOfReproduction, plantsEnergy);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.y() <= upperRight.y() && position.y() >= lowerLeft.y() && position.x() <= upperRight.x() && position.x() >= lowerLeft.x();
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.position();
        Vector2d newPosition = animal.getNextMove();

        if (!canMoveTo(newPosition)) {
            animal.iteratePointer();
            newPosition = getRandomPosition();
            animal.decreaseEnergy(costOfReproduction);
        }

        animals.remove(oldPosition);
        animal.move();
        animal.setPosition(newPosition);
        animals.put(animal.position(), animal);

        mapChanged();
    }

    private Vector2d getRandomPosition() {
        int x = (int)(Math.random() * (upperRight.x() - lowerLeft.x() + 1)) + lowerLeft.x();
        int y = (int)(Math.random() * (upperRight.y() - lowerLeft.y() + 1)) + lowerLeft.y();
        return new Vector2d(x, y);
    }
}
