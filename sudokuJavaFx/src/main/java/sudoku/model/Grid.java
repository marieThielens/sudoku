package sudoku.model;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.Arrays;
import java.util.Random;


public class Grid {
    static final int GRID_WIDTH = 9;
    static final int MAX_NUMBER = 9;

    private final Cell[][] sudokuSoluce;
    private final Cell[][] sudokuGame;
    private static final Random random = new Random();
    private final LongBinding countCellFilled;


    public Grid() {
        sudokuSoluce = new Cell[GRID_WIDTH][GRID_WIDTH];
        sudokuGame = new Cell[GRID_WIDTH][GRID_WIDTH];

        if (!completeGrid(0, 0)) {
            throw new RuntimeException("Impossible de remplir la grille avec une solution valide.");
        }
        // Copie de la solution dans la grille de jeu
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                sudokuGame[i][j] = new Cell(sudokuSoluce[i][j].getValueCase());
                //addCellValueChangeListener(sudokuGame[i][j]); // méthode pour écouter le changement
            }
        }
        deleteNumber();

        // compter le nombre de case à remplir restantes
        countCellFilled = Bindings.createLongBinding(() -> {
            int filledEditableCells = 0; // Compteur pour les cellules éditables remplies

            // Parcourir la grille
            for (int i = 0; i < GRID_WIDTH; i++) {
                for (int j = 0; j < GRID_WIDTH; j++) {
                    Cell cell = sudokuGame[i][j];

                    // Si la cellule est éditable et qu'elle a une valeur non nulle, elle est remplie
                    if (cell.isEditable() && cell.getValueCase() != 0) {
                        filledEditableCells++;
                    }
                }
            }

            // Commence à 20 et décrémente selon le nombre de cases remplies
            return 20L - filledEditableCells;

        }, Arrays.stream(sudokuGame)
                .flatMap(Arrays::stream)
                .map(Cell::valueCaseProperty)
                .toArray(Observable[]::new));
    }


    //------------ getter & setter ---------------

    public static int getGridWidth(){ return GRID_WIDTH;} // La grille fait 9 x 9
    public Cell[][] getSudokuSoluceTab() {
        return sudokuSoluce;
    }
    public Cell[][] getSudokuGameTab() {
        return sudokuGame;
    }


    ReadOnlyObjectProperty<CellStateEnum> valueEnumProperty(int line, int col) {
        return sudokuGame[line][col].cellStateEnumProperty();
    }
    public LongBinding countCellFilledProperty(){
        return countCellFilled;
    }


    //-------------------- Méthodes -------------------------------

    private boolean completeGrid(int line, int column) {
        if (column == GRID_WIDTH) {
            column = 0;
            line++;
            if (line == GRID_WIDTH) {
                return true; // La grille est remplie
            }
        }

        int[] numbers = getShuffledNumbers(); // Obtenir une liste mélangée de 1 à 9

        for (int num : numbers) {
            if (isValidNumber(num, line, column)) {
                sudokuSoluce[line][column] = new Cell(num); // Assigner un nombre valide

                // Appeler récursivement pour la prochaine cellule
                if (completeGrid(line, column + 1)) {
                    return true; // Si on a rempli avec succès, terminer
                }

                // Si ça échoue, réinitialiser la cellule et essayer un autre nombre
                sudokuSoluce[line][column] = new Cell(0);
            }
        }
        return false; // Échec si aucun nombre valide n'a été trouvé
    }

    // Mélanger les nombres de 1 à 9
    private int[] getShuffledNumbers() {
        int[] numbers = new int[MAX_NUMBER];
        for (int i = 0; i < MAX_NUMBER; i++) {
            numbers[i] = i + 1;
        }

        // Mélanger les nombres
        for (int i = numbers.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = numbers[index];
            numbers[index] = numbers[i];
            numbers[i] = temp;
        }

        return numbers;
    }

    // Vérifie si un nombre est valide dans la cellule courante
    private boolean isValidNumber(int num, int line, int column) {
        return !isNumberInLine(num, line) && !isNumberInColumn(num, column) && !isNumberInBlock(num, line, column);
    }

    // est-ce que le chiffre existe déjà dans la ligne ?
    private boolean isNumberInLine(int num, int line) {
        for (int i = 0; i < GRID_WIDTH; i++) {
            if (sudokuSoluce[line][i] != null && sudokuSoluce[line][i].getValueCase() == num) {
                return true;
            }
        }
        return false;
    }

    // est-ce que le chiffre existe déjà dans la colonne ?
    private boolean isNumberInColumn(int num, int column) {
        for (int i = 0; i < GRID_WIDTH; i++) {
            if (sudokuSoluce[i][column] != null && sudokuSoluce[i][column].getValueCase() == num) {
                return true;
            }
        }
        return false;
    }

    // est-ce que le chiffre existe déjà dans le bloc 3x3 ?
    private boolean isNumberInBlock(int num, int line, int column) {
        int blockRowStart = (line / 3) * 3;
        int blockColStart = (column / 3) * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudokuSoluce[blockRowStart + i][blockColStart + j] != null &&
                        sudokuSoluce[blockRowStart + i][blockColStart + j].getValueCase() == num) {
                    return true;
                }
            }
        }
        return false;
    }
    // Enlever des chiffres et mettre des 0 à la place (qui seront remplacé pr du vide)
    private int deleteNumber() {
        int nb = 20;
        boolean isDeleteNotFinish = true;
        while(isDeleteNotFinish) {
            int line = getShuffledNumbers()[0] -1;
            int col = getShuffledNumbers()[0] -1;
            if(sudokuGame[line][col].getValueCase() != 0) {
                sudokuGame[line][col].setValueCase(0);
                // mettre l'enum à jour
                sudokuGame[line][col].setCellStateEnum(CellStateEnum.EDITABLE);
                nb--;
            }
            if(nb  == 0) {
                isDeleteNotFinish = false;
            }
        }
        return nb;
    }


    public void setGrid(Grid newGrid) {
        // Parcourir chaque ligne et chaque colonne
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                // Copier la valeur de la cellule depuis newGrid
                this.sudokuGame[i][j].setValueCase(newGrid.getSudokuGameTab()[i][j].getValueCase());

                //this.sudokuGame[i][j].setCellStateEnum(newGrid.getSudokuGameTab()[i][j].getCellStateEnum());

                // Si nécessaire, copier la solution du Sudoku aussi (si elle fait partie de l'objet Grid)
                this.sudokuSoluce[i][j].setValueCase(newGrid.getSudokuSoluceTab()[i][j].getValueCase());
            }
        }
    }

    // Méthode pour ajouter un listener à une cellule
//    private void addCellValueChangeListener(Cell cell) {
//        cell.valueCaseProperty().addListener((observable, oldValue, newValue) -> {
//            // Invalider le binding lorsqu'une valeur change
//            if (oldValue.intValue() != newValue.intValue()) {
//               // countCellFilled.invalidate();
//            }
//
//
//            //System.out.println("La valeur de la cellule est passée de " + oldValue + " à " + newValue);
//        });
//    }
}
