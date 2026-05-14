package connect4;

import connect4.exception.InvalidMoveException;
import connect4.model.Board;
import connect4.model.Piece;
import connect4.player.AIPlayer;
import connect4.player.Player;


 // 게임 전체 진행 관리. 두 플레이어와 보드를 가지고 턴을 진행

public class Game {
    

     // 게임 상태
    public enum State {
        IN_PROGRESS,
        RED_WIN,
        YELLOW_WIN,
        DRAW
    }
    
    private final Board board;
    private final Player redPlayer;
    private final Player yellowPlayer;
    private Player currentPlayer;
    private State state;
    
    public Game(Player redPlayer, Player yellowPlayer) {
        if (redPlayer == null || yellowPlayer == null) {
            throw new IllegalArgumentException("플레이어는 null일 수 없습니다.");
        }
        if (redPlayer.getPiece() != Piece.RED || yellowPlayer.getPiece() != Piece.YELLOW) {
            throw new IllegalArgumentException("플레이어의 말 색깔이 잘못되었습니다.");
        }
        
        this.board = new Board();
        this.redPlayer = redPlayer;
        this.yellowPlayer = yellowPlayer;
        this.currentPlayer = redPlayer;  // red 선공
        this.state = State.IN_PROGRESS;
    }
    
    /*
     * 현재 플레이어의 한 턴을 진행한다.
     * return 말이 놓인 행 번호 (확장용 ex.마지막착수 하이라이트)
     * throws InvalidMoveException 잘못된 수 / IllegalStateException 게임이 이미 끝났을 때
     */
    public int playTurn() throws InvalidMoveException {
        if (state != State.IN_PROGRESS) {
            throw new IllegalStateException("게임이 이미 종료되었습니다.");
        }
        
        // 1) 현재 플레이어가 둘 열 결정
        int col = currentPlayer.decideMove(board);
        
        // 2) 보드에 말 놓기
        int row = board.dropPiece(col, currentPlayer.getPiece());
        
        // 3) 승리 판정
        if (board.checkWin(row, col)) {
            state = (currentPlayer.getPiece() == Piece.RED) 
                    ? State.RED_WIN 
                    : State.YELLOW_WIN;
        } else if (board.isFull()) {
            state = State.DRAW;
        } else {
            // 4) 턴 전환
            switchTurn();
        }
        
        return row;
    }
    

     // 턴 전환

    private void switchTurn() {
        currentPlayer = (currentPlayer == redPlayer) ? yellowPlayer : redPlayer;
    }
    

     // 현재 플레이어가 AI인지 확인 (UI 자동 진행 트리거에 사용)

    public boolean isCurrentPlayerAI() {
        return currentPlayer instanceof AIPlayer;
    }
    

     // 게임 종료 여부

    public boolean isOver() {
        return state != State.IN_PROGRESS;
    }
    

     // 게임 결과 메시지

    public String getResultMessage() {
        switch (state) {
            case RED_WIN:    return redPlayer.getName() + " 승리!";
            case YELLOW_WIN: return yellowPlayer.getName() + " 승리!";
            case DRAW:       return "무승부!";
            default:         return "진행 중";
        }
    }
    
    // Getter
    
    public Board getBoard() {
        return board;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public Player getRedPlayer() {
        return redPlayer;
    }
    
    public Player getYellowPlayer() {
        return yellowPlayer;
    }
    
    public State getState() {
        return state;
    }
}