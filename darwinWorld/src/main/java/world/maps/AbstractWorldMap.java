package world.maps;

import world.basic.Vector2d;
import world.entities.Animal;
import world.entities.Grass;
import world.entities.WorldElement;
import world.model.PositionAlreadyOccupiedException;
import world.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected Map<Vector2d, Animal> animals = new HashMap<>();
    protected Map<world.basic.Vector2d, Grass> plants = new HashMap<>();
    protected ArrayList<world.maps.MapChangeListener> mapChangeListeners = new ArrayList<>();
    protected final MapVisualizer mapVisualizer;
    protected Vector2d lowerLeft;
    protected Vector2d upperRight;
    protected UUID id;

    public AbstractWorldMap() {
        this.mapVisualizer = new MapVisualizer(this);
        this.id = UUID.randomUUID();
    }

    @Override
    public void place(Animal animal) throws PositionAlreadyOccupiedException {
        if (!canMoveTo(animal.getPosition())) {
            throw new PositionAlreadyOccupiedException(animal.getPosition());
        }
        animals.put(animal.getPosition(), animal);
        mapChanged("Animal placed at: " + animal.getPosition());
    }

    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animals.remove(animal.getPosition());
        animal.move(this);
        animals.put(animal.getPosition(), animal);
        if (!oldPosition.equals(animal.getPosition())) {
            mapChanged("Animal moved to: " + animal.getPosition());
        }
        else {
            System.out.println("Animal did not move");
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        boolean canMove = position.follows(lowerLeft) && position.precedes(upperRight) && !animals.containsKey(position);
        if (!canMove) {
            System.out.println("Animal cannot move to: " + position);
        }
        return canMove;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    public synchronized String toString() {
        Boundary boundary = getCurrentBounds();
        return mapVisualizer.draw(boundary.lowerLeft(), boundary.upperRight());
    }

    public Set<WorldElement> getElements() { return new HashSet<>(animals.values()); }

    public abstract Boundary getCurrentBounds();

    public void addMapChangeListener(MapChangeListener listener) {
        mapChangeListeners.add(listener);
    }

    public void removeMapChangeListener(MapChangeListener listener) {
        mapChangeListeners.remove(listener);
    }

    private synchronized void mapChanged(String message) {
        for (MapChangeListener listener : mapChangeListeners) {
            listener.mapChanged(this, message);
        }
    }

    public UUID getId() {
        return id;
    }
}