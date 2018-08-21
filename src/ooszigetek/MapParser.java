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

public class MapParser {

    private Map<Integer, List<Coordinate>> islands;
    private BufferedReader reader;
    private Stack<Integer> stack;
    private List<String> map;
    private int[][] track;
    private int islandCounter;
    private int islandId;

    public MapParser(BufferedReader br) throws IOException {
        this.islandCounter = 0;
        this.islandId = 1;

        this.reader = br;
        this.islands = new HashMap<>();
        this.map = new ArrayList<>();
        this.stack = new Stack<>();

        readMap();
        track = new int[map.size()][map.get(0).length()];
    }

    private void readMap() throws IOException {
        String line;
        for (line = reader.readLine(); line != null; line = reader.readLine()) {
            map.add(line);
        }
    }

    private void setIslandList() {
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[i].length; j++) {
                int id = track[i][j];
                
                if (id == 0) {
                    continue;
                }
                
                if (islands.get(id) == null) {
                    islands.put(id, new ArrayList<>());
                }
                islands.get(id).add(new Coordinate(i, j));
            }
        }
    }

    public void parseMap() {
        long t = System.currentTimeMillis();

        for (int i = 0; i < map.size(); i++) {
            if (map.get(i).contains("o")) {
                int prev = islandCounter;

                for (int j = 0; j < map.get(i).length(); j++) {
                    if (track[i][j] == 0) {
                        process(i, j);
                    }
                }
                if (islandCounter > prev) {
                    islandCounter = 0;
                    islandId++;
                }
            }
        }

        setIslandList();
        System.out.println("Time: " + (System.currentTimeMillis() - t) + "ms");
    }

    private void process(int i, int j) {
        stack.push(j);
        stack.push(i);

        while (!stack.empty()) {
            int row = stack.pop();
            int col = stack.pop();
/// FIXIXIXIXIXIXIXIXIX
            if (get(row, col) == 'o' && track[row][col] == 0) {
                track[row][col] = islandId;
                islandCounter++;

                // E
                stack.push(col + 1);
                stack.push(row);

                // W
                stack.push(col - 1);
                stack.push(row);

                // N
                stack.push(col);
                stack.push(row - 1);

                // NE
                stack.push(col + 1);
                stack.push(row - 1);

                // NW
                stack.push(col - 1);
                stack.push(row + 1);

                // SE
                stack.push(col + 1);
                stack.push(row + 1);

                // S
                stack.push(col);
                stack.push(row + 1);

                // SW
                stack.push(col - 1);
                stack.push(row + 1);
            }
        }
    }

    private char get(int i, int j) {
        if (i >= 0 && j >= 0 && i < track.length && j < track[0].length) {
            return map.get(i).charAt(j);
        } else {
            return 'x';
        }
    }

    public int getIslandCount() {
        return islandId - 1;
    }

    public Map<Integer, List<Coordinate>> getIslands() {
        return islands;
    }

    @Override
    public String toString() {
        return Arrays.stream(track)
                .map(row -> Arrays.toString(row)
                    .replaceAll("[^\\d.]", "")
                    .replaceAll("0", "_")
                )
                .collect(Collectors.joining("\n"));
    }
}
