package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day09 extends Day {

    public Day09() {
        super(9);
    }

    public static void main(String[] args) {
        new Day09()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("1877825184")
                .verifyPart2("1108");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        Result result = input.lines()
                .parallel()
                .map(this::solve)
                .reduce(new Result(0L, 0L), Result::sum);


        results.setPart1(result.part1());
        results.setPart2(result.part2());
    }

    private Result solve(String input) {
        List<long[]> levels = new ArrayList<>();
        long[] currentLevel = Pattern.compile("-?\\d+").matcher(input).results().mapToLong(r -> Long.parseLong(r.group())).toArray();

        levels.add(currentLevel);
        do {
            long[] nextLevel = new long[currentLevel.length - 1];
            for (int i = 1; i < currentLevel.length; i++) {
                long a = currentLevel[i - 1];
                long b = currentLevel[i];
                nextLevel[i - 1] = b - a;
            }
            levels.add(nextLevel);
            currentLevel = nextLevel;
        } while (!allZeroes(currentLevel));

        long[] firsts = new long[levels.size()];
        long[] lasts = new long[levels.size()];

        for (int i = levels.size() - 2; i >= 0; i--) {
            long[] higherLevel = levels.get(i);

            lasts[i] = lasts[i + 1] + higherLevel[higherLevel.length - 1];
            firsts[i] = higherLevel[0] - firsts[i + 1];
        }

        return new Result(lasts[0], firsts[0]);
    }

    private boolean allZeroes(long[] level) {
        for (long l : level) {
            if (l != 0L) {
                return false;
            }
        }
        return true;
    }

    private record Result(long part1, long part2) {
        public Result sum(Result other) {
            return new Result(part1 + other.part1, part2 + other.part2);
        }
    }

}