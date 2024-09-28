package sudoku.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sudoku.viewModel.BoardViewModel;

import java.io.File;

public class BoardView  extends BorderPane {
    private final BoardViewModel boardViewModel;

    // Constantes de mise en page
    private static final int GRID_WIDTH = BoardViewModel.gridWidth();
    private static final int SCENE_MIN_WIDTH = 450;
    private static final int SCENE_MIN_HEIGHT = 450;

    // Composants principaux
    private final VBox headerBox = new VBox();
    private final Label titleLabel = new Label("Jeu du sudoku");
    private final Label nbCaseLabel = new Label("");

    private static String buttonName(int i) {
        return Integer.toString(i);
    }
    private final Button undoButton = new Button("Undo");
    // Menu déroulant
    private final MenuBar dropDownMenu = new MenuBar(); // barre de déroulement
    private final Menu menu = new Menu("Fichier");
    private final MenuItem newGame = new MenuItem("Nouvelle partie");
    private final MenuItem openGame = new MenuItem("Ouvrir");
    private final MenuItem saveAs = new MenuItem("Sauvegarder");
    private final MenuItem exit = new MenuItem("Exit");

    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;
        start(primaryStage);
    }
    private void start(Stage primaryStage) {

        // Dimensions de la fenetre
        Scene scene = new Scene(this, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        primaryStage.setTitle("Sudoku by Marie");
        primaryStage.setScene(scene);
        primaryStage.show(); // afficher la fenetre
        // ne pas pouvoir réduire la fenetre en dessous de sa taille actuelle
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());

        createHeader();
        createGrid();
        createRightDiv();

        // ecouter les changement si full passe à true
        boardViewModel.isFullProperty().addListener((observable, oldvalue, newVal) -> {
            if(newVal){ createAlert();}
        });
    }

    private void createHeader(){
        // menu déroulant
        menu.getItems().addAll(newGame, openGame, saveAs, exit);
        dropDownMenu.getMenus().add(menu);
        // texte
        nbCaseLabel.textProperty().bind(boardViewModel.countCellFilledProperty().asString("Nombre de case restante %d"));
        headerBox.getChildren().addAll(dropDownMenu, titleLabel, nbCaseLabel);
        headerBox.setAlignment(Pos.CENTER);
        setTop(headerBox);
        menuOption();
    }
    private void createGrid(){
        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get(), heightProperty().get() - headerBox.heightProperty().get());
                    return Math.floor(size / GRID_WIDTH) * GRID_WIDTH;
                },
                widthProperty(),
                heightProperty(),
                headerBox.heightProperty());

        GridView gridView = new GridView(boardViewModel.getGridViewModel(), gridWidth);
        setCenter(gridView);
    }

    private void createRightDiv(){
        VBox containerDroite = new VBox();
        GridPane gridPaneButton = new GridPane();

        // Ajouter un espace entre les boutons
        gridPaneButton.setHgap(10);
        gridPaneButton.setVgap(10);

        for(int i = 1; i <= 9; ++i) {
            Button button = new Button(buttonName(i));
            button.setPrefSize(30, 40); // taille des boutons
            // Enlever le contour des boutons avec CSS
            button.setStyle("-fx-background-color: lightblue; -fx-border-color: transparent; -fx-focus-color: transparent;");
            // Ajout des boutons au GridPane, 3 boutons par ligne
            gridPaneButton.add(button, (i -1) % 3, (i -1) / 3);
        }
        containerDroite.getChildren().addAll(gridPaneButton, undoButton);

        setRight(containerDroite);
    }
    private void createAlert() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Test");
        confirmation.showAndWait(); // afficher l'alerte
    }
    private void menuOption() {

        saveAs.setOnAction(e-> {
            // Créer une instance de FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder partie");

            // Définir les filtres de type de fichier
            FileChooser.ExtensionFilter xsbFilter = new FileChooser.ExtensionFilter("XSB Files", "*.xsb");
            fileChooser.getExtensionFilters().add(xsbFilter);

            // Ouvrir la boîte de dialogue pour choisir le fichier
            File file = fileChooser.showSaveDialog(getScene().getWindow());

            if(file != null) {
                boardViewModel.saveGameFile(file);
            }
        });

        exit.setOnAction(e -> {
            // pour fermer l'application
            Stage stage = (Stage) getScene().getWindow();
            stage.close();
        });

        openGame.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XSB Files", "*.xsb"));
            // Ouvrir la boîte de dialogue pour choisir le fichier
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if(selectedFile != null ) {
                boardViewModel.openGameFile(selectedFile);
            }
        });
        undoButton.setOnAction(e-> {
            boardViewModel.undo();
        });

    }

}
