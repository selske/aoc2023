package be.selske.aoc2023.helpers;

import java.util.stream.IntStream;

public class Primes {

    public static IntStream stream(int max) {
        int limit = max / 2;
        boolean[] sieve = new boolean[limit];
        return IntStream.range(2, limit)
                .filter(i -> !sieve[i])
                .peek(p -> {
                    for (int j = 2; j * p < limit; j++) {
                        sieve[j * p] = true;
                    }
                });
    }

}
