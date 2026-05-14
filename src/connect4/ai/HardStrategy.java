package connect4.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import connect4.model.Board;
import connect4.model.Piece;
import connect4.exception.InvalidMoveException;


 // Hard - Minimax 일정 깊이까지 가능한 수를 모두 시뮬레이션해 최적의 수를 선택

public class HardStrategy implements Strategy {
    
    private static final int MAX_DEPTH = 3;
    private static final int WIN_SCORE = 1000000;
    private final Random random = new Random();
    
    @Override
    public int decideMove(Board board, Piece myPiece) throws InvalidMoveException {
    	List<Integer> validCols = board.getValidColumns();
        
        if (validCols.isEmpty()) {
            throw new InvalidMoveException("둘 수 있는 열이 없습니다.");
        }
        
        Piece opponentPiece = (myPiece == Piece.RED) ? Piece.YELLOW : Piece.RED;
        
        int bestScore = Integer.MIN_VALUE;
        List<Integer> bestCols = new ArrayList<>();
        
        // 각 열에 둬보고 점수 계산
        for (int col : validCols) {
            Board sim = board.copy();
            int row;
            try {
                row = sim.dropPiece(col, myPiece);
            } catch (InvalidMoveException e) {
                continue;
            }
            
            int score;
            if (sim.checkWin(row, col)) {
                score = WIN_SCORE;
            } else {
                score = minimax(sim, MAX_DEPTH - 1, false, myPiece, opponentPiece);
            }
            
            if (score > bestScore) {
                bestScore = score;
                bestCols.clear();
                bestCols.add(col);
            } else if (score == bestScore) {
                bestCols.add(col);
            }
        }
        
        // 최고점이 동점이면 그중 랜덤
        return bestCols.get(random.nextInt(bestCols.size()));
    }
    
    @Override
    public String getDifficultyName() {
        return "어려움";
    }
    
     // Minimax 재귀. true면 AI 차례, false면 상대 차례
    
    private int minimax(Board board, int depth, boolean isMaximizing,
                        Piece myPiece, Piece opponentPiece) {
        // 종료 조건
        if (depth == 0 || board.isFull()) {
            return evaluate(board, myPiece, opponentPiece);
        }
        
        Piece currentPiece = isMaximizing ? myPiece : opponentPiece;
        
        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (int col = 0; col < Board.COLS; col++) {
                if (board.isColumnFull(col)) continue;
                
                Board sim = board.copy();
                try {
                    int row = sim.dropPiece(col, currentPiece);
                    int score;
                    if (sim.checkWin(row, col)) {
                        score = WIN_SCORE - (MAX_DEPTH - depth);  // 빨리 이기는 수 선호
                    } else {
                        score = minimax(sim, depth - 1, false, myPiece, opponentPiece);
                    }
                    maxScore = Math.max(maxScore, score);
                } catch (InvalidMoveException e) {
                    // 무시
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int col = 0; col < Board.COLS; col++) {
                if (board.isColumnFull(col)) continue;
                
                Board sim = board.copy();
                try {
                    int row = sim.dropPiece(col, currentPiece);
                    int score;
                    if (sim.checkWin(row, col)) {
                        score = -WIN_SCORE + (MAX_DEPTH - depth);  // 빨리 지면 더 나쁨
                    } else {
                        score = minimax(sim, depth - 1, true, myPiece, opponentPiece);
                    }
                    minScore = Math.min(minScore, score);
                } catch (InvalidMoveException e) {
                    // 무시
                }
            }
            return minScore;
        }
    }
    
    /*
     * 보드 평가 함수: 내가 유리할수록 큰 점수.
     * - 가운데 열 가중치 (+)
     * - 가로/세로/대각선의 4칸 윈도우마다 점수
     */
    private int evaluate(Board board, Piece myPiece, Piece opponentPiece) {
        int score = 0;
        int centerCol = Board.COLS / 2;
        
        // 1) 가운데 열 가중치
        for (int r = 0; r < Board.ROWS; r++) {
            if (board.getCell(r, centerCol) == myPiece) score += 3;
            else if (board.getCell(r, centerCol) == opponentPiece) score -= 3;
        }
        
        // 2) 가로 4칸 평가
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c <= Board.COLS - 4; c++) {
                Piece[] window = new Piece[4];
                for (int i = 0; i < 4; i++) {
                    window[i] = board.getCell(r, c + i);
                }
                score += evaluateWindow(window, myPiece, opponentPiece);
            }
        }
        
        // 3) 세로 4칸 평가
        for (int c = 0; c < Board.COLS; c++) {
            for (int r = 0; r <= Board.ROWS - 4; r++) {
                Piece[] window = new Piece[4];
                for (int i = 0; i < 4; i++) {
                    window[i] = board.getCell(r + i, c);
                }
                score += evaluateWindow(window, myPiece, opponentPiece);
            }
        }
        
        // 4) 대각선 \ 4칸 평가
        for (int r = 0; r <= Board.ROWS - 4; r++) {
            for (int c = 0; c <= Board.COLS - 4; c++) {
                Piece[] window = new Piece[4];
                for (int i = 0; i < 4; i++) {
                    window[i] = board.getCell(r + i, c + i);
                }
                score += evaluateWindow(window, myPiece, opponentPiece);
            }
        }
        
        // 5) 대각선 / 4칸 평가
        for (int r = 0; r <= Board.ROWS - 4; r++) {
            for (int c = 3; c < Board.COLS; c++) {
                Piece[] window = new Piece[4];
                for (int i = 0; i < 4; i++) {
                    window[i] = board.getCell(r + i, c - i);
                }
                score += evaluateWindow(window, myPiece, opponentPiece);
            }
        }
        
        return score;
    }


     // 4칸 하나를 평가 같은 색이 많을수록 점수가 가파르게 증가

    private int evaluateWindow(Piece[] window, Piece myPiece, Piece opponentPiece) {
        int myCount = 0;
        int oppCount = 0;
        int emptyCount = 0;
        
        for (Piece p : window) {
            if (p == myPiece) myCount++;
            else if (p == opponentPiece) oppCount++;
            else emptyCount++;
        }
        
        // 내 말과 상대 말이 섞여있으면 의미 없음 (4목 불가)
        if (myCount > 0 && oppCount > 0) return 0;
        
        // 내가 유리한 상황
        if (myCount == 4) return 100;        // 4목
        if (myCount == 3 && emptyCount == 1) return 5;   // 3개 + 빈칸 1 = 다음 수 위협
        if (myCount == 2 && emptyCount == 2) return 2;   // 2개 + 빈칸 2 = 잠재력
        
        // 상대가 유리한 상황
        if (oppCount == 4) return -100;
        if (oppCount == 3 && emptyCount == 1) return -4;  // 막아야 함
        if (oppCount == 2 && emptyCount == 2) return -1;
        
        return 0;
    }
    
}