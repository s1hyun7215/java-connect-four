package connect4.player;

import connect4.model.Board;
import connect4.model.Piece;
import connect4.exception.InvalidMoveException;


// GUI에서 마우스 클릭으로 입력받은 열 번호를 setNextMove()로 전달받고 decideMove()에서 그 값을 반환

public class HumanPlayer extends Player {
    
    private int nextMove = -1;  // 아직 입력 없음
    
    public HumanPlayer(String name, Piece piece) {
        super(name, piece);
    } 
    

     // GUI에서 사용자가 클릭한 열 번호를 미리 설정

    public void setNextMove(int col) {
        this.nextMove = col;
    }
    
    @Override
    public int decideMove(Board board) throws InvalidMoveException {
        if (nextMove < 0) {
            throw new InvalidMoveException("아직 입력이 없습니다.");
        }
        if (board.isColumnFull(nextMove)) {
            int invalid = nextMove;
            nextMove = -1;
            throw new InvalidMoveException(invalid + "번 열은 가득 찼습니다.");
        }
        
        int move = nextMove;
        nextMove = -1;
        return move;
    }
}