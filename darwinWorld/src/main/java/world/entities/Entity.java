package world.entities;

import world.basic.Vector2d;
public abstract class Entity {
    private Vector2d position;

    public Entity(Vector2d initialPosition) {
        this.position = initialPosition;
    }
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }
    public Vector2d getPosition() {
        return this.position;
    }
    public void setPosition(Vector2d position) {
        this.position = position;
    }
}
