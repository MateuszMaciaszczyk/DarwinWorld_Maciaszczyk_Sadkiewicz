package world.maps;

import world.basic.Vector2d;
import world.entities.Animal;

public class Globe extends AbstractWorldMap{
    public Globe(int width, int height, int grassNumber, int costOfReproduction) {
        super(width, height, grassNumber, costOfReproduction);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.getY() <= upperRight.getY() && position.getY() >= lowerLeft.getY();
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeft, upperRight);
    }

    @Override
    public void move(Animal animal) {
        System.out.println(animals);
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition = animal.getNextMove();
        if(!canMoveTo(newPosition)) {
            animal.iteratePointer();
        }
        else if (newPosition.getX() < lowerLeft.getX()) {
            animal.move(this);
            animals.remove(oldPosition);
            animal.setPosition(new Vector2d(upperRight.getX(), newPosition.getY()));
            animals.put(animal.getPosition(), animal);
        }
        else if (newPosition.getX() > upperRight.getX()) {
            animal.move(this);
            animals.remove(oldPosition);
            animal.setPosition(new Vector2d(lowerLeft.getX(), newPosition.getY()));
            animals.put(animal.getPosition(), animal);
        }
        else {
            animal.move(this);
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
            mapChanged("Animal moved from: " + oldPosition + " to: " + newPosition);
        }
    }
}
