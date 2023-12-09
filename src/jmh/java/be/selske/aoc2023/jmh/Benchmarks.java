package be.selske.aoc2023.jmh;

import be.selske.aoc2023.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class Benchmarks {

    private Day01 day01;
    private Day02 day02;
    private Day03 day03;
    private Day04 day04;
    private Day05 day05;
    private Day06 day06;
    private Day07 day07;
    private Day08 day08;
    private Day09 day09;


    @Setup(Level.Iteration)
    public void setup() {
        day01 = new Day01();
        day02 = new Day02();
        day03 = new Day03();
        day04 = new Day04();
        day05 = new Day05();
        day06 = new Day06();
        day07 = new Day07();
        day08 = new Day08();
        day09 = new Day09();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void day01(Blackhole blackhole) {
        blackhole.consume(day01.solve());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void day02(Blackhole blackhole) {
        blackhole.consume(day02.solve());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void day03(Blackhole blackhole) {
        blackhole.consume(day03.solve());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void day04(Blackhole blackhole) {
        blackhole.consume(day04.solve());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void day05(Blackhole blackhole) {
        blackhole.consume(day05.solve());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void day06(Blackhole blackhole) {
        blackhole.consume(day06.solve());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void day07(Blackhole blackhole) {
        blackhole.consume(day07.solve());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void day08(Blackhole blackhole) {
        blackhole.consume(day08.solve());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void day09(Blackhole blackhole) {
        blackhole.consume(day09.solve());
    }


}
