package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends Day {

    public Day12() {
        super(12);
    }

    public static void main(String[] args) {
        new Day12()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1(null)
                .verifyPart2(null);
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        long part1 = part1(input);
        long part2 = part2(input);

        results.setPart1(part1);
        results.setPart2(part2);
    }

    private static long part1(String input) {
        return solve(input, Function.identity());
    }

    private static long part2(String input) {
        return solve(input, s -> s.repeat(5));
    }

    private record Input(String input, List<Integer> lengths) {
        private Input(String input, List<Integer> lengths) {
            this.input = input.replaceAll("\\.+", ".");
            this.lengths = lengths;
        }

        public Input repeat(int amount) {
            return new Input(
                    Stream.generate(() -> input).limit(amount).collect(Collectors.joining("?")),
                    Stream.generate(() -> lengths).limit(amount).flatMap(Collection::stream).toList()
            );
        }
    }

    private static long solve(String input, Function<Input, Input> modifier) {
        AtomicInteger count = new AtomicInteger();
        return input.lines()
                .parallel()
                .map(line -> {
                    String[] split = line.split(" ");
                    String springInput = split[0];
                    var lengths = Arrays.stream(split[1].split(",")).map(Integer::parseInt).toList();
                    return new Input(springInput, lengths);
                })
                .map(modifier)
                .mapToLong(line -> countCombinations(line.input, line.lengths, 0, 0))
                .peek(i -> System.out.println(count.incrementAndGet() + ": " + i))
                .sum();
    }

    private static void print(List<List<Integer>> combinations, String springInput, List<Integer> lengths) {
        for (List<Integer> combination : combinations) {
            System.out.println(combinationToString(springInput, lengths, combination));
        }
    }

    private static String combinationToString(String input, List<Integer> lengths, List<Integer> combination) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = 0; i < input.length(); i++) {
            if (j < combination.size() && i == combination.get(j)) {
                for (int k = 0; k < lengths.get(j); k++) {
                    sb.append('#');
                    i++;
                }
                i--;
                j++;
            } else {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    private static long countCombinations(String input, List<Integer> lengths, int startIndex, int depth) {
        if (lengths.size() == depth) {
            return 1;
        }
        long combinations = 0;
        int firstLength = lengths.get(depth);

        int minLength = lengths.stream().skip(depth).mapToInt(Integer::intValue).sum() + lengths.size() - 1 - depth;
        outer:
        for (int i = startIndex; i < input.length() - minLength + 1; i++) {
            if (i > 0 && input.charAt(i - 1) == '#') {
                break;
            }
            int firstEnd = i + firstLength;
            for (int j = i; j < firstEnd; j++) {
                char c = input.charAt(j);
                if (c != '?' && c != '#') {
                    continue outer;
                }
            }
            if (firstEnd < input.length() && input.charAt(firstEnd) == '#') {
                continue;
            }
            combinations += countCombinations(input, lengths, i + firstLength + 1, depth + 1);
        }
        return combinations;
    }

}