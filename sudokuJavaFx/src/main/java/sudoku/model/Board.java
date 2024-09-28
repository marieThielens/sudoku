package sudoku.model;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final Grid grid = new Grid();
    private BooleanProperty isFull = new SimpleBooleanProperty(false);
    // pour le ctr -Z, va stocker des copies profondes de l'objet grid
    private List<Grid> history = new ArrayList<>();

    public Board(){
        isFull.bind(grid.countCellFilledProperty().isEqualTo(0));
    }

    // récupérer le num qu'il y a dans la case
    public int getCellValue(int line, int col){
        return grid.getSudokuGameTab()[line][col].getValueCase();
    }
    // l'enum en lecture
    public ReadOnlyObjectProperty<CellStateEnum> valueEnumProperty(int line, int col) {
        return grid.valueEnumProperty(line, col);
    }

    // Envoie chaque nouvelle valeur dans une case
    public void setValue(int line, int col, int newValue) {
        // Sauvegarder l'état actuel de la grille avant de le modifier (ctrl z)
        history.add(copyCurrentGrid());
        
        grid.getSudokuGameTab()[line][col].setValueCase(newValue);
    }
    // Méthode pour récupérer le nombre de cases remplies
    public LongBinding countCellFilledProperty() {
        return grid.countCellFilledProperty();
    }
    // Méthode pour récupérer l'état plein/vide de la grille
    public BooleanProperty isFullProperty() {
        return isFull;
    }

    public Cell[][] getSudokuGameTab() {
        return grid.getSudokuGameTab();
    }
    public Cell[][] getSudokuSoluceTab(){
        return grid.getSudokuSoluceTab();
    }

    // copie profonde pour copier l'état actuel de la grille
    private Grid copyCurrentGrid() {
         Grid newGrid = new Grid();
        // Parcourir chaque ligne et colonne de la grille actuelle
        for (int i = 0; i < Grid.getGridWidth(); i++) {
            for (int j = 0; j < Grid.getGridWidth() ; j++) {
                // Obtenir la cellule actuelle de la grille du jeu
                Cell currentCell = this.grid.getSudokuGameTab()[i][j];

                // Créez une nouvelle cellule avec la même valeur et état
                Cell newCell = new Cell(currentCell.getValueCase());
              //  newCell.setCellStateEnum(currentCell.getCellStateEnum());

                // Affecter la nouvelle cellule à la nouvelle grille
                newGrid.getSudokuGameTab()[i][j] = newCell;
            }
        }
        return newGrid;
    }
    public void undo() {
        if(history.size() > 1) {
            // supprimer le dernier
            history.remove(history.size() -1);
            Grid previouGrid = history.get(history.size() - 1);
            grid.setGrid(previouGrid);
        }
    }

}
