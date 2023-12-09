package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Day03 extends Day {

    public Day03() {
        super(3);
    }

    public static void main(String[] args) {
        new Day03()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("537732")
                .verifyPart2("84883664");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        Pattern numberPattern = Pattern.compile("\\d+");
        var lines = input.lines().toList();
        Map<Point, List<Integer>> gears = new HashMap<>();

        int part1 = 0;
        for (int i = 0; i < lines.size(); i++) {
            int row = i;
            part1 += numberPattern.matcher(lines.get(row)).results()
                    .mapToInt(result -> partNumberValue(Integer.parseInt(result.group()), row, result.start(), result.end(), lines, gears))
                    .sum();
        }

        int part2 = gears.values().stream()
                .filter(v -> v.size() == 2)
                .mapToInt(v -> v.get(0) * v.get(1))
                .sum();

        results.setPart1(part1);
        results.setPart2(part2);
    }

    private int partNumberValue(int number, int row, Integer startCol, int col, List<String> lines, Map<Point, List<Integer>> gears) {
        int toRow = min(row + 1, lines.get(0).length() - 1);
        int toCol = min(col, lines.get(0).length() - 1);

        for (int r = max(row - 1, 0); r <= toRow; r++) {
            var line = lines.get(r);
            for (int c = max(startCol - 1, 0); c <= toCol; c++) {
                if (row == r && startCol <= c && c < col) continue;
                char charToCheck = line.charAt(c);
                if (charToCheck != '.' && !isDigit(charToCheck)) {
                    if (charToCheck == '*') {
                        gears.computeIfAbsent(new Point(r, c), k -> new ArrayList<>()).add(number);
                    }
                    return number;
                }
            }
        }
        return 0;
    }

    private record Point(int row, int col) {}

}