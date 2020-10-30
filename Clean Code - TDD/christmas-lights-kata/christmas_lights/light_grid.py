from dataclasses import dataclass
from typing import List


@dataclass
class Cell:
    i: int
    j: int


class LightGrid:
    def __init__(self, length: int, width: int):
        self._grid = self.__create_grid(length, width)

    def __create_grid(self, length: int, width: int) -> List[List[bool]]:
        self._grid = []
        for i in range(length):
            self._grid.append([])
            for j in range(width):
                self._grid[i].append(False)
        return self._grid

    def cell(self, cell: Cell) -> bool:
        return self._grid[cell.i][cell.j]

    def turn_on(self, cell: Cell) -> None:
        self._grid[cell.i][cell.j] = True

    def turn_group_on(self, start_cell: Cell, end_cell: Cell) -> None:
        for curr_i in range(start_cell.i, end_cell.i + 1):
            for curr_j in range(start_cell.j, end_cell.j + 1):
                self.turn_on(Cell(curr_i, curr_j))

    def turn_off(self, cell: Cell) -> None:
        self._grid[cell.i][cell.j] = False

    def turn_group_off(self, start_cell: Cell, end_cell: Cell) -> None:
        for curr_i in range(start_cell.i, end_cell.i + 1):
            for curr_j in range(start_cell.j, end_cell.j + 1):
                self.turn_off(Cell(curr_i, curr_j))

    def toggle(self, cell: Cell) -> None:
        state = self._grid[cell.i][cell.j] is not True
        self._grid[cell.i][cell.j] = state

    def toggle_group(self, start_cell: Cell, end_cell: Cell) -> None:
        for curr_i in range(start_cell.i, end_cell.i + 1):
            for curr_j in range(start_cell.j, end_cell.j + 1):
                self.toggle(Cell(curr_i, curr_j))

    def print_grid(self) -> None:
        for i in range(len(self._grid)):
            for j in range(len(self._grid[0])):
                print('*' if self._grid[i][j] else '_', end='  ')
            print()
