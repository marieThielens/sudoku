package sudoku.viewModel;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.Grid;

import java.io.*;
import java.util.ArrayList;

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

    // sauvegarder la partie --------------------
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

    // Ouvrir le fichier
    public void openGameFile(File file) {
        if(file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                ArrayList<String> linesList = new ArrayList<>();
                String line;

                // Lire toutes les lignes du fichier et les ajouter dans l'ArrayList
                while ((line = reader.readLine()) != null) {
                    linesList.add(line);
                }

                int lineIndex = 0;
                Cell[][] loadedSolution = new Cell[Grid.getGridWidth()][Grid.getGridWidth()];
                Cell[][] loadedGame = new Cell[Grid.getGridWidth()][Grid.getGridWidth()];

                // Parcourir les lignes pour charger la grille solution et la grille de jeu
                while (lineIndex < linesList.size()) {
                    line = linesList.get(lineIndex);

                    // Charger la solution
                    if (line.startsWith("Sudoku Solution:")) {
                        lineIndex++;
                        for (int i = 0; i < Grid.getGridWidth(); i++) {
                            String[] values = linesList.get(lineIndex).split(" ");
                            for (int j = 0; j < Grid.getGridWidth(); j++) {
                                loadedSolution[i][j] = new Cell(Integer.parseInt(values[j]));
                            }
                            lineIndex++;
                        }
                        continue;
                    }
                    // Charger la solution
                    if (line.startsWith("Sudoku Game:")) {
                        lineIndex++;
                        for (int i = 0; i < Grid.getGridWidth(); i++) {
                            String[] values = linesList.get(lineIndex).split(" ");
                            for (int j = 0; j < Grid.getGridWidth(); j++) {
                                loadedGame[i][j] = new Cell(Integer.parseInt(values[j]));
                            }
                            lineIndex++;
                        }
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.err.println("Erreur lors de la lecture d'un nombre dans le fichier.");
                e.printStackTrace();
            }
        }

    }

    public void undo(){
        board.undo();
    }


}
