import re
import timeit

file = open("../resources/day1.txt")

lines = file.readlines()

start = timeit.default_timer()


def parse(text: str) -> int:
    match text:
        case 'one':
            return 1
        case 'two':
            return 2
        case 'three':
            return 3
        case 'four':
            return 4
        case 'five':
            return 5
        case 'six':
            return 6
        case 'seven':
            return 7
        case 'eight':
            return 8
        case 'nine':
            return 9
        case _:
            return int(text)


part1 = 0
part2 = 0

number_pattern = re.compile('\\d')
text_pattern = re.compile('(?=(\\d|one|two|three|four|five|six|seven|eight|nine))')
for line in lines:
    number_results = number_pattern.findall(line)
    part1 += int(number_results[0]) * 10 + int(number_results[-1])
    text_results = text_pattern.findall(line)
    part2 += parse(text_results[0]) * 10 + parse(text_results[-1])

print('Took ', (timeit.default_timer() - start) * 1000, 'ms')
print('Part 1: ', part1)
print('Part 2: ', part2)
