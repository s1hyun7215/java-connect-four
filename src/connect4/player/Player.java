package connect4.player;

import connect4.model.Board;
import connect4.model.Piece;
import connect4.exception.InvalidMoveException;



public abstract class Player {
    
    protected final String name;
    protected final Piece piece;
    
    public Player(String name, Piece piece) {
        if (piece == Piece.EMPTY) {
            throw new IllegalArgumentException("플레이어의 말은 EMPTY일 수 없습니다.");
        }
        this.name = name;
        this.piece = piece;
    }
    
    
     // 다음에 둘 열을 결정한다. param 현재 보드 상태 return 둘 열 번호
     
    public abstract int decideMove(Board board) throws InvalidMoveException;
    
    public String getName() {
        return name;
    }
    
    public Piece getPiece() {
        return piece;
    }
    
    @Override
    public String toString() {
        return name + "(" + piece + ")";
    }
}