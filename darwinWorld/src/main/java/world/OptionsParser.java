package world;

import world.basic.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirection> parse(String[] args) {
        List<MoveDirection> result = new ArrayList<>();

        for (String arg : args) {
            switch (arg) {
                case "f" -> result.add(MoveDirection.FORWARD);
                case "fr" -> result.add(MoveDirection.FORWARD_RIGHT);
                case "fl" -> result.add(MoveDirection.FORWARD_LEFT);
                case "b" -> result.add(MoveDirection.BACKWARD);
                case "br" -> result.add(MoveDirection.BACKWARD_RIGHT);
                case "bl" -> result.add(MoveDirection.BACKWARD_LEFT);
                case "r" -> result.add(MoveDirection.RIGHT);
                case "l" -> result.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }

        return result;
    }
}