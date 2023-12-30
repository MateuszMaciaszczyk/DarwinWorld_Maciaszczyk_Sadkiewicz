package world.entities;

import world.basic.MapDirection;
import world.maps.MoveValidator;
import world.basic.Vector2d;


public class Animal implements WorldElement {
    private Vector2d position;
    private int orientation;
    private int[] genes;
    private int energy;
    private int pointer;  // pointer to the current gene

    public int getOrientation() {
        return orientation;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public Vector2d getNextMove() {
        return this.position.add(MapDirection.toUnitVector(getNextOrientation()));
    }

    public void iteratePointer(){
        pointer = (pointer + 1) % genes.length;
    }

    public int getNextOrientation(){
        int direction = genes[pointer];
        return (orientation + direction) % 8;
    }

    public Animal(Vector2d position) {
        this.position = position;
        this.energy = 10;
        this.genes = new int[]{0, 0, 0, 0, 0, 0};
        this.orientation = (int)(Math.random() * 8);
        System.out.println(orientation);
    }

    private void randomGenes(int n){
        this.genes = new int[n];
        for(int i = 0; i < genes.length; i++){
            genes[i] = (int)(Math.random() * 8);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(energy);
    }

    public void move(MoveValidator validator) {
        this.position = getNextMove();
        this.orientation = getNextOrientation();
        pointer = (pointer + 1) % genes.length;
        energy--;
    }

    public void gainEnergy(int energy) {
        this.energy += energy;
    }

    public boolean isAt(Vector2d position) { return this.position.equals(position); }

    public Vector2d getPosition() { return position; }
}
