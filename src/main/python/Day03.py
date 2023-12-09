import re
import timeit
from dataclasses import dataclass

file = open("../resources/day3.txt")

lines = file.readlines()

start = timeit.default_timer()


@dataclass(frozen=True)
class Point:
    r: int
    c: int


def part_number_value(number: int, start_row, start_col, col):
    to_row = min(start_row + 1, len(lines) - 1)
    to_col = min(col + 1, len(lines[0]) - 1)

    for r in range(max(start_row - 1, 0), to_row + 1):
        line_to_check = lines[r]
        for c in range(max(start_col - 1, 0), to_col):
            # if row != r or (start_col > c or c >= col):
            char_to_check = line_to_check[c]
            if char_to_check != '.' and not char_to_check.isdigit():
                if char_to_check == '*':
                    if not Point(r, c) in gears:
                        gears[Point(r, c)] = []
                    gears[Point(r, c)].append(number)
                return number
    return 0


part1 = 0
gears = {}
for row in range(0, len(lines)):
    line = lines[row]
    for match in re.compile('\\d+').finditer(line):
        part1 += part_number_value(int(match.group()), row, match.start(), match.end())

part2 = 0
for value in gears.values():
    if len(value) == 2:
        part2 += value[0] * value[1]

print('Took ', (timeit.default_timer() - start) * 1000, 'ms')
print('Part 1: ', part1)
print('Part 2: ', part2)
