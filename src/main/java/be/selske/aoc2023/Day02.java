package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;

public class Day02 extends Day {

    public Day02() {
        super(2);
    }

    public static void main(String[] args) {
        new Day02()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("2563")
                .verifyPart2("70768");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        List<Game> games = input.lines()
                .map(line -> {
                    String[] split = line.split(":");
                    var game = Integer.parseInt(split[0].split(" ")[1]);
                    var combinations = Arrays.stream(split[1].split(";"))
                            .map(Combination::fromText)
                            .toList();
                    return new Game(game, combinations);
                })
                .toList();

        results.setPart1(
                games.stream()
                        .mapToInt(Day02::part1Line)
                        .sum()
        );
        results.setPart2(
                games.stream()
                        .mapToInt(Day02::part2Line)
                        .sum()
        );
    }

    private static int part1Line(Game game) {
        boolean possible = game.combinations.stream()
                .allMatch(new Combination(14, 12, 13)::contains);
        return possible ? game.game : 0;
    }

    private static int part2Line(Game game) {
        Combination minimum = game.combinations.stream()
                .reduce(new Combination(0, 0, 0), (a, b) -> new Combination(max(a.blue(), b.blue()), max(a.red(), b.red()), max(a.green(), b.green())));
        return minimum.power();
    }

    private record Game(int game, List<Combination> combinations) {

    }

    private record Combination(int blue, int red, int green) {
        static Combination fromText(String text) {
            return new Combination(
                    parse(text, "blue"),
                    parse(text, "red"),
                    parse(text, "green")
            );
        }

        private static int parse(String text, String colour) {
            var redPattern = Pattern.compile("(\\d+) " + colour);
            Matcher redMatcher = redPattern.matcher(text);
            if (redMatcher.find()) {
                return Integer.parseInt(redMatcher.group(1));
            } else {
                return 0;
            }
        }

        public boolean contains(Combination combination) {
            return this.blue >= combination.blue && this.red >= combination.red && this.green >= combination.green;
        }

        public int power() {
            return blue * red * green;
        }
    }

}