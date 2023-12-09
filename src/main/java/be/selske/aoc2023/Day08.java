package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;
import be.selske.aoc2023.helpers.Primes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class Day08 extends Day {

    public Day08() {
        super(8);
    }

    public static void main(String[] args) {
        new Day08()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("12169")
                .verifyPart2("12030780859469");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        var nodes = input.lines()
                .skip(2)
                .map(Node::parse)
                .collect(toMap(Node::name, Function.identity()));

        String path = input.lines().findFirst().orElseThrow();

        int count = solvePath(nodes, path, "AAA", "ZZZ"::equals);

        int[] distances = nodes.keySet().stream()
                .filter(node -> node.endsWith("A"))
                .mapToInt(node -> solvePath(nodes, path, node, cur -> cur.endsWith("Z")))
                .toArray();

        results.setPart1(count);
        results.setPart2(lcm(distances));
    }

    private long lcm(int[] values) {
        Map<Integer, Long> maxFactorCounts = new HashMap<>();
        int max = IntStream.of(values).max().orElseThrow();
        int[] primes = Primes.stream(max).toArray();
        for (int value : values) {
            List<Integer> factors = primeFactorsOf(value, primes);
            factors.stream()
                    .collect(groupingBy(Function.identity(), counting()))
                    .forEach((key, val) -> maxFactorCounts.compute(key, (k, v) -> v != null && v > val ? v : val));
        }
        return maxFactorCounts.entrySet().stream()
                .mapToLong(e -> (long) Math.pow(e.getKey(), e.getValue()))
                .reduce(1L, (a, b) -> a * b);
    }

    private List<Integer> primeFactorsOf(int value, int[] primes) {
        List<Integer> primeFactors = new ArrayList<>();
        int currentValue = value;
        for (int i = 0; i < primes.length; i++) {
            int prime = primes[i];
            if (currentValue % prime == 0) {
                currentValue /= prime;
                primeFactors.add(prime);
                i--;
            }
        }
        return primeFactors;
    }


    private static int solvePath(Map<String, Node> nodes, String path, String start, Predicate<String> end) {
        String current = start;
        int count = 0;
        int i = 0;
        do {
            count++;
            var node = nodes.get(current);
            char c = path.charAt(i);
            if (c == 'L') {
                current = node.left();
            } else {
                current = node.right();
            }
            i = (i + 1) % path.length();
        } while (!end.test(current));
        return count;
    }

    private record Node(String name, String left, String right) {
        public static Node parse(String input) {
            return new Node(input.substring(0, 3), input.substring(7, 10), input.substring(12, 15));
        }
    }

}