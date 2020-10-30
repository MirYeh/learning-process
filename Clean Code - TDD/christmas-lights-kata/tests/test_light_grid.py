import pytest

from christmas_lights.light_grid import LightGrid, Cell


def test_cell_turn_off():
    g = LightGrid(1, 1)
    g.turn_on(Cell(0, 0))
    assert g.cell(Cell(0, 0)) is True
    g.turn_off(Cell(0, 0))
    assert g.cell(Cell(0, 0)) is False


def test_cell_turn_group_off():
    length = width = 3
    g = LightGrid(length, width)
    start_cell, end_cell = Cell(0, 0), Cell(2, 2)
    g.turn_group_on(start_cell, end_cell)
    _assert_cells(g, start_cell, end_cell, True)
    g.turn_group_off(start_cell, end_cell)
    _assert_cells(g, start_cell, end_cell, False)


def test_toggle():
    g = LightGrid(3, 3)
    cell = Cell(1, 2)
    g.toggle(cell)
    assert g.cell(cell) is True
    g.toggle(cell)
    assert g.cell(cell) is False


def test_toggle_group():
    g = LightGrid(3, 3)
    start_cell, end_cell = Cell(1, 2), Cell(2, 2)
    g.toggle_group(start_cell, end_cell)
    _assert_cells(g, start_cell, end_cell, True)
    g.toggle_group(start_cell, end_cell)
    _assert_cells(g, start_cell, end_cell, False)


def _assert_cells(grid, start_cell, end_cell, expected_state):
    for curr_i in range(start_cell.i, end_cell.i + 1):
        for curr_j in range(start_cell.j, end_cell.j + 1):
            assert grid.cell(Cell(curr_i, curr_j)) is expected_state

