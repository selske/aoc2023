package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.HashMap;
import java.util.Objects;

public class Day13 extends Day {

    public Day13() {
        super(13);
    }

    public static void main(String[] args) {
        new Day13()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("32371")
                .verifyPart2("37416");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        var lines = input.lines().toList();

        var map = new HashMap<Point, Character>();
        int part1 = 0;
        int part2 = 0;

        int startRow = 0;
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            if (line.isBlank()) {
                part1 += solve(map, 0, i - startRow, lines.get(startRow).length());
                part2 += solve(map, 1, i - startRow, lines.get(startRow).length());
                map = new HashMap<>();
                startRow = i + 1;
            }
            for (int col = 0; col < line.length(); col++) {
                var point = new Point(i - startRow, col);
                map.put(point, line.charAt(col));
            }
        }
        part1 += solve(map, 0, lines.size() - startRow, lines.get(startRow).length());
        part2 += solve(map, 1, lines.size() - startRow, lines.get(startRow).length());


        results.setPart1(part1);
        results.setPart2(part2);
    }

    private static int solve(HashMap<Point, Character> map, int desiredDifferences, int rows, int cols) {
        for (int mirrorCol = 1; mirrorCol < cols; mirrorCol++) {
            int differences = 0;
            for (int i = 0; i < mirrorCol; i++) {
                int leftCol = mirrorCol - (i + 1);
                int rightCol = mirrorCol + i;
                for (int row = 0; row < rows; row++) {
                    Character leftVal = map.get(new Point(row, leftCol));
                    if (leftVal == null) {
                        continue;
                    }
                    Character rightVal = map.get(new Point(row, rightCol));
                    if (rightVal == null) {
                        continue;
                    }
                    if (!Objects.equals(leftVal, rightVal)) {
                        differences++;
                        if (differences > desiredDifferences) {
                            break;
                        }
                    }
                }
            }
            if (differences == desiredDifferences) {
                return mirrorCol;
            }
        }

        for (int mirrorRow = 1; mirrorRow < rows; mirrorRow++) {
            int differences = 0;
            for (int i = 0; i < mirrorRow; i++) {
                int topRow = mirrorRow - (i + 1);
                int bottomRow = mirrorRow + i;
                for (int col = 0; col < cols; col++) {
                    Character topVal = map.get(new Point(topRow, col));
                    if (topVal == null) {
                        continue;
                    }
                    Character bottomVal = map.get(new Point(bottomRow, col));
                    if (bottomVal == null) {
                        continue;
                    }
                    if (!Objects.equals(topVal, bottomVal)) {
                        differences++;
                        if (differences > desiredDifferences) {
                            break;
                        }
                    }
                }
            }
            if (differences == desiredDifferences) {
                return mirrorRow * 100;
            }
        }

        throw new IllegalArgumentException();
    }

    private record Point(int row, int col) {}
}