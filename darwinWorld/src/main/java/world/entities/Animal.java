package world.entities;

import world.basic.MapDirection;
import world.maps.MoveValidator;
import world.basic.Vector2d;


public class Animal implements WorldElement {
    private Vector2d position;
    private int[] genes;
    private int energy;
    private int pointer;  // pointer to the current gene

    public Animal(Vector2d position) {
        this.position = position;
        this.energy = 10;
        this.genes = new int[]{0, 0, 1, 2, 2};
    }

    private void randomGenes(int n){
        this.genes = new int[n];
        for(int i = 0; i < genes.length; i++){
            genes[i] = (int)(Math.random() % n);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(pointer);
    }

    public void move(MoveValidator validator) {
        int direction = genes[pointer];
        this.position = this.position.add(MapDirection.toUnitVector(direction));
        pointer = (pointer + 1) % genes.length;
        energy--;
    }

    private void moveHelper(MoveValidator validator, Vector2d unitVector) {
        Vector2d new_position = this.position.add(unitVector);
        if(validator.canMoveTo(new_position)) {
            this.position = new_position;
        }
    }

    public boolean isAt(Vector2d position) { return this.position.equals(position); }

    public Vector2d getPosition() { return position; }
}
