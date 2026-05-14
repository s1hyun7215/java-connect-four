package connect4.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.IntConsumer;

import javax.swing.JPanel;

import connect4.model.Board;
import connect4.model.Piece;

/*
 * 6x7 격자와 각 칸의 돌을 그림
 * 마우스 클릭 시 어느 열인지 계산해 콜백으로 전달
 */
public class BoardPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    // 화면 크기 상수
    private static final int CELL_SIZE = 80;
    private static final int PADDING = 10;
    private static final int BOARD_WIDTH  = Board.COLS * CELL_SIZE;
    private static final int BOARD_HEIGHT = Board.ROWS * CELL_SIZE;
    
    // 색상
    private static final Color BOARD_COLOR = new Color(30, 80, 180); // 파랑
    private static final Color EMPTY_COLOR = new Color(240, 240, 240); // 흰색
    private static final Color RED_COLOR    = new Color(220, 60, 60);
    private static final Color YELLOW_COLOR = new Color(240, 210, 50);
    
    private Board board;
    private boolean clickEnabled = true;          // 사람 차례에만 클릭 활성화
    private IntConsumer onColumnClick;            // 클릭 시 외부 열 번호 전달 콜백
    
    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.WHITE);
        
        // 마우스 클릭 이벤트
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!clickEnabled) return;
                
                int col = e.getX() / CELL_SIZE;  // x좌표 열 번호
                if (col < 0 || col >= Board.COLS) return;
                
                if (onColumnClick != null) {
                    onColumnClick.accept(col);
                }
            }
        });
    }
    

     // 클릭 콜백 등록 GameFrame에 사용자 클릭 전달 

    public void setOnColumnClick(IntConsumer callback) {
        this.onColumnClick = callback;
    }
    

     // 클릭 활성/비활성. AI 차례엔 false로 막아둠.

    public void setClickEnabled(boolean enabled) {
        this.clickEnabled = enabled;
    }
    

     // 보드 데이터 교체 (새 게임 시작 시)

    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }
    

     // 화면 그리기

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // 부모의 기본 그리기 먼저 (배경 클리어)
        
        // 안티앨리어싱 : 동그라미 부드럽게
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 1) 배경
        g2.setColor(BOARD_COLOR);
        g2.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        
        // 2) 각 칸의 동그라미 그리기
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                int x = c * CELL_SIZE + PADDING;
                int y = r * CELL_SIZE + PADDING;
                int diameter = CELL_SIZE - 2 * PADDING;
                
                // 칸의 말 종류에 따라 색깔 결정
                Piece piece = board.getCell(r, c);
                Color color = getColorFor(piece);
                
                g2.setColor(color);
                g2.fillOval(x, y, diameter, diameter);
            }
        }
    }
    

     // Piece에 맞는 색깔 반환

    private Color getColorFor(Piece piece) {
        switch (piece) {
            case RED:    return RED_COLOR;
            case YELLOW: return YELLOW_COLOR;
            case EMPTY:
            default:     return EMPTY_COLOR;
        }
    }
}