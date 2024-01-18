package world.maps;

import world.basic.Vector2d;
import world.entities.Animal;

public class Globe extends AbstractWorldMap{
    public Globe(int width, int height, int grassNumber, int costOfReproduction, int plantsEnergy) {
        super(width, height, grassNumber, costOfReproduction, plantsEnergy);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.y() <= upperRight.y() && position.y() >= lowerLeft.y();
    }

    @Override
    public void move(Animal animal) {
        System.out.println(animals);
        Vector2d oldPosition = animal.position();
        Vector2d newPosition = animal.getNextMove();
        if(!canMoveTo(newPosition)) {
            animal.iteratePointer();
        }
        else if (newPosition.x() < lowerLeft.x()) {
            animal.move();
            animals.remove(oldPosition);
            animal.setPosition(new Vector2d(upperRight.x(), newPosition.y()));
            animals.put(animal.position(), animal);
        }
        else if (newPosition.x() > upperRight.x()) {
            animal.move();
            animals.remove(oldPosition);
            animal.setPosition(new Vector2d(lowerLeft.x(), newPosition.y()));
            animals.put(animal.position(), animal);
        }
        else {
            animal.move();
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
            mapChanged();
        }
    }
}
