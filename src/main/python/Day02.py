import re
import timeit

file = open("../resources/day2.txt")

lines = file.readlines()

start = timeit.default_timer()

game_pattern = re.compile('Game (\\d+):(.*)')
green_pattern = re.compile('(\\d+) green')
red_pattern = re.compile('(\\d+) red')
blue_pattern = re.compile('(\\d+) blue')

part1 = 0
part2 = 0
for line in lines:
    game = int(game_pattern.match(line)[1])
    max_green = max([int(i) for i in green_pattern.findall(line)])
    max_red = max([int(i) for i in red_pattern.findall(line)])
    max_blue = max([int(i) for i in blue_pattern.findall(line)])
    # 12 red cubes, 13 green cubes, and 14 blue cubes
    if max_blue <= 14 and max_red <= 12 and max_green <= 13:
        part1 += game

    part2 += max_green * max_red * max_blue

print('Took ', (timeit.default_timer() - start) * 1000, 'ms')
print('Part 1: ', part1)
print('Part 2: ', part2)
