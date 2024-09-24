package sudoku;

import javafx.application.Application;
import javafx.stage.Stage;
import sudoku.model.Board;
import sudoku.view.BoardView;
import sudoku.viewModel.BoardViewModel;

public class SudokuApp extends Application  {

    @Override
    public void start(Stage primaryStage) {
        Board board = new Board();
        BoardViewModel vm = new BoardViewModel(board);
        new BoardView(primaryStage, vm);
    }

    public static void main(String[] args) {
        launch();
    }

}
