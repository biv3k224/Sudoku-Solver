const API_BASE = "http://localhost:8080/api/sudoku";

document.addEventListener("DOMContentLoaded", () => {
    createGrid();
});

function createGrid() {
    const grid = document.getElementById("sudoku-grid");
    grid.innerHTML = ""; 
    for (let i = 0; i < 81; i++) {
        let cell = document.createElement("input");
        cell.type = "text";
        cell.classList.add("cell");
        cell.maxLength = 1;
        cell.oninput = (e) => validateInput(e);
        grid.appendChild(cell);
    }
}

function validateInput(event) {
    let value = event.target.value;
    if (!/^[1-9]$/.test(value)) event.target.value = "";
}

function getGridData() {
    let cells = document.querySelectorAll(".cell");
    let grid = [];
    for (let i = 0; i < 9; i++) {
        grid[i] = [];
        for (let j = 0; j < 9; j++) {
            let value = cells[i * 9 + j].value;
            grid[i][j] = value ? parseInt(value) : 0;
        }
    }
    return grid;
}

function setGridData(data) {
    let cells = document.querySelectorAll(".cell");
    for (let i = 0; i < 9; i++) {
        for (let j = 0; j < 9; j++) {
            cells[i * 9 + j].value = data[i][j] === 0 ? "" : data[i][j];
        }
    }
}

function generateSudoku() {
    fetch(`${API_BASE}/create?clues=30`)
        .then(response => response.json())
        .then(data => setGridData(data))
        .catch(error => console.error("Error generating puzzle:", error));
}

function solveSudoku() {
    let grid = getGridData();
    fetch(`${API_BASE}/solve`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(grid)
    })
    .then(response => response.json())
    .then(solution => animateSolution(solution))
    .catch(error => alert("No solution found!"));
}

function animateSolution(solution) {
    let cells = document.querySelectorAll(".cell");
    let index = 0;

    function fillNextCell() {
        while (index < 81 && cells[index].value !== "") {
            index++; // Skip pre-filled cells
        }

        if (index >= 81) return;

        let row = Math.floor(index / 9);
        let col = index % 9;
        let value = solution[row][col];

        cells[index].value = value;
        cells[index].style.backgroundColor = "#ffcc00"; // Highlight effect

        setTimeout(() => {
            cells[index].style.backgroundColor = "";
            index++; // Move to the next cell
            fillNextCell();
        }, 50); // Adjust speed of animation here
    }

    fillNextCell();
}

function clearGrid() {
    document.querySelectorAll(".cell").forEach(cell => cell.value = "");
}