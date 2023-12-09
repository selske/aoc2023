import timeit
from collections import Counter
from dataclasses import dataclass
from typing import Callable

FIVE_OF_A_KIND = 6
FOUR_OF_A_KIND = 5
FULL_HOUSE = 4
THREE_OF_A_KIND = 3
TWO_PAIRS = 2
ONE_PAIR = 1
HIGH_CARD = 0

# file = open("../resources/day7_example.txt")
file = open("../resources/day7.txt")

lines = file.readlines()

start = timeit.default_timer()


def parse_value(c, j_value: int) -> int:
    match c:
        case 'T':
            return 10
        case 'J':
            return j_value
        case 'Q':
            return 12
        case 'K':
            return 13
        case 'A':
            return 14
    return int(c)


def parse_values(name: str, j_value: int) -> list[int]:
    return [parse_value(c, j_value) for c in name]


def get_hand_type(values: list) -> int:
    counts = Counter(values)

    max_count = max(counts.values(), default=0)
    jokers = 5 - len(values)
    if max_count + jokers == 5:
        return FIVE_OF_A_KIND
    elif max_count + jokers == 4:
        return FOUR_OF_A_KIND
    elif max_count + jokers == 3:
        min_count = min(counts.values())
        if min_count == 2:
            return FULL_HOUSE
        else:
            return THREE_OF_A_KIND
    elif max_count + jokers == 2:
        number_of_pairs = len([count for count in counts.values() if count == 2])
        if number_of_pairs + jokers == 2:
            return TWO_PAIRS
        else:
            return ONE_PAIR
    else:
        return HIGH_CARD


@dataclass
class Hand:
    bid: int
    type: int
    values: list[int]


def solve(parser: Callable[[str], Hand]) -> int:
    hands = [parser(line) for line in lines]
    hands.sort(key=lambda h: (h.type, h.values), reverse=False)
    return sum([(i + 1) * hand.bid for i, hand in enumerate(hands)])


def parse_part1(line) -> Hand:
    split = line.split(' ')
    values = parse_values(split[0], 11)
    hand_type = get_hand_type([int(c) for c in values])
    return Hand(int(split[1]), hand_type, values)


def parse_part2(line) -> Hand:
    split = line.split(' ')
    values = parse_values(split[0], 0)
    hand_type = get_hand_type([int(c) for c in values if c != 0])
    return Hand(int(split[1]), hand_type, values)


part1 = solve(parse_part1)
part2 = solve(parse_part2)

print('Took ', (timeit.default_timer() - start) * 1000, 'ms')
print('Part 1: ', part1)
print('Part 2: ', part2)
