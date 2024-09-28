package sudoku.model;

import javafx.beans.Observable;
import javafx.beans.property.*;

public class Cell {
    private final IntegerProperty valueCase = new SimpleIntegerProperty(); // le nb qu'il y a dans une case
    private final ObjectProperty<CellStateEnum> cellStateEnum = new SimpleObjectProperty<>(CellStateEnum.FIXED);

    public Cell() {}
    public Cell(int valueCase) {
       // this.valueCase = valueCase;
        this.valueCase.set(valueCase);
    }

    // ---------Getter & setter ---------------------


    public int getValueCase() {
        return valueCase.get();
    }

    // Envoyer la valeur, le num dans une case
    public void setValueCase(int valueCase) {
        this.valueCase.set(valueCase);
    }
    public IntegerProperty valueCaseProperty() {
        return valueCase; // Retourner la propriété observable
    }

    void setCellStateEnum(CellStateEnum cellStateEnum) {
        this.cellStateEnum.setValue(cellStateEnum);
    }
    ReadOnlyObjectProperty<CellStateEnum> cellStateEnumProperty() { return cellStateEnum;}


    boolean isEditable() {
        return cellStateEnum.get() == CellStateEnum.EDITABLE;
    }


}
