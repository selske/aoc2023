package be.selske.aoc2023;

import be.selske.aoc2023.benchmark.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class Day15 extends Day {

    public Day15() {
        super(15);
    }

    public static void main(String[] args) {
        new Day15()
                .example()
                .puzzle()
                .benchmark()
                .verifyPart1("521341")
                .verifyPart2("252782");
    }

    @Override
    protected void solve(ResultContainer results, String input, String parameter) {
        var lines = input.split(",");

        int part1 = 0;

        var boxes = Stream.generate(() -> new ArrayList<Lens>()).limit(256).toList();
        outer:
        for (String line : lines) {
            part1 += hash(line);

            int equalsIndex = line.indexOf('=');
            if (equalsIndex < 0) {
                String[] split = line.split("-");
                String label = split[0];
                int box = hash(label);

                List<Lens> lenses = boxes.get(box);
                for (int j = 0; j < lenses.size(); j++) {
                    Lens lens = lenses.get(j);
                    if (label.equals(lens.label)) {
                        lenses.remove(j);
                        continue outer;
                    }
                }
            } else {
                String[] split = line.split("=");
                String label = split[0];
                int focalLength = parseInt(split[1]);
                int box = hash(label);

                List<Lens> lenses = boxes.get(box);
                Lens newLens = new Lens(label, focalLength);
                for (int j = 0; j < lenses.size(); j++) {
                    Lens lens = lenses.get(j);
                    if (label.equals(lens.label)) {
                        lenses.set(j, newLens);
                        continue outer;
                    }
                }
                lenses.add(newLens);
            }
        }

        int part2 = 0;
        for (int i = 0; i < boxes.size(); i++) {
            List<Lens> box = boxes.get(i);

            for (int j = 0; j < box.size(); j++) {
                Lens lens = box.get(j);
                part2 += (i + 1) * (j + 1) * lens.focalLength;
            }

        }

        results.setPart1(part1);
        results.setPart2(part2);
    }

    record Lens(String label, int focalLength) {}

    private static int hash(String line) {
        int value = 0;
        for (int i = 0; i < line.length(); i++) {
            int c = line.charAt(i);
            value += c;
            value *= 17;
            value %= 256;
        }
        return value;
    }
}