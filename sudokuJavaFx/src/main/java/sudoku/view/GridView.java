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
        //setGridLinesVisible(true);
        setPadding(new Insets(PADDING));

        DoubleBinding cellWidth = gridWidth.subtract(PADDING * 2).divide(GRID_WIDTH);

        // Remplissage de la grille
        for (int i = 0; i < GRID_WIDTH ; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {

                CellView cellView = new CellView(gridViewModel.getCellViewModel(i,j) , cellWidth);

                // Style par défaut de la bordure
                StringBuilder borderStyle = new StringBuilder("-fx-border-color: black; -fx-border-width: 0.2px;");

                // Ajouter des bordures horizontale
                if (i % 3 == 0 && i != 0) { // Bord supérieur des blocs 3x3 (sauf la première ligne)
                    borderStyle.append("-fx-border-color: lightblue black black black; -fx-border-width: 3px 0.2 0.2 0.2;");
                }
                if (j % 3 == 0 && j != 0) { // Bord gauche des blocs 3x3 (sauf la première colonne)
                    borderStyle.append("-fx-border-color: black black black lightblue; -fx-border-width:0.2 0.2 0.2 3px;");
                }

                // Appliquer le style à la CellView
                cellView.setStyle(borderStyle.toString());

                add(cellView, j, i); // lignes/colonnes inversées dans gridpane
            }
        }
    }
}
