package ooszigetek;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static final String[] ISLANDS = {
        "szigetek-spiral",
        "szigetek",
        "szigetek_apro",
        "szigetek_kavalkad",
        "szigetek_oriasi",
        "szigetek_osszevonni",
        "worldmap",
        "full",
        "grid",
        "szigetek-sok"
    };

    public static void main(String[] args) {
        String island = getIslandSrc(ISLANDS[9]);

        try {
            BufferedReader br = new BufferedReader(new FileReader(island));
            MapParser parser = new MapParser(br);
            parser.parseMap();

            System.out.println(String.format("Number of islands: %d", parser.getIslandCount()));
            System.out.println(parser);

            parser.getIslands().forEach(
                    (key, value) -> System.out.printf("Island ID: %d, Coors:\n%s\n", key, value.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getIslandSrc(String island) {
        return "szigetek/" + island + ".txt";
    }

}
