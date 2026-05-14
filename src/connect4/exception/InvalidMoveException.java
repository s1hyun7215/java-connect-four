package connect4.exception;

 // 게임에서 잘못된 수를 두려고 할 때 발생하는 예외. (가득 찬 열에 두기, 보드 범위 밖, EMPTY를 둠)

public class InvalidMoveException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidMoveException(String message) {
        super(message);
    }
    
    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}