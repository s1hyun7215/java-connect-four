package connect4;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import connect4.ai.EasyStrategy;
import connect4.ai.HardStrategy;
import connect4.ai.MediumStrategy;
import connect4.ai.Strategy;
import connect4.model.Piece;
import connect4.player.AIPlayer;
import connect4.player.HumanPlayer;
import connect4.player.Player;
import connect4.ui.GameFrame;



public class Main {
    
    private static GameFrame gameFrame;
    
    public static void main(String[] args) {
        // Swing 코드는 EDT에서 실행
        SwingUtilities.invokeLater(Main::startNewGame);
    }
    

     // 새 게임 시작: 난이도 선택 → 선공 선택 → 게임 생성 → 화면 표시

    private static void startNewGame() {
        // 1) 난이도 선택
        Strategy strategy = askDifficulty();
        if (strategy == null) {
            if (gameFrame == null) {
                System.exit(0);
            }
            return;
        }
        
        // 2) 선공 선택
        Boolean humanFirst = askWhoGoesFirst();
        if (humanFirst == null) {
            if (gameFrame == null) {
                System.exit(0);
            }
            return;
        }
        
        // 3) 플레이어 생성 (선공 RED)
        Player human;
        Player ai;
        if (humanFirst) {
            human = new HumanPlayer("내", Piece.RED);
            ai = new AIPlayer("AI", Piece.YELLOW, strategy);
        } else {
            ai = new AIPlayer("AI", Piece.RED, strategy);
            human = new HumanPlayer("내", Piece.YELLOW);
        }
        
        // 4) 게임 생성 (Game 생성자는 red, yellow 순서)
        Game game;
        if (humanFirst) {
            game = new Game(human, ai);
        } else {
            game = new Game(ai, human);
        }
        
        // 5) GameFrame 생성 또는 갱신
        if (gameFrame == null) {
            gameFrame = new GameFrame(game);
            gameFrame.setOnNewGameRequested(Main::startNewGame);
            gameFrame.setVisible(true);
        } else {
            gameFrame.setGame(game);
        }
    }
    

     // 난이도 선택 다이얼로그. return 선택된 Strategy, 취소하면 null

    private static Strategy askDifficulty() {
        String[] options = { "쉬움", "보통", "어려움" };
        
        int choice = JOptionPane.showOptionDialog(
                gameFrame,
                "AI 난이도를 선택하세요.",
                "커넥트4 - 난이도 선택",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]   // 기본: 보통
        );
        
        switch (choice) {
            case 0:  return new EasyStrategy();
            case 1:  return new MediumStrategy();
            case 2:  return new HardStrategy();
            default: return null;
        }
    }
    

     // 선공 선택 다이얼로그. return true 사람 선 / false AI 선 / null 취소

    private static Boolean askWhoGoesFirst() {
        String[] options = { "내가 먼저", "AI 먼저" };
        
        int choice = JOptionPane.showOptionDialog(
                gameFrame,
                "누가 먼저 시작할까요?\n(먼저 두는 쪽이 빨강입니다)",
                "커넥트4 - 선공 선택",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]   // 기본: 사람 선
        );
        
        switch (choice) {
            case 0:  return true;
            case 1:  return false;
            default: return null;
        }
    }
}