package world.basic;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH,
    SOUTH_EAST,
    SOUTH_WEST,
    WEST,
    EAST;

    @Override
    public String toString() {
        return switch (this) {
            case EAST -> "Wschód";
            case WEST -> "Zachód";
            case NORTH -> "Północ";
            case NORTH_EAST -> "Północny wschód";
            case NORTH_WEST -> "Północny zachód";
            case SOUTH -> "Południe";
            case SOUTH_EAST -> "Południowy wschód";
            case SOUTH_WEST -> "Południowy zachód";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case EAST -> SOUTH_EAST;
            case WEST -> NORTH_WEST;
            case NORTH -> NORTH_EAST;
            case SOUTH -> SOUTH_WEST;
            case NORTH_EAST -> EAST;
            case NORTH_WEST -> NORTH;
            case SOUTH_EAST -> SOUTH;
            case SOUTH_WEST -> WEST;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case EAST -> NORTH_EAST;
            case WEST -> SOUTH_WEST;
            case NORTH -> NORTH_WEST;
            case SOUTH -> SOUTH_EAST;
            case NORTH_EAST -> NORTH;
            case NORTH_WEST -> WEST;
            case SOUTH_EAST -> EAST;
            case SOUTH_WEST -> SOUTH;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case EAST -> new Vector2d(1, 0);
            case WEST -> new Vector2d(-1, 0);
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case NORTH_WEST -> new Vector2d(-1, 1);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
        };
    }
}
