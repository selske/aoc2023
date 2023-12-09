package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;

public class Day07 extends Day {

    public Day07() {
        super(7);
    }

    public static void main(String[] args) {
        new Day07()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("251029473")
                .verifyPart2("251003917");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        int part1 = solve(input, Hand::part1);
        int part2 = solve(input, Hand::part2);

        results.setPart1(part1);
        results.setPart2(part2);
    }

    private int solve(String input, Function<String, Hand> parser) {
        List<Hand> hands = input.lines()
                .map(parser)
                .sorted(Hand.COMPARATOR)
                .toList();

        int part1 = 0;
        for (int i = 0; i < hands.size(); i++) {
            Hand hand = hands.get(i);
            part1 += (i + 1) * hand.bid;
        }
        return part1;
    }

    private enum HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND,
    }

    private record Hand(int bid, HandType type, int[] cardValues) {

        public static final Comparator<Hand> COMPARATOR = comparingInt((Hand hand) -> hand.type.ordinal())
                .thenComparing((a,b) -> Arrays.compare(a.cardValues, b.cardValues));

        public static Hand part1(String input) {
            String[] split = input.split(" ");
            String value = split[0];
            int bid = parseInt(split[1]);
            HandType type = getHandType(value.chars().boxed().toList());
            var cardValues = parseValues(value, 11);

            return new Hand(bid, type, cardValues);
        }

        public static Hand part2(String input) {
            String[] split = input.split(" ");
            String value = split[0];
            int bid = parseInt(split[1]);
            HandType type = Hand.getHandType(value.chars().filter(c -> c != 'J').boxed().toList());
            var cardValues = parseValues(value, 0);

            return new Hand(bid, type, cardValues);
        }

        private static HandType getHandType(List<Integer> values) {
            var counts = values.stream().collect(groupingBy(Function.identity()));
            var maxCount = counts.values().stream().mapToInt(List::size).max().orElse(0);
            var jokers = 5 - values.size();

            HandType type;
            if (maxCount + jokers == 5) {
                type = HandType.FIVE_OF_A_KIND;
            } else if (maxCount + jokers == 4) {
                type = HandType.FOUR_OF_A_KIND;
            } else if (maxCount + jokers == 3) {
                var minCount = counts.values().stream().mapToInt(List::size).min().orElse(0);
                if (minCount == 2) {
                    type = HandType.FULL_HOUSE;
                } else {
                    type = HandType.THREE_OF_A_KIND;
                }
            } else if (maxCount + jokers == 2) {
                var numberOfPairs = counts.values().stream().filter(v -> v.size() == 2).count();
                if (numberOfPairs + jokers == 2) {
                    type = HandType.TWO_PAIR;
                } else {
                    type = HandType.ONE_PAIR;
                }
            } else {
                type = HandType.HIGH_CARD;
            }
            return type;
        }

        private static int[] parseValues(String input, int jValue) {
            return input.chars()
                    .map(i -> switch ((char) i) {
                        case 'A' -> 14;
                        case 'K' -> 13;
                        case 'Q' -> 12;
                        case 'J' -> jValue;
                        case 'T' -> 10;
                        default -> Character.digit(i, 10);
                    })
                    .toArray();
        }

    }

}