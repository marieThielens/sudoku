package sudoku.viewModel;

import javafx.beans.property.ReadOnlyObjectProperty;
import sudoku.model.Board;
import sudoku.model.CellStateEnum;

public class CellViewModel {
    private final int line;
    private final int col;
    private final Board board;

    public CellViewModel(int line, int col, Board board) {
        this.line = line;
        this.col = col;
        this.board = board;
    }
    // ------------ Getter & setter -----------------

    // Pour connaitre le nombre qu'il y a dans la case
    public int getValue() {
        return board.getCellValue(line, col);
    }
    // Envoyer le num entré par l'user
    public void setValue(int value) {
        board.setValue(line, col, value);
    }
    // valeur de l'enum
    public ReadOnlyObjectProperty<CellStateEnum> valueEnumProperty(){
        return board.valueEnumProperty(line, col);
    }


}
