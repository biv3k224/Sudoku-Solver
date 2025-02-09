package com.sudokusolver.Sudoku.controller;


import org.springframework.web.bind.annotation.*;

import com.sudokusolver.Sudoku.service.SudokuSolver;

@RestController
@RequestMapping("/api/sudoku")
public class SudokuController {

    private final SudokuSolver solver;

    public SudokuController() {
        this.solver = new SudokuSolver(9); // Default size is 9x9
    }

    @GetMapping("/create")
    public int[][] createSudoku(@RequestParam(defaultValue = "30") int clues) {
        if (clues < 17 || clues > 81) {
            throw new IllegalArgumentException("Clues must be between 17 and 81.");
        }
        return solver.generatePuzzle(clues);
    }

  
    @PostMapping("/solve")
    public int[][] solveSudoku(@RequestBody int[][] grid) {
        if (solver.solve(grid)) {
            return grid; // Return the solved puzzle
        } else {
            throw new IllegalArgumentException("No solution exists for the given Sudoku puzzle.");
        }
    }
}
