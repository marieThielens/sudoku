package sudoku.viewModel;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.Grid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BoardViewModel {
    private final Board board;
    private final GridViewModel gridViewModel;

    public BoardViewModel(Board board) {
        this.board = board;
        gridViewModel = new GridViewModel(board);
    }
    // ----------- Getter ---------
    public GridViewModel getGridViewModel() {
        return gridViewModel;
    }
    public static int gridWidth() { return Grid.getGridWidth(); }
    // Compter le nombre de case remplie
    public LongBinding countCellFilledProperty() {
        return board.countCellFilledProperty();
    }
    // vérifier si toute la grille est remplis
    public BooleanProperty isFullProperty() {
        return board.isFullProperty();
    }
    // sauvegarder la partie
    public void saveGameFile(File file){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            // Ecrire la solution
            writer.write("Sudoku Solution:");
            writer.newLine();
            writeGridToFile(writer, board.getSudokuSoluceTab());

            // Ecrire la grille de jeu
            writer.write("Sudoku Game:");
            writer.newLine();
            writeGridToFile(writer, board.getSudokuGameTab());
                //System.out.println("File saved: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    private void writeGridToFile(BufferedWriter writer, Cell[][] grid) throws IOException {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Écrire la valeur de la cellule
                writer.write(grid[i][j].getValueCase() + " ");
            }
            writer.newLine(); // Nouvelle ligne après chaque ligne de la grille
        }
    }

}
