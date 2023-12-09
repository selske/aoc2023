package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.Long.parseLong;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public class Day06 extends Day {

    public Day06() {
        super(6);
    }

    public static void main(String[] args) {
        new Day06()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("1083852")
                .verifyPart2("23501589");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        List<String> lines = input.lines().toList();

        Pattern numberPattern = Pattern.compile("\\d+");

        var times = numberPattern.matcher(lines.get(0)).results().mapToLong(r -> parseLong(r.group())).toArray();
        var distances = numberPattern.matcher(lines.get(1)).results().mapToLong(r -> parseLong(r.group())).toArray();

        long part1 = IntStream.range(0, times.length)
                .mapToLong(i -> simulate(times[i], distances[i]))
                .reduce(1, (a, b) -> a * b);

        long time = parseLong(lines.get(0).replaceAll(" ", "").replaceAll("\\D", ""));
        long currentRecord = parseLong(lines.get(1).replaceAll(" ", "").replaceAll("\\D", ""));
        long part2 = simulate(time, currentRecord);

        results.setPart1(part1);
        results.setPart2(part2);
    }

    private long simulate(long time, long currentRecord) {
        var discriminant = sqrt((time * time) - (4 * currentRecord));

        var x1 = (time - discriminant) / 2;
        var x2 = (time + discriminant) / 2;

        return (long) (ceil(x2) - ceil(x1));
    }

}