import java.util.Scanner;

public class BitCheckers {
    private static final long[] board = new long[2]; // Two players
    private int currentPlayer = 1; // Start with player 1

    public BitCheckers() {
        initializeBoard(); // Set up the board
    }

    public void initializeBoard() {
        // Initial positions for player 1 (P1) and player 2 (P2)
        board[0] = 0xAA55AA; // 101010100101010110101010 in binary
        board[1] = (0x55AA55L << 40); // 010101011010101001010101 in binary shifted to the top rows
    }

    public void movePiece(int start, int end) {
        // Check if move is legal
        if (isLegalMove(start, end) == 0) {
            System.out.println("INVALID MOVE.");
            return;
        }

        // Move the piece
        board[currentPlayer - 1] &= ~(1L << start); // Remove piece from start position
        board[currentPlayer - 1] |= (1L << end); // Place piece at end position

        // Check if the opponent's piece is at the destination (capture logic)
        int opponent = (currentPlayer == 1) ? 1 : 0;
        if ((board[opponent] & (1L << end)) != 0) {
            capturePiece(opponent, end);
        }

        updateGameState(); // Switch to the next player
    }

    private void updateGameState() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1; // Switch player
    }

    private void capturePiece(int opponent, int pos) {
        // Remove opponent's piece at the given position
        board[opponent] &= ~(1L << pos);
    }

    public int isLegalMove(int start, int end) {
        // Check if the start and end positions are within bounds
        if (start < 0 || start >= 64 || end < 0 || end >= 64) {
            return 0; // Invalid move
        }

        // Check if there is a piece at the start position
        long currentPlayerBoard = board[currentPlayer - 1];
        if ((currentPlayerBoard & (1L << start)) == 0) {
            return 0; // No piece at start position
        }

        // Ensure end position is not already occupied by the same player's piece
        if ((currentPlayerBoard & (1L << end)) != 0) {
            return 0; // End position occupied by current player's piece
        }

        return 1; // Move is valid
    }

    public void printBoard() {
        System.out.println("  1 2 3 4 5 6 7 8");
        for (int row = 7; row >= 0; row--) {
            System.out.print((row + 1) + " ");
            for (int col = 0; col < 8; col++) {
                int pos = row * 8 + col;
                if ((board[1] & (1L << pos)) != 0) { // Player 2
                    System.out.print("W ");
                } else if ((board[0] & (1L << pos)) != 0) { // Player 1
                    System.out.print("B ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public void printBoardBinaryHex() {
        System.out.println("Player 1 [0] (Binary): " + Long.toBinaryString(board[0]));
        System.out.println("Player 2 [1] (Hex): " + Long.toHexString(board[1]));
    }

    public static void main(String[] args) {
        BitCheckers game = new BitCheckers();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Print the initial board
        game.printBoard();
        game.printBoardBinaryHex(); // Print binary and hex representation

        while (running) {
            System.out.println(" ____ MAIN MENU ----- ");
            System.out.println("1. Make a Move");
            System.out.println("2. Display Board");
            System.out.println("3. Print Board in Binary and Hex");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter start position (0-63): ");
                    int start = scanner.nextInt();
                    System.out.print("Enter end position (0-63): ");
                    int end = scanner.nextInt();
                    game.movePiece(start, end);

                    // Print the board and its binary/hex representation after the move
                    game.printBoard();
                    game.printBoardBinaryHex();
                    break;
                case 2:
                    game.printBoard(); // Print the board
                    break;
                case 3:
                    game.printBoardBinaryHex(); // Print binary and hex representation
                    break;
                case 4:
                    running = false; // Exit the game
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }

        scanner.close();
    }
}