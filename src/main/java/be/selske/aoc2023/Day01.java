package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Day01 extends Day {

    public Day01() {
        super(1);
    }

    public static void main(String[] args) {
        new Day01()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("55002")
                .verifyPart2("55093");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        results.setPart1(part1(input));
        results.setPart2(part2(input));
    }

    private static int part1(String input) {
        Pattern pattern = Pattern.compile("\\d");
        return input.lines()
                .mapToInt(line -> {
                    Matcher matcher = pattern.matcher(line);
                    if (!matcher.find()) {
                        throw new IllegalArgumentException();
                    }
                    int first = parseInt(matcher.group());
                    int last = first;
                    while (matcher.find()) {
                        last = parseInt(matcher.group());
                    }
                    return first * 10 + last;
                })
                .sum();
    }

    private static int part2(String input) {
        Pattern pattern = Pattern.compile("\\d|one|two|three|four|five|six|seven|eight|nine");
        return input.lines()
                .mapToInt(line -> {
                    Matcher matcher = pattern.matcher(line);
                    if (!matcher.find()) {
                        throw new IllegalArgumentException();
                    }
                    int first = parse(matcher.group());
                    int index = line.length();
                    while (!matcher.find(--index)) ;
                    int last = parse(matcher.group());
                    return first * 10 + last;
                })
                .sum();
    }

    private static int parse(String input) {
        if (input.matches("\\d")) {
            return parseInt(input);
        }
        return switch (input) {
            case "one" -> 1;
            case "two" -> 2;
            case "three" -> 3;
            case "four" -> 4;
            case "five" -> 5;
            case "six" -> 6;
            case "seven" -> 7;
            case "eight" -> 8;
            case "nine" -> 9;
            default -> throw new IllegalArgumentException("Unknown value " + input);
        };
    }

}