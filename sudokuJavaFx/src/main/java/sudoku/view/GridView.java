package sudoku.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sudoku.viewModel.BoardViewModel;
import sudoku.viewModel.GridViewModel;

public class GridView extends GridPane {
    private static final int PADDING = 20;
    private static final int GRID_WIDTH = BoardViewModel.gridWidth();

    public GridView(GridViewModel gridViewModel, DoubleBinding gridWidth) {

       // setStyle("-fx-background-color: lightgrey");
        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));

        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(GRID_WIDTH);

        // Remplissage de la grille
        for (int i = 0; i < GRID_WIDTH ; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                CellView cellView = new CellView(gridViewModel.getCellViewModel(i,j) , cellWidth);
                add(cellView, j, i); // lignes/colonnes inversées dans gridpane
            }
        }
    }
}
