import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeMinimax extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private char[][] board = new char[3][3];
    private char player = 'X';
    private char ai = 'O';
    private boolean playerTurn = true;

    public TicTacToeMinimax() {
        setTitle("Tic Tac Toe - Smart AI");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        initializeBoard();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Calibri", Font.BOLD, 40));
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                panel.add(buttons[i][j]);
            }
        }

        JButton resetButton = new JButton("Reset Game");
        resetButton.setFont(new Font("Calibri", Font.BOLD, 20));
        resetButton.setBackground(Color.WHITE);
        resetButton.setForeground(Color.BLACK);
        resetButton.addActionListener(e -> resetGame());

        JLabel titleLabel = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 30));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(254, 50));

        add(titleLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(resetButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!playerTurn)
            return;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e.getSource() == buttons[i][j] && board[i][j] == ' ') {
                    board[i][j] = player;
                    buttons[i][j].setText(String.valueOf(player));
                    buttons[i][j].setForeground(Color.BLUE);
                    playerTurn = false;

                    if (checkWin(player)) {
                        JOptionPane.showMessageDialog(this, "You Win! 🎉");
                        resetGame();
                        return;
                    } else if (isBoardFull()) {
                        JOptionPane.showMessageDialog(this, "It's a Tie! 🤝");
                        resetGame();
                        return;
                    }

                    aiMove();
                }
            }
        }
    }

    private void aiMove() {
        int[] bestMove = minimax(board, ai);
        board[bestMove[0]][bestMove[1]] = ai;
        buttons[bestMove[0]][bestMove[1]].setText(String.valueOf(ai));
        buttons[bestMove[0]][bestMove[1]].setForeground(Color.RED);

        if (checkWin(ai)) {
            JOptionPane.showMessageDialog(this, "AI Wins! 😞");
            resetGame();
            return;
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "It's a Tie! 🤝");
            resetGame();
            return;
        }

        playerTurn = true;
    }

    private boolean checkWin(char symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol)
                return true;
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)
                return true;
        }
        return (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ')
                    return false;
            }
        }
        return true;
    }

    private void resetGame() {
        initializeBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }
        playerTurn = true;
    }

    private int[] minimax(char[][] board, char currentPlayer) {
        int bestScore = (currentPlayer == ai) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = { -1, -1 };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = currentPlayer;
                    int score = minimaxScore(board, currentPlayer == ai ? player : ai);
                    board[i][j] = ' ';

                    if ((currentPlayer == ai && score > bestScore) || (currentPlayer == player && score < bestScore)) {
                        bestScore = score;
                        bestMove = new int[] { i, j };
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimaxScore(char[][] board, char currentPlayer) {
        if (checkWin(ai))
            return 10;
        if (checkWin(player))
            return -10;
        if (isBoardFull())
            return 0;

        int bestScore = (currentPlayer == ai) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = currentPlayer;
                    int score = minimaxScore(board, currentPlayer == ai ? player : ai);
                    board[i][j] = ' ';

                    if ((currentPlayer == ai && score > bestScore) || (currentPlayer == player && score < bestScore)) {
                        bestScore = score;
                    }
                }
            }
        }
        return bestScore;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeMinimax::new);
    }
}
