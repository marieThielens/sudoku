package sudoku.model;

public enum CellStateEnum {
    FIXED, // La case est préremplie et ne peut pas etre modifiée
    EDITABLE, // case modifiable par l'utilisateur
    NUMBER
}
