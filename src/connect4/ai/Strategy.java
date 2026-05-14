package connect4.ai;

import connect4.model.Board;
import connect4.model.Piece;
import connect4.exception.InvalidMoveException;


// AI의 수 결정 전략 인터페이스

public interface Strategy {
    
    /*
     * 현재 보드 상태에서 AI가 둘 열을 결정한다.
     * param board 현재 보드 / myPiece AI의 말 색깔
     * return 둘 열 번호
     */
    int decideMove(Board board, Piece myPiece) throws InvalidMoveException;
    
     // 난이도 이름 표시용
    String getDifficultyName();
}