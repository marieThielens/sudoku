package sudoku.viewModel;

import sudoku.model.Board;

public class GridViewModel {

    private final Board board;

    public GridViewModel(Board board) {
        this.board = board;
    }
    // ------- getter --------------
    public CellViewModel getCellViewModel(int line, int col) {
        return new CellViewModel(line, col, board);
    }
}
