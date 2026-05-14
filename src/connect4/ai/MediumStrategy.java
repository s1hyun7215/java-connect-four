package connect4.ai;

import java.util.List;
import java.util.Random;

import connect4.model.Board;
import connect4.model.Piece;
import connect4.exception.InvalidMoveException;


 // Medium - 1수 앞을 본다

public class MediumStrategy implements Strategy {
    
    private static final int CENTER_COL = Board.COLS / 2;  // 3
    private final Random random = new Random();
    
    @Override
    public int decideMove(Board board, Piece myPiece) throws InvalidMoveException {
    	List<Integer> validCols = board.getValidColumns();
        
        if (validCols.isEmpty()) {
            throw new InvalidMoveException("둘 수 있는 열이 없습니다.");
        }
        
        Piece opponentPiece = (myPiece == Piece.RED) ? Piece.YELLOW : Piece.RED;
        
        // 1) 내가 이기는 수가 있으면 둠
        for (int col : validCols) {
            if (board.wouldWin(col, myPiece)) {
                return col;
            }
        }
        
        // 2) 상대가 이기는 수를 막을 수 있으면 막음
        for (int col : validCols) {
            if (board.wouldWin(col, opponentPiece)) {
                return col;
            }
        }
        
        // 3) 가운데 열이 비어있으면 가운데 둠
        if (validCols.contains(CENTER_COL)) {
            return CENTER_COL;
        }
        
        // 4) 랜덤
        return validCols.get(random.nextInt(validCols.size()));
    }
    
    @Override
    public String getDifficultyName() {
        return "보통";
    }
    
}