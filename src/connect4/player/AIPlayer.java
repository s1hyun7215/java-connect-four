package connect4.player;

import connect4.ai.Strategy;
import connect4.exception.InvalidMoveException;
import connect4.model.Board;
import connect4.model.Piece;



public class AIPlayer extends Player {
    
    private final Strategy strategy;
    
    public AIPlayer(String name, Piece piece, Strategy strategy) {
        super(name, piece);
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy는 null일 수 없습니다.");
        }
        this.strategy = strategy;
    }
    
    @Override
    public int decideMove(Board board) throws InvalidMoveException {
        return strategy.decideMove(board, piece);
    }
    
    public Strategy getStrategy() {
        return strategy;
    }
    
    @Override
    public String toString() {
        return name + "(" + piece + ", " + strategy.getDifficultyName() + ")";
    }
}