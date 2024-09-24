package sudoku.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sudoku.model.CellStateEnum;
import sudoku.viewModel.CellViewModel;


public class CellView extends StackPane {

    private final CellViewModel cellViewModel;
    private final DoubleBinding cellWidthProperty;
    private final Label labelNumber = new Label();
    private final TextField numberUser = new TextField();
    private final Rectangle backgroundCaseColor = new Rectangle();


    public CellView(CellViewModel cellViewModel, DoubleBinding cellWidthProperty) {
        this.cellViewModel = cellViewModel;
        this.cellWidthProperty = cellWidthProperty;

        setAlignment(Pos.CENTER);

        getChildren().addAll(backgroundCaseColor, labelNumber, numberUser);
        configureBindings();
        updateLabel();
        numberUser.setVisible(false);
    }
    // Taille de chaque case
    private void configureBindings(){
        minWidthProperty().bind(cellWidthProperty);
        minHeightProperty().bind(cellWidthProperty);

        this.setOnMouseClicked(e -> {

            if(cellViewModel.valueEnumProperty().getValue() == CellStateEnum.EDITABLE) {
                backgroundCaseColor.setFill(Color.LIGHTBLUE);
                configureKeyInput(); // Taper des nombres
                this.requestFocus();
            }

        });
    }
    // Contenu d'une case
    private void updateLabel(){
        int value = cellViewModel.getValue();
        if(value != 0) {
            labelNumber.setText(String.valueOf(value));
            //labelNumber.setStyle("-fx-font-weight: bold;");
        } else {
            labelNumber.setText(""); // Si la valeur est 0, on n'affiche rien
        }
    }


    // Capture des événements clavier pour entrer une valeur dans la cellule
    private void configureKeyInput() {
        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            String input = event.getText();

            // Vérifier si la touche pressée est un chiffre entre 1 et 9
            if (input.matches("[1-9]")) {
                int value = Integer.parseInt(input);
                cellViewModel.setValue(value);  // Met à jour la valeur dans le ViewModel
                updateLabel();  // Met à jour l'affichage du label
            }

            // Empêcher les autres événements non pertinents
            event.consume();
        });
    }
}
