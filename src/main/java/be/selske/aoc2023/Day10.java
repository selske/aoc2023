package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.*;

import static be.selske.aoc2023.Day10.Direction.*;
import static java.util.stream.Collectors.toSet;

public class Day10 extends Day {

    public Day10() {
        super(10);
    }

    public static void main(String[] args) {
        new Day10()
//                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("7107")
                .verifyPart2(null); //<564,<552
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        Diagram diagram = load(input);

        Map<Point, Character> points = diagram.map();
        Direction direction = Arrays.stream(values())
                .filter(d -> d.chars.contains(points.get(diagram.start().getNeighbour(d))))
                .findAny()
                .orElseThrow();

        Point current = diagram.start();
        Map<Point, Character> path = new LinkedHashMap<>();
        Set<Point> left = new HashSet<>();
        Set<Point> right = new HashSet<>();
        do {
            path.put(current, diagram.map.get(current));
            diagram.map.remove(current);
            current = current.getNeighbour(direction);
            direction = getNextDirection(current, direction.comingFrom(), points);
            if (diagram.map.get(current) == null) {
                continue;
            }

            switch (diagram.map.get(current)) {
                case '|' -> {
                    if (direction == NORTH) {
                        left.add(current.getNeighbour(WEST));
                        right.add(current.getNeighbour(EAST));
                    } else if (direction == SOUTH) {
                        left.add(current.getNeighbour(EAST));
                        right.add(current.getNeighbour(WEST));
                    } else {
                        throw new IllegalStateException();
                    }
                }
                case '-' -> {
                    if (direction == WEST) {
                        left.add(current.getNeighbour(SOUTH));
                        right.add(current.getNeighbour(NORTH));
                    } else if (direction == EAST) {
                        left.add(current.getNeighbour(NORTH));
                        right.add(current.getNeighbour(SOUTH));
                    } else {
                        throw new IllegalStateException();
                    }
                }
                case 'L' -> {
                    if (direction == EAST) {
                        right.add(current.getNeighbour(WEST));
                        right.add(current.getNeighbour(SOUTH));
                    } else if (direction == NORTH) {
                        left.add(current.getNeighbour(WEST));
                        left.add(current.getNeighbour(SOUTH));
                    }
                }
                case 'F' -> {
                    if (direction == SOUTH) {
                        right.add(current.getNeighbour(WEST));
                        right.add(current.getNeighbour(NORTH));
                    } else if (direction == EAST) {
                        left.add(current.getNeighbour(WEST));
                        left.add(current.getNeighbour(NORTH));
                    }
                }
                case 'J' -> {
                    if (direction == WEST) {
                        left.add(current.getNeighbour(EAST));
                        left.add(current.getNeighbour(SOUTH));
                    } else if (direction == NORTH) {
                        right.add(current.getNeighbour(EAST));
                        right.add(current.getNeighbour(SOUTH));
                    }
                }
                case '7' -> {
                    if (direction == SOUTH) {
                        left.add(current.getNeighbour(NORTH));
                        left.add(current.getNeighbour(EAST));
                    } else if (direction == WEST) {
                        right.add(current.getNeighbour(NORTH));
                        right.add(current.getNeighbour(EAST));
                    }
                }
            }
        } while (direction != null);

        left.removeAll(path.keySet());
        right.removeAll(path.keySet());

        diagram.map.keySet()
                .forEach(p -> {
                    if (find(Set.of(p), diagram.map.keySet(), right, left)) {
                        right.add(p);
                    } else {
                        left.add(p);
                    }
                });


        int part2;
        if (left.stream().anyMatch(p -> p.row == 0 || p.row == diagram.rows - 1 || p.col == 0 || p.col == diagram.cols - 1)) {
            part2 = right.size();
        } else {
            part2 = left.size();
        }

//        print(diagram, path, left, right);

        results.setPart1(path.size() / 2);
        results.setPart2(part2);
    }

