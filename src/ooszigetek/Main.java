package ooszigetek;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    /**
     * String array containing the names of the island map files without the
     * extension.
     */
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
        int index = args.length > 0 ? Integer.parseInt(args[0]): 6;
        
        for (int i = 0; i< ISLANDS.length; i++) {
        String island = getIslandSrc(ISLANDS[i]);

        try {
            BufferedReader br = new BufferedReader(new FileReader(island));
            MapParser parser = new MapParser(br);
            parser.parseMap();

            System.out.println(String.format("Number of islands: %d", parser.getIslandCount()));
            System.out.println(String.format("Map size: %d", parser.getCache().length * parser.getCache()[0].length));
            System.out.println();
            //int id = 1;

            //System.out.printf("Island %d:\n", id);
            //printIsland(parser, id);
        } catch (IOException e) {
            e.printStackTrace();
        }}
    }

    /**
     * Prints an island to the console in its original 'ocean', based on the id.
     * 
     * @param parser the {@code MapParser} that parsed the map
     * @param id the id the island has
     */
    public static void printIsland(MapParser parser, int id) {
        for (int[] row : parser.getCache()) {
            for (int j = 0; j < row.length; j++) {
                if (row[j] == id) {
                    System.out.print("o");
                } else {
                    System.out.print("~");
                }
            }
            System.out.println();
        }
    }

    /**
     * Returns a relative path to the map file.
     * 
     * @param island the name of the island map file without the extension
     * @return "szigetek/" + island + ".txt"
     */
    public static String getIslandSrc(String island) {
        return "szigetek/" + island + ".txt";
    }

}
