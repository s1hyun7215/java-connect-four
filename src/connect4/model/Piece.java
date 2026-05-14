package connect4.model;

public enum Piece {
    EMPTY("·"),
    RED("●"),
    YELLOW("○");
    
    private final String symbol;
    
    Piece(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
}