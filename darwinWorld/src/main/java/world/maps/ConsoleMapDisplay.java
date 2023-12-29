package world.maps;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updateCount = 0;

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {
            updateCount++;
            System.out.println("Map Id: " + worldMap.getId());
            System.out.println("Update #" + updateCount + ": " + message);
            System.out.println(worldMap.toString());
        }
    }
}
