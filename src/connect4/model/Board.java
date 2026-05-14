package connect4.model;

import connect4.exception.InvalidMoveException;
import java.util.ArrayList;
import java.util.List;

public class Board {
    
    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final int WIN_COUNT = 4;
    
    private final Piece[][] grid;
    
    public Board() {
        grid = new Piece[ROWS][COLS];
        // 모든 칸을 EMPTY로 초기화
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = Piece.EMPTY;
            }
        }
    }
    
    /*
     * 지정된 열에 말을 떨어뜨림
     * return 말이 놓인 행 인덱스
     * throws InvalidMoveException 열이 가득 찼거나 잘못된 열
     */
    public int dropPiece(int col, Piece piece) throws InvalidMoveException {
        if (col < 0 || col >= COLS) {
            throw new InvalidMoveException(
                "잘못된 열입니다: " + col + " (0~" + (COLS - 1) + " 사이여야 합니다)");
        }
        if (piece == Piece.EMPTY) {
            throw new InvalidMoveException("EMPTY는 놓을 수 없습니다.");
        }
        
        // 아래에서부터 빈 칸 찾기
        for (int r = ROWS - 1; r >= 0; r--) {
            if (grid[r][col] == Piece.EMPTY) {
                grid[r][col] = piece;
                return r;
            }
        }
        
        throw new InvalidMoveException(col + "번 열이 가득 찼습니다.");
    }
    

     // 해당 열이 가득 찼는지 확인
    public boolean isColumnFull(int col) {
        if (col < 0 || col >= COLS) return true; // 존재하지 않는 열 방어코드
        return grid[0][col] != Piece.EMPTY;  // 맨 위 칸이 차있으면 full
    }
    

     // 보드 전체가 다 찼는지 (무승부 판정)
    public boolean isFull() {
        for (int c = 0; c < COLS; c++) {
            if (!isColumnFull(c)) return false;
        }
        return true;
    }
    

     // 특정 칸 조회
    public Piece getCell(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return Piece.EMPTY;
        }
        return grid[row][col];
    }
    
    
    // 현재 보드에서 둘 수 있는 모든 열 목록을 반환
    public List<Integer> getValidColumns() {
        List<Integer> valid = new ArrayList<>();
        for (int c = 0; c < COLS; c++) {
            if (!isColumnFull(c)) {
                valid.add(c);
            }
        }
        return valid;
    }
    

     // 마지막에 놓인 말의 위치를 기준으로 승리 판정
     // return 4개 연속이면 true
    public boolean checkWin(int row, int col) {
        Piece piece = grid[row][col];
        if (piece == Piece.EMPTY) return false;
        
        // 4방향 검사
        int[][] directions = {
            {0, 1},   // 가로
            {1, 0},   // 세로
            {1, 1},   // 우하단
            {1, -1}   // 좌하단
        };
        
        for (int[] dir : directions) {
            int count = 1;  // 자기 자신 포함
            count += countDirection(row, col, dir[0], dir[1], piece);   // 한쪽 방향
            count += countDirection(row, col, -dir[0], -dir[1], piece); // 반대 방향
            
            if (count >= WIN_COUNT) return true;
        }
        return false;
    }
    

     // 특정 방향으로 같은 말이 몇 개 연속인지 세기
    private int countDirection(int row, int col, int dr, int dc, Piece piece) {
        int count = 0;
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && grid[r][c] == piece) {
            count++;
            r += dr;
            c += dc;
        }
        return count;
    }
    

     // 디버깅/콘솔 출력용
    public void print() {
        for (int c = 0; c < COLS; c++) {
            System.out.print(" " + c);
        }
        System.out.println();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                System.out.print(" " + grid[r][c].getSymbol());
            }
            System.out.println();
        }
    }
    

     // 보드 깊은 복사 반환 (AI 시뮬레이션용)
    public Board copy() {
        Board newBoard = new Board();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                newBoard.grid[r][c] = this.grid[r][c];
            }
        }
        return newBoard;
    }
    

     // 특정 열에 piece를 두면 승리하는지 시뮬레이션 (실제 보드 변경하지 않음)
    public boolean wouldWin(int col, Piece piece) {
        if (isColumnFull(col)) return false;
        
        Board sim = this.copy();
        try {
            int row = sim.dropPiece(col, piece);
            return sim.checkWin(row, col);
        } catch (InvalidMoveException e) {
            return false;
        }
    }
    
}