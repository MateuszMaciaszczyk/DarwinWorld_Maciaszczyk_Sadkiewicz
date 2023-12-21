package world;

import world.basic.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirection> parse(List<Integer> args) {
        List<MoveDirection> result = new ArrayList<>();

        for (Integer arg : args) {
            switch (arg) {
                case 0 -> result.add(MoveDirection.FORWARD);
                case 1 -> result.add(MoveDirection.FORWARD_RIGHT);
                case 2 -> result.add(MoveDirection.RIGHT);
                case 3 -> result.add(MoveDirection.BACKWARD_RIGHT);
                case 4 -> result.add(MoveDirection.BACKWARD);
                case 5 -> result.add(MoveDirection.BACKWARD_LEFT);
                case 6 -> result.add(MoveDirection.LEFT);
                case 7 -> result.add(MoveDirection.FORWARD_LEFT);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }

        return result;
    }
}