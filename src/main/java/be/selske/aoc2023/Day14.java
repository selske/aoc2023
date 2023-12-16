package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 extends Day {

    public Day14() {
        super(14);
    }

    public static void main(String[] args) {
        new Day14()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("105249")
                .verifyPart2("88680");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        List<String> lines = input.lines().toList();
        int rows = lines.size();
        int cols = lines.getFirst().length();

        Map<Point, Character> map = init(lines, rows, cols);

        int part1 = part1(map, rows, cols);
        int part2 = part2(map, rows, cols);

        results.setPart1(part1);
        results.setPart2(part2);
    }

    private static int part1(Map<Point, Character> input, int rows, int cols) {
        Map<Point, Character> map = new HashMap<>(input);

        rollUp(cols, rows, map);

        return weigh(rows, cols, map);
    }

    private static int part2(Map<Point, Character> input, int rows, int cols) {
        Map<Point, Character> map = new HashMap<>(input);
        List<Map<Point, Character>> pastPositions = new ArrayList<>();
        Integer stopAt = null;
        for (int i = 0; stopAt == null || i < stopAt; i++) {
            cycle(rows, cols, map);
            if (stopAt == null) {
                int indexOfPreviousOccurrence = pastPositions.indexOf(map);
                if (indexOfPreviousOccurrence > 0) {
                    stopAt = i + ((1_000_000_000 - i) % (i - indexOfPreviousOccurrence));
                    pastPositions.clear();
                }
                pastPositions.add(new HashMap<>(map));
            }
        }

        return weigh(rows, cols, map);
    }

    private static Map<Point, Character> init(List<String> lines, int rows, int cols) {
        Map<Point, Character> map = new HashMap<>();

        for (int row = 0; row < rows; row++) {
            String line = lines.get(row);
            for (int col = 0; col < cols; col++) {
                var point = new Point(row, col);
                map.put(point, line.charAt(col));
            }
        }
        return map;
    }

    private static int weigh(int rows, int cols, Map<Point, Character> map) {
        int weight = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (map.get(new Point(row, col)) == 'O') {
                    weight += rows - row;
                }
            }
        }
        return weight;
    }


    private static void cycle(int rows, int cols, Map<Point, Character> map) {
        rollUp(cols, rows, map);
        rollLeft(cols, rows, map);
        rollDown(cols, rows, map);
        rollRight(cols, rows, map);
    }

    private static void rollUp(int cols, int rows, Map<Point, Character> map) {
        for (int col = 0; col < cols; col++) {
            for (int row = 1; row < rows; row++) {
                char c = map.get(new Point(row, col));
                if (c != 'O') {
                    continue;
                }
                boolean placed = false;
                for (int i = row - 1; i >= 0; i--) {
                    char c1 = map.get(new Point(i, col));
                    if (c1 == 'O' || c1 == '#') {
                        map.put(new Point(row, col), '.');
                        map.put(new Point(i + 1, col), c);
                        placed = true;
                        break;
                    }
                }
                if (!placed) {
                    map.put(new Point(row, col), '.');
                    map.put(new Point(0, col), c);
                }
            }
        }
    }

    private static void rollLeft(int cols, int rows, Map<Point, Character> map) {
        for (int row = 0; row < rows; row++) {
            for (int col = 1; col < cols; col++) {
                char c = map.get(new Point(row, col));
                if (c != 'O') {
                    continue;
                }
                boolean placed = false;
                for (int i = col - 1; i >= 0; i--) {
                    char c1 = map.get(new Point(row, i));
                    if (c1 == 'O' || c1 == '#') {
                        map.put(new Point(row, col), '.');
                        map.put(new Point(row, i + 1), c);
                        placed = true;
                        break;
                    }
                }
                if (!placed) {
                    map.put(new Point(row, col), '.');
                    map.put(new Point(row, 0), c);
                }
            }
        }
    }

    private static void rollDown(int cols, int rows, Map<Point, Character> map) {
        for (int col = 0; col < cols; col++) {
            for (int row = rows - 1; row >= 0; row--) {
                char c = map.get(new Point(row, col));
                if (c != 'O') {
                    continue;
                }
                boolean placed = false;
                for (int i = row + 1; i < rows; i++) {
                    char c1 = map.get(new Point(i, col));
                    if (c1 == 'O' || c1 == '#') {
                        map.put(new Point(row, col), '.');
                        map.put(new Point(i - 1, col), c);
                        placed = true;
                        break;
                    }
                }
                if (!placed) {
                    map.put(new Point(row, col), '.');
                    map.put(new Point(rows - 1, col), c);
                }
            }
        }
    }

    private static void rollRight(int cols, int rows, Map<Point, Character> map) {
        for (int row = 0; row < rows; row++) {
            for (int col = cols - 1; col >= 0; col--) {
                char c = map.get(new Point(row, col));
                if (c != 'O') {
                    continue;
                }
                boolean placed = false;
                for (int i = col + 1; i < cols; i++) {
                    char c1 = map.get(new Point(row, i));
                    if (c1 == 'O' || c1 == '#') {
                        map.put(new Point(row, col), '.');
                        map.put(new Point(row, i - 1), c);
                        placed = true;
                        break;
                    }
                }
                if (!placed) {
                    map.put(new Point(row, col), '.');
                    map.put(new Point(row, cols - 1), c);
                }
            }
        }
    }

    record Point(int row, int col) {}

}