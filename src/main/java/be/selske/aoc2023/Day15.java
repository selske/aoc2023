package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Day15 extends Day {

    public Day15() {
        super(11);
    }

    public static void main(String[] args) {
        new Day15()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("9947476")
                .verifyPart2(null);
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        List<String> lines = input.lines().toList();

        List<Point> points = new ArrayList<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '#') {
                    points.add(new Point(row, col));
                }
            }
        }
        List<Integer> emptyRows = new ArrayList<>();
        for (int row = 0; row < lines.size(); row++) {
            int rowToCheck = row;
            if (points.stream().noneMatch(p -> p.row() == rowToCheck)) {
                emptyRows.add(row);
            }
        }
        List<Integer> emptyCols = new ArrayList<>();
        for (int col = 0; col < lines.size(); col++) {
            int colToCheck = col;
            if (points.stream().noneMatch(p -> p.col() == colToCheck)) {
                emptyCols.add(col);
            }
        }

        long part1 = 0L;
        long part2 = 0L;
        for (int i = 0; i < points.size() - 1; i++) {
            Point a = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                Point b = points.get(j);
                long distance = abs(a.row - b.row) + abs(a.col - b.col);
                long emptyRowsBetween = emptyRows.stream()
                        .filter(row -> (a.row < row && row < b.row) || (b.row < row && row < a.row))
                        .count();
                long emptyColsBetween = emptyCols.stream()
                        .filter(col -> (a.col < col && col < b.col) || (b.col < col && col < a.col))
                        .count();
                part1 += distance + emptyRowsBetween + emptyColsBetween;
                part2 += distance + emptyRowsBetween * (1_000_000 - 1) + emptyColsBetween * (1_000_000 - 1);
            }
        }

        results.setPart1(part1);
        results.setPart2(part2);
    }

    private record Point(int row, int col) {}

}