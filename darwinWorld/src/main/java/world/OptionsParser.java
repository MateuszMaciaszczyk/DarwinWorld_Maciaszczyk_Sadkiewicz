package world;

import world.basic.MapDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<Integer> parse(String[] args) {
        List<Integer> result = new ArrayList<>();

        for (String arg : args) {
            switch (arg) {
                case "0" -> result.add(0);
                case "1" -> result.add(1);
                case "2" -> result.add(2);
                case "3" -> result.add(3);
                case "4" -> result.add(4);
                case "5" -> result.add(5);
                case "6" -> result.add(6);
                case "7" -> result.add(7);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }

        return result;
    }
}