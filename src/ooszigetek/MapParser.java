package ooszigetek;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Class for parsing a text file that contains a map with the conventions on
 * <a>https://www.webotlet.hu/?p=1047</a>
 * @author Jordan_Szalontai
 */
public class MapParser {

    private Map<Integer, List<Coordinate>> islands;
    private BufferedReader reader;
    private Stack<Integer> stack;
    private List<String> mapRows;

    private int[][] cache;
    private int islandCounter;
    private int islandId;

    public MapParser(BufferedReader br) throws IOException {
        this.islandCounter = 0;
        this.islandId = 1;

        this.reader = br;
        this.islands = new HashMap<>();
        this.mapRows = new ArrayList<>();
        this.stack = new Stack<>();

        String line;
        for (line = reader.readLine(); line != null; line = reader.readLine()) {
            this.mapRows.add(line);
        }

        this.cache = new int[mapRows.size()][mapRows.get(0).length()];
    }

    /**
     * Crawls trough the map then it sets the islands {@code Map} of this
     * object.
     *
     * As an extra this method counts the time it took to do all the
     * calculations.
     */
    public void parseMap() {
        long t = System.currentTimeMillis();

        for (int i = 0; i < mapRows.size(); i++) {
            int prevCounter = 0;

            for (int j = 0; j < mapRows.get(i).length(); j++) {
                if (get(i, j) == 'o' && cache[i][j] == 0) {
                    process(i, j, prevCounter);
                }
            }
        }

        islands = createIslandMap();
        System.out.println("Time: " + (System.currentTimeMillis() - t) + "ms");
    }

    /**
     * Marks the coordinates in a cache, so we can distinguish the islands in
     * the future.
     *
     * Each island will have an unique ID, that is an integer number. This
     * method utilizes a stack so it can crawl through the map with all the
     * touching coordinates.
     *
     * @param i the row index of the starting position
     * @param j the column index of the starting position
     * @param prevCounter a counter that indicates how many iterations occurred
     * during the last processing phase
     */
    private void process(int i, int j, int prevCounter) {
        stack.push(j);
        stack.push(i);

        while (!stack.empty()) {
            int row = stack.pop();
            int col = stack.pop();

            if (get(row, col) == 'o' && cache[row][col] == 0) {
                cache[row][col] = islandId;
                islandCounter++;

                pushEightDirections(row, col);
            }
        }

        // if we covered more space than ever before it means that we just found
        // a new island and we are not crawling an existing one anymore
        if (islandCounter > prevCounter) {
            islandCounter = 0;
            islandId++;
        }
    }

    /**
     * Returns a {@code Map} that contains the islands on the given map.
     *
     * An island is a list of coordinates, e.g.
     * <pre>
     * [
     *  {0, 0},
     *  {0, 1},
     *  {1, 0}
     * ]
     * </pre>
     *
     * @return the map that contains islands, the keys are the ID-s of each map
     */
    private Map<Integer, List<Coordinate>> createIslandMap() {
        Map<Integer, List<Coordinate>> newIslands = new HashMap<>();

        for (int i = 0; i < cache.length; i++) {
            for (int j = 0; j < cache[i].length; j++) {
                int id = cache[i][j];

                if (id == 0) {
                    continue;
                }

                if (newIslands.get(id) == null) {
                    newIslands.put(id, new ArrayList<>());
                }
                newIslands.get(id).add(new Coordinate(i, j));
            }
        }

        return newIslands;
    }

    /**
     * Pushes eight new coordinates to the stack of this object.
     *
     * These coordinates are based on the given one. (N, E, S, W, NE, SE, NW,
     * SW)
     *
     * There will be 16 extra elements in the stack, because two integers
     * represent a coordinate.
     *
     * @param row the row index of the coordinate
     * @param col the column index of the coordinate
     */
    private void pushEightDirections(int row, int col) {
        stack.push(col + 1);
        stack.push(row);

        stack.push(col - 1);
        stack.push(row);

        stack.push(col);
        stack.push(row - 1);

        stack.push(col + 1);
        stack.push(row - 1);

        stack.push(col - 1);
        stack.push(row + 1);

        stack.push(col + 1);
        stack.push(row + 1);

        stack.push(col);
        stack.push(row + 1);

        stack.push(col - 1);
        stack.push(row + 1);
    }

    /**
     * Returns a character that represents land water, on non-existent part of
     * the map.
     *
     * @param i the row index of the map
     * @param j the column index of the map
     * @return <ul><li>{@code o} if it is a land</li><li>{@code ~} if it is
     * water</li><li>{@code x} if coordinates are out of bounds</li>
     */
    private char get(int i, int j) {
        try {
            return mapRows.get(i).charAt(j);
        } catch (Exception e) {
            return 'x';
        }
    }

    /**
     * Returns how many islands there are on the map that was read by the
     * {@code reader}.
     *
     * @return the amount of islands on the map
     */
    public int getIslandCount() {
        return islandId - 1;
    }

    public Map<Integer, List<Coordinate>> getIslands() {
        return islands;
    }

    public int[][] getCache() {
        return cache;
    }

    /**
     * Returns a long string that represents the map with each island having an
     * unique number.
     *
     * The downside of using this method is that if we hit 9+ islands it will
     * not look nice on the console.
     *
     * @return the string representation of the map
     */
    @Override
    public String toString() {
        return Arrays.stream(cache)
                .map(row -> Arrays.toString(row)
                .replaceAll("[^\\d.]", "")
                .replaceAll("0", "_")
                )
                .collect(Collectors.joining("\n"));
    }
}
