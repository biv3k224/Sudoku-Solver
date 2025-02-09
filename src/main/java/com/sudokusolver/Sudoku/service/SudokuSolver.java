package com.sudokusolver.Sudoku.service;

import java.util.Random;

public class SudokuSolver {
    private int size; // Size of the Sudoku grid (e.g., 9 for 9x9)
    private int subgridSize; // Size of the subgrid (e.g., 3 for 9x9)

    public SudokuSolver(int size) {
        this.size = size;
        this.subgridSize = (int) Math.sqrt(size);
    }

    // Method to solve the Sudoku puzzle
    public boolean solve(int[][] board) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= size; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;

                            if (solve(board)) {
                                return true;
                            }

                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // Method to check if a number is valid in a given cell
    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < size; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        int startRow = row - row % subgridSize;
        int startCol = col - col % subgridSize;
        for (int i = 0; i < subgridSize; i++) {
            for (int j = 0; j < subgridSize; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    // Method to generate a Sudoku puzzle
    public int[][] generatePuzzle(int clues) {
        int[][] grid = new int[size][size];
        Random random = new Random();

        // Fill the grid with a valid Sudoku solution
        solve(grid);

        // Remove numbers to create the puzzle
        int cellsToRemove = size * size - clues;
        while (cellsToRemove > 0) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (grid[row][col] != 0) {
                int temp = grid[row][col];
                grid[row][col] = 0;

                // Check if the puzzle still has a unique solution
                int[][] tempGrid = copyGrid(grid);
                SudokuSolver tempSolver = new SudokuSolver(size);
                if (tempSolver.solve(tempGrid)) {
                    cellsToRemove--;
                } else {
                    grid[row][col] = temp; // Revert if the solution is not unique
                }
            }
        }

        return grid;
    }

    // Helper method to copy a grid
    private int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, size);
        }
        return copy;
    }
}