    private boolean find(Set<Point> pointsToCheck, Set<Point> points, Set<Point> target, Set<Point> other) {
        if (pointsToCheck.isEmpty()) {
            return false;
        }
        if (pointsToCheck.stream().anyMatch(target::contains)) {
            return true;
        }
        if (pointsToCheck.stream().anyMatch(other::contains)) {
            return false;
        }
        Set<Point> nextPoints = pointsToCheck.stream()
                .parallel()
                .flatMap(point -> Arrays.stream(values()).map(point::getNeighbour))
                .filter(points::contains)
                .collect(toSet());
        Set<Point> newPoints = new HashSet<>(points);
        nextPoints.forEach(newPoints::remove);
        pointsToCheck.forEach(newPoints::remove);
        return find(nextPoints, newPoints, target, other);

    }

    private void print(Diagram diagram, Map<Point, Character> path, Set<Point> left, Set<Point> right) {
        for (int row = 0; row < diagram.rows; row++) {
            for (int col = 0; col < diagram.cols; col++) {
                Point point = new Point(row, col);
                if (left.contains(point)) {
                    System.out.print('L');
                } else if (right.contains(point)) {
                    System.out.print('R');
                } else if (diagram.map().containsKey(point)) {
                    System.out.print('X');
                } else if (path.containsKey(point)) {
                    char c = path.get(point);
                    System.out.print(
                            switch (c) {
                                case 'F' -> '┌';
                                case 'J' -> '┘';
                                case '7' -> '┐';
                                case 'L' -> '└';
                                case '-' -> '─';
                                default -> c;
                            }
                    );
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
    }

    private static Diagram load(String input) {
        List<String> lines = input.lines().toList();

        Map<Point, Character> points = new HashMap<>();
        Point start = null;
        int rows = lines.size();
        int cols = lines.getFirst().length();
        for (int row = 0; row < rows; row++) {
            var line = lines.get(row);
            for (int col = 0; col < cols; col++) {
                char c = line.charAt(col);
                Point point = new Point(row, col);
                if (c == 'S') {
                    if (start != null) {
                        throw new IllegalArgumentException("More than one start");
                    }
                    start = point;
                }
                points.put(point, c);
            }
        }
        if (start == null) {
            throw new IllegalArgumentException("No start found");
        }

        return new Diagram(start, points, lines.size(), lines.getFirst().length());
    }


    private Direction getNextDirection(Point current, Direction comingFrom, Map<Point, Character> points) {
        Character next = points.get(current);
        return switch (next) {
            case null -> null;
            case '|' -> comingFrom == NORTH ? SOUTH : NORTH; // is a vertical pipe connecting north and south.
            case '-' -> comingFrom == WEST ? EAST : WEST; // is a horizontal pipe connecting east and west.
            case 'L' -> comingFrom == NORTH ? EAST : NORTH; // is a 90-degree bend connecting north and east.
            case 'J' -> comingFrom == NORTH ? WEST : NORTH; // is a 90-degree bend connecting north and west.
            case '7' -> comingFrom == SOUTH ? WEST : SOUTH; // is a 90-degree bend connecting south and west.
            case 'F' -> comingFrom == SOUTH ? EAST : SOUTH; // is a 90-degree bend connecting south and east.
            case 'S' -> null;
            default -> throw new IllegalArgumentException(next + " is unknown");
        };
    }


    private record Point(int row, int col) {
        public Point getNeighbour(Direction direction) {
            return switch (direction) {
                case NORTH -> new Point(row - 1, col);
                case EAST -> new Point(row, col + 1);
                case SOUTH -> new Point(row + 1, col);
                case WEST -> new Point(row, col - 1);
            };
        }
    }

    enum Direction {
        NORTH(List.of('|', 'F', '7')),
        EAST(List.of('-', 'J', '7')),
        SOUTH(List.of('|', 'L', 'J')),
        WEST(List.of('-', 'F', 'L')),
        ;
        private final List<Character> chars;

        Direction(List<Character> chars) {
            this.chars = chars;
        }

        public Direction comingFrom() {
            return switch (this) {
                case NORTH -> SOUTH;
                case EAST -> WEST;
                case SOUTH -> NORTH;
                case WEST -> EAST;
            };
        }
    }

    private record Diagram(Point start, Map<Point, Character> map, int rows, int cols) {}
}