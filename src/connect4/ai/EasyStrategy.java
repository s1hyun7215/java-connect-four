package connect4.ai;

import java.util.List;
import java.util.Random;

import connect4.model.Board;
import connect4.model.Piece;
import connect4.exception.InvalidMoveException;


 // Easy - 둘 수 있는 열 중에서 랜덤 선택

public class EasyStrategy implements Strategy {
    
    private final Random random = new Random();
    
    @Override
    public int decideMove(Board board, Piece myPiece) throws InvalidMoveException {
    	List<Integer> validCols = board.getValidColumns();
        
        if (validCols.isEmpty()) {
            throw new InvalidMoveException("둘 수 있는 열이 없습니다.");
        }
        
        // 랜덤하게 하나 선택
        int randomIndex = random.nextInt(validCols.size());
        return validCols.get(randomIndex);
    }
    
    @Override
    public String getDifficultyName() {
        return "쉬움";
    }

}