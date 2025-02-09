package com.sudokusolver.Sudoku.service;

import java.util.Random;


public class SudokuSolver {
    private int size;
    private int subgridSize;
    private Random random = new Random();

    public SudokuSolver(int size) {
        this.size = size;
        this.subgridSize = (int) Math.sqrt(size);
    }

    // Method to solve Sudoku using backtracking
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

    // Check if placing 'num' in board[row][col] is valid
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

    // Generate a valid Sudoku puzzle with given clues
    public int[][] generatePuzzle(int clues) {
        int[][] grid = new int[size][size];
        fillDiagonalGrids(grid); // Ensure a valid base
        solve(grid); // Solve the grid to create a full valid Sudoku

        // Remove numbers to create the puzzle
        removeNumbers(grid, size * size - clues);

        return grid;
    }

    // Fill diagonal 3x3 subgrids first for better generation
    private void fillDiagonalGrids(int[][] grid) {
        for (int k = 0; k < size; k += subgridSize) {
            fillSubgrid(grid, k, k);
        }
    }

    // Fill a subgrid with unique numbers
    private void fillSubgrid(int[][] grid, int row, int col) {
        boolean[] used = new boolean[size + 1];
        for (int i = 0; i < subgridSize; i++) {
            for (int j = 0; j < subgridSize; j++) {
                int num;
                do {
                    num = random.nextInt(size) + 1;
                } while (used[num]);
                used[num] = true;
                grid[row + i][col + j] = num;
            }
        }
    }

    // Remove numbers while keeping the puzzle solvable
    private void removeNumbers(int[][] grid, int cellsToRemove) {
        while (cellsToRemove > 0) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (grid[row][col] != 0) {
                int temp = grid[row][col];
                grid[row][col] = 0;

                int[][] tempGrid = copyGrid(grid);
                if (!hasUniqueSolution(tempGrid)) {
                    grid[row][col] = temp; // Revert if solution isn't unique
                } else {
                    cellsToRemove--;
                }
            }
        }
    }

    // Check if a Sudoku puzzle has a unique solution
    private boolean hasUniqueSolution(int[][] board) {
        return countSolutions(board, 0, 0) == 1;
    }

    // Count the number of valid solutions (for uniqueness check)
    private int countSolutions(int[][] board, int row, int col) {
        if (row == size) return 1;
        if (col == size) return countSolutions(board, row + 1, 0);

        if (board[row][col] != 0) return countSolutions(board, row, col + 1);

        int count = 0;
        for (int num = 1; num <= size; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                count += countSolutions(board, row, col + 1);
                if (count > 1) break; // Stop if more than one solution is found
                board[row][col] = 0;
            }
        }
        return count;
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
