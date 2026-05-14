package connect4.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import connect4.Game;
import connect4.exception.InvalidMoveException;
import connect4.player.Player;


 // 메인 창. BoardPanel + 상태 표시 + 새 게임 버튼을 묶어서 보여줌

public class GameFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private static final int AI_DELAY_MS = 600;  // AI 착수 딜레이 600ms
    
    private Game game;
    private final BoardPanel boardPanel;
    private final JLabel statusLabel;
    private final JButton newGameButton;
    private Runnable onNewGameRequested;  // 새 게임 버튼 클릭 시 외부 동작
    
    public GameFrame(Game game) {
        this.game = game;
        
        setTitle("커넥트4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // 상단: 상태 라벨
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        statusLabel.setPreferredSize(new Dimension(0, 50));
        add(statusLabel, BorderLayout.NORTH);
        
        // 중앙: 보드
        boardPanel = new BoardPanel(game.getBoard());
        boardPanel.setOnColumnClick(this::handleHumanClick);
        add(boardPanel, BorderLayout.CENTER);
        
        // 하단: 새 게임 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        newGameButton = new JButton("새 게임");
        newGameButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        newGameButton.addActionListener(e -> {
            if (onNewGameRequested != null) {
                onNewGameRequested.run();
            }
        });
        bottomPanel.add(newGameButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);  // 화면 중앙
        setResizable(false);
        
        // 초기 상태 표시 + AI라면 자동 진행
        updateStatus();
        triggerAITurnIfNeeded();
    }
    
     // "새 게임" 버튼 클릭 시 호출될 동작 등록 (Main에서 등록)
    public void setOnNewGameRequested(Runnable callback) {
        this.onNewGameRequested = callback;
    }
    
     // 새 게임 객체로 교체
    public void setGame(Game game) {
        this.game = game;
        boardPanel.setBoard(game.getBoard());
        boardPanel.setClickEnabled(true);
        updateStatus();
        triggerAITurnIfNeeded();
    }
    
     // 사람이 보드를 클릭했을 때 호출
    private void handleHumanClick(int col) {
        if (game.isOver()) return;
        if (game.isCurrentPlayerAI()) return;  // AI 차례엔 무시
        
        // HumanPlayer에 클릭한 열 정보 전달
        connect4.player.HumanPlayer human = 
                (connect4.player.HumanPlayer) game.getCurrentPlayer();
        human.setNextMove(col);
        
        // 한 턴 진행
        playOneTurn();
    }
    
     // 한 턴 진행 (사람/AI 공통)
    private void playOneTurn() {
        try {
            game.playTurn();
            boardPanel.repaint();
            updateStatus();
            
            if (game.isOver()) {
                onGameOver();
            } else {
                triggerAITurnIfNeeded();
            }
        } catch (InvalidMoveException e) {
            // 사람이 잘못된 열 클릭한 경우 — 알림 띄우고 다시 클릭 받기
            JOptionPane.showMessageDialog(this, 
                    e.getMessage(), 
                    "잘못된 수", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    
     // AI 차례면 일정 딜레이 후 자동으로 수를 두게 함
    private void triggerAITurnIfNeeded() {
        if (game.isOver()) return;
        if (!game.isCurrentPlayerAI()) {
            boardPanel.setClickEnabled(true);
            return;
        }
        
        // AI 차례 사람 클릭 막기
        boardPanel.setClickEnabled(false);
        
        // 딜레이 후 자동 진행 (Swing Timer)
        Timer timer = new Timer(AI_DELAY_MS, e -> playOneTurn());
        timer.setRepeats(false);
        timer.start();
    }
    
     // 상태 라벨 갱신
    private void updateStatus() {
        if (game.isOver()) {
            statusLabel.setText(game.getResultMessage());
            statusLabel.setForeground(new Color(20, 100, 20));  // 진한 초록
        } else {
            Player current = game.getCurrentPlayer();
            statusLabel.setText(current.getName() + " 차례 (" 
                    + current.getPiece() + ")");
            
            // 현재 플레이어 색깔 강조
            switch (current.getPiece()) {
                case RED:    statusLabel.setForeground(new Color(180, 40, 40)); break;
                case YELLOW: statusLabel.setForeground(new Color(180, 150, 20)); break;
                default:     statusLabel.setForeground(Color.BLACK);
            }
        }
    }
    
     // 게임 종료 시 처리
    private void onGameOver() {
        boardPanel.setClickEnabled(false);
        JOptionPane.showMessageDialog(this, 
                game.getResultMessage(), 
                "게임 종료", 
                JOptionPane.INFORMATION_MESSAGE);
    }
}