package world.maps;

import world.basic.Vector2d;
import world.entities.Animal;
import world.entities.Grass;
import world.entities.WorldElement;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected Map<Vector2d,Animal> animals = new HashMap<>();
    protected Map<Vector2d, Grass> plants = new HashMap<>();
    protected ArrayList<MapChangeListener> mapChangeListeners = new ArrayList<>();
    protected Vector2d lowerLeft;
    protected Vector2d upperRight;
    protected UUID id;
    protected int costOfReproduction;

    public AbstractWorldMap(int width, int height, int grassNumber, int costOfReproduction) {
        this.id = UUID.randomUUID();
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
        generateGrass(grassNumber);
    }

    private void generateGrass(int grassNumber) {
        for (int i = 0; i < grassNumber; i++) {
            this.placePlants();
        }
    }

    @Override
    public int getGrassesNumber() {
        return plants.size();
    }

    @Override
    public int getFreeSpaceNumber() {
        int freeSpaceNumber = 0;
        for (int i = 0; i < upperRight.getX(); i++) {
            for (int j = 0; j < lowerLeft.getY(); j++) {
                if (!isOccupied(new Vector2d(i, j))) {
                    freeSpaceNumber++;
                }
            }
        }
        return freeSpaceNumber;
    }

    @Override
    public void place(Animal animal){
        animals.put(animal.getPosition(), animal);
        mapChanged("Animal placed at: " + animal.getPosition());
    }

    public void placePlants() {
        Vector2d grassPosition = getPreferredPosition();
        while (plants.containsKey(grassPosition)) {
            grassPosition = getPreferredPosition();
        }
        plants.put(grassPosition, new Grass(grassPosition, 5));
    }

    @Override
    public void eatGrass(Animal animal) {
        Vector2d position = animal.getPosition();
        if (plants.containsKey(position)) {
            animal.gainEnergy(plants.get(position).getEnergy());
            plants.remove(position);
            mapChanged("Animal ate grass at: " + position);
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position) || plants.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (this.isOccupied(position)) {
            if (animals.containsKey(position)) {
                return animals.get(position);
            }
        }
        return plants.get(position);
    }

    public Set<WorldElement> getElements() { return new HashSet<>(animals.values()); }

    public abstract Boundary getCurrentBounds();

    public void addMapChangeListener(MapChangeListener listener) {
        mapChangeListeners.add(listener);
    }

    public void removeMapChangeListener(MapChangeListener listener) {
        mapChangeListeners.remove(listener);
    }

    public synchronized void mapChanged(String message) {
        for (MapChangeListener listener : mapChangeListeners) {
            listener.mapChanged(this, message);
        }
    }

    public UUID getId() {
        return id;
    }

    public Vector2d getPreferredPosition() {
        int preferredHeight = (upperRight.getY() - lowerLeft.getY()) / 5; // 20% of the map height
        int preferredLowerY = lowerLeft.getY() + 2 * preferredHeight;
        int preferredUpperY = upperRight.getY() - 2 * preferredHeight;

        double chance = Math.random();
        int x, y;

        if (chance <= 0.8) {
            // Generate position in the preferred area
            x = (int)(Math.random() * (upperRight.getX() - lowerLeft.getX() + 1)) + lowerLeft.getX();
            y = (int)(Math.random() * (preferredUpperY - preferredLowerY + 1)) + preferredLowerY;
        } else {
            // Generate position outside the preferred area
            if (Math.random() < 0.5) {
                // Generate position in the lower non-preferred area
                x = (int)(Math.random() * (upperRight.getX() - lowerLeft.getX() + 1)) + lowerLeft.getX();
                y = (int)(Math.random() * (preferredLowerY - lowerLeft.getY() + 1)) + lowerLeft.getY();
            } else {
                // Generate position in the upper non-preferred area
                x = (int)(Math.random() * (upperRight.getX() - lowerLeft.getX() + 1)) + lowerLeft.getX();
                y = (int)(Math.random() * (upperRight.getY() - preferredUpperY + 1)) + preferredUpperY;
            }
        }

        return new Vector2d(x, y);
    }

    @Override
    public void removeDeadAnimal(Animal animal) {
        animals.remove(animal.getPosition());
        animal.die();
    }
}
