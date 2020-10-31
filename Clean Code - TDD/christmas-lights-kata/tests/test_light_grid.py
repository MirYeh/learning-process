import pytest

from christmas_lights.light_grid import LightGrid, Cell


def test_cell_turn_on_and_off():
    g = LightGrid(1, 1)
    g.turn_on(Cell(0, 0))
    assert g.cell(Cell(0, 0)) == 1
    assert g.count_brightness() == 1

    g.turn_off(Cell(0, 0))
    assert g.cell(Cell(0, 0)) == 0
    assert g.count_brightness() == 0


def test_cell_turn_group_on_and_off():
    length = width = 3
    g = LightGrid(length, width)
    start_cell, end_cell = Cell(0, 0), Cell(2, 2)
    g.turn_group_on(start_cell, end_cell)
    _assert_cells(g, start_cell, end_cell, 1)
    assert g.count_brightness() == 9

    g.turn_group_off(start_cell, end_cell)
    _assert_cells(g, start_cell, end_cell, 0)
    assert g.count_brightness() == 0


def test_minimal_brightness():
    g = LightGrid(1, 1)
    cell = Cell(0, 0)
    assert g.cell(cell) == 0
    assert g.count_brightness() == 0

    g.turn_off(cell)
    assert g.cell(cell) == 0
    assert g.count_brightness() == 0


def test_toggle():
    g = LightGrid(3, 3)
    cell = Cell(1, 2)
    g.toggle(cell)
    assert g.cell(cell) is 2
    assert g.count_brightness() == 2


def test_toggle_group():
    g = LightGrid(3, 3)
    start_cell, end_cell = Cell(1, 2), Cell(2, 2)
    g.toggle_group(start_cell, end_cell)
    _assert_cells(g, start_cell, end_cell, 2)
    assert g.count_brightness() == 4


def _assert_cells(grid, start_cell, end_cell, expected_cell_state):
    for curr_i in range(start_cell.i, end_cell.i + 1):
        for curr_j in range(start_cell.j, end_cell.j + 1):
            assert grid.cell(Cell(curr_i, curr_j)) is expected_cell_state

