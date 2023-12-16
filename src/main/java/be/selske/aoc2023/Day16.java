package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static be.selske.aoc2023.Day16.Direction.*;

public class Day16 extends Day {

    public Day16() {
        super(16);
    }

    public static void main(String[] args) {
        new Day16()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("7623")
                .verifyPart2("8244");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        List<String> lines = input.lines().toList();
        int rows = lines.size();
        int cols = lines.getFirst().length();

        Map<Point, Tile> map = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < lines.get(row).length(); col++) {
                var point = new Point(row, col);
                map.put(point, new Tile(point, line.charAt(col)));
            }
        }

        var part1 = tracePath(map, new Point(0, 0), RIGHT);

        int part2 = IntStream.concat(
                        IntStream.range(0, rows)
                                .parallel()
                                .flatMap(row -> IntStream.of(
                                        tracePath(map, new Point(row, 0), RIGHT),
                                        tracePath(map, new Point(row, cols - 1), LEFT)
                                )),
                        IntStream.range(0, cols)
                                .parallel()
                                .flatMap(col -> IntStream.of(
                                        tracePath(map, new Point(0, col), DOWN),
                                        tracePath(map, new Point(rows - 1, col), UP)
                                ))
                )
                .max().orElseThrow();

        results.setPart1(part1);
        results.setPart2(part2);
    }

    private int tracePath(Map<Point, Tile> map, Point start, Direction startDirection) {
        Set<Point> energizedPoints = new HashSet<>();
        Set<PointDirection> visited = new HashSet<>();
        List<PointDirection> next = List.of(new PointDirection(start, startDirection));
        while (!next.isEmpty()) {
            next = next.stream()
                    .flatMap(pointDirection -> energize(map, pointDirection, energizedPoints, visited))
                    .toList();
        }
        return energizedPoints.size();
    }

    private Stream<PointDirection> energize(Map<Point, Tile> map, PointDirection pointDirection, Set<Point> energizedPoints, Set<PointDirection> visited) {
        if (!visited.add(pointDirection)) {
            return null;
        }
        var point = pointDirection.point;
        var tile = map.get(point);
        if (tile == null) {
            return null;
        }
        energizedPoints.add(point);

        Direction direction = pointDirection.direction();
        List<Direction> nextDirections = switch (tile.content) {
            case '-' -> {
                if (direction != LEFT && direction != RIGHT) {
                    yield List.of(LEFT, RIGHT);
                } else {
                    yield List.of(direction);
                }
            }
            case '|' -> {
                if (direction != UP && direction != DOWN) {
                    yield List.of(UP, DOWN);
                } else {
                    yield List.of(direction);
                }
            }
            case '/' -> List.of(switch (direction) {
                case RIGHT -> UP;
                case DOWN -> LEFT;
                case LEFT -> DOWN;
                case UP -> RIGHT;
            });
            case '\\' -> List.of(switch (direction) {
                case RIGHT -> DOWN;
                case DOWN -> RIGHT;
                case LEFT -> UP;
                case UP -> LEFT;
            });
            default -> List.of(direction);
        };

        return nextDirections.stream()
                .map(nextDirection -> new PointDirection(point.getNeighbour(nextDirection), nextDirection));

    }

    enum Direction {
        RIGHT,
        DOWN,
        LEFT,
        UP,
    }

    record PointDirection(Point point, Direction direction) {

    }

    record Point(int row, int col) {
        public Point getNeighbour(Direction direction) {
            return switch (direction) {
                case UP -> new Point(row - 1, col);
                case RIGHT -> new Point(row, col + 1);
                case DOWN -> new Point(row + 1, col);
                case LEFT -> new Point(row, col - 1);
            };
        }
    }

    record Tile(Point location, char content) {}
}