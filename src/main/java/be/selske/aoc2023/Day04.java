package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

public class Day04 extends Day {

    public Day04() {
        super(4);
    }

    public static void main(String[] args) {
        new Day04()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("23847")
                .verifyPart2("8570000");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        var cards = input.lines()
                .map(Card::fromInput)
                .toList();

        var part1 = cards.stream()
                .mapToInt(Card::points)
                .sum();

        ConcurrentMap<Integer, Integer> cache = new ConcurrentHashMap<>();
        int part2 = IntStream.range(0, cards.size())
                .parallel()
                .map(i -> countAllCards(cards, i, cache))
                .sum();

        results.setPart1(part1);
        results.setPart2(part2);
    }


    private synchronized int countAllCards(List<Card> cards, Integer i, Map<Integer, Integer> cache) {
        if (cache.containsKey(i)) {
            return cache.get(i);
        }
        int count = 1;
        Card card = cards.get(i);
        for (int j = 0; j < card.winningNumbers().size(); j++) {
            count += countAllCards(cards, i + j + 1, cache);
        }
        cache.put(i, count);
        return count;

    }

    private record Card(int number, Set<Integer> winningNumbers) {

        public static final Pattern CARD_PATTERN = Pattern.compile("Card +(\\d+):(.*)\\|(.*)");
        public static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

        public int points() {
            long numberOfWinningNumbers = winningNumbers.size();
            if (numberOfWinningNumbers == 0) {
                return 0;
            }
            if (numberOfWinningNumbers == 1) {
                return 1;
            }
            return (int) Math.pow(2, numberOfWinningNumbers - 1);
        }

        public static Card fromInput(String input) {
            Matcher matcher = CARD_PATTERN.matcher(input);
            if (!matcher.matches()) {
                throw new IllegalArgumentException();
            }
            int card = Integer.parseInt(matcher.group(1));
            Set<Integer> winningNumbers = NUMBER_PATTERN.matcher(matcher.group(2)).results()
                    .map(MatchResult::group)
                    .map(Integer::valueOf)
                    .collect(toSet());
            Set<Integer> numbers = NUMBER_PATTERN.matcher(matcher.group(3)).results()
                    .map(MatchResult::group)
                    .map(Integer::valueOf)
                    .filter(winningNumbers::contains)
                    .collect(toSet());
            return new Card(card, numbers);
        }
    }

}