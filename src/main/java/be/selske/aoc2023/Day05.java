package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.Long.parseLong;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class Day05 extends Day {

    public Day05() {
        super(5);
    }

    public static void main(String[] args) {
        new Day05()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("3374647")
                .verifyPart2("6082852");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        List<String> lines = input.lines().toList();
        List<Long> seeds = Pattern.compile("\\d+")
                .matcher(lines.get(0)).results()
                .map(result -> Long.valueOf(result.group()))
                .toList();

        List<Range> stages = initialize(lines);

        long part1 = part1(seeds, stages);
        long part2 = part2(seeds, stages);

        results.setPart1(part1);
        results.setPart2(part2);
    }

    private List<Range> initialize(List<String> lines) {
        List<List<Range>> stages = new ArrayList<>();
        List<Range> stage = new ArrayList<>();
        for (int i = 2; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) {
                stages.add(fillOut(stage));
                stage = new ArrayList<>();
            } else if (lines.get(i).matches("\\d+.*")) {
                String[] split = lines.get(i).split(" ");
                stage.add(Range.link(parseLong(split[0]), parseLong(split[1]), parseLong(split[2])));
            }
        }
        stages.add(fillOut(stage));
        return compact(stages);
    }

    private static List<Range> fillOut(List<Range> stage) {
        stage.sort(Comparator.comparingLong(Range::start));

        if (stage.get(0).start > 0) {
            stage.add(0, new Range(0, stage.get(0).start, 0));
        }
        if (stage.get(stage.size() - 1).end < Long.MAX_VALUE) {
            stage.add(new Range(stage.get(stage.size() - 1).end, Long.MAX_VALUE, 0));
        }

        for (int i = 0; i < stage.size() - 1; i++) {
            if (stage.get(i).end != stage.get(i + 1).start) {
                stage.add(i + 1, new Range(stage.get(i).end, stage.get(i + 1).start, 0));
            }
        }

        return stage;
    }

    private List<Range> compact(List<List<Range>> stages) {
        List<Range> stage = stages.get(0);
        List<Range> newStage = emptyList();
        for (int i = 1; i < stages.size(); i++) {
            List<Range> nextStage = stages.get(i);

            newStage = new ArrayList<>();
            for (Range range : stage) {
                var matching = nextStage.stream()
                        .filter(l -> l.end > range.start + range.delta && l.start <= range.end + range.delta)
                        .map(l -> new Range(l.start - range.delta, l.end - range.delta, l.delta + range.delta))
                        .collect(toList());
                Range firstMatch = matching.get(0);
                matching.set(0, new Range(range.start, firstMatch.end, firstMatch.delta));
                Range lastMatch = matching.get(matching.size() - 1);
                matching.set(matching.size() - 1, new Range(lastMatch.start, range.end, lastMatch.delta));

                newStage.addAll(matching);
            }
            stage = newStage;
        }
        return newStage;
    }

    private static long part1(List<Long> seeds, List<Range> stage) {
        seeds = seeds.stream()
                .map(seed -> map(stage, seed))
                .toList();

        return seeds.stream().mapToLong(Long::longValue).min().orElseThrow();
    }

    private static long map(List<Range> ranges, long seed) {
        return ranges.stream()
                .filter(range -> range.contains(seed))
                .mapToLong(range -> seed + range.delta)
                .findFirst().orElse(seed);
    }

    private static long part2(List<Long> seeds, List<Range> stage) {

        return IntStream.range(0, seeds.size() / 2)
                .mapToLong(i -> {
                    Long start = seeds.get(2 * i);
                    Long amount = seeds.get(2 * i + 1);

                    var subSeeds = stage.stream()
                            .filter(l -> l.end > start && l.start <= start + amount)
                            .map(l -> l.start)
                            .collect(toList());

                    return part1(subSeeds, stage);
                })
                .min().orElseThrow();
    }

    private record Range(long start, long end, long delta) {


        private static Range link(long destRangeStart, long sourceRangeStart, long sourceRangeLength) {
            return new Range(sourceRangeStart, sourceRangeStart + sourceRangeLength, destRangeStart - sourceRangeStart);
        }

        public boolean contains(long seed) {
            return start <= seed && seed < end;
        }

        @Override
        public String toString() {
            return start + "->" + (end) + "=" + delta;
        }
    }


}