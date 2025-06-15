package model;

public class Board {
    private final Piece[][] board;

    public Board() {
        this.board = new Piece[8][8];
        setupBoard();
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    // move pieces here or whatever
    public boolean isvalidMove(int startRow, int startCol, int endRow, int endCol) {
        if (board[startRow][startCol] == null) {
            return false;
        }

        return true;
    }

    public void movePiece(int startRow, int startCol, int endRow, int endCol) {
        Piece piece = getPiece(startRow, startCol);
        board[endRow][endCol] = piece;
        board[startRow][startCol] = null;
    }

    private void setupBoard() {
        // black
        board[0][0] = new Piece(PieceType.ROOK, Colour.BLACK);
        board[0][1] = new Piece(PieceType.KNIGHT, Colour.BLACK);
        board[0][2] = new Piece(PieceType.BISHOP, Colour.BLACK);
        board[0][3] = new Piece(PieceType.QUEEN, Colour.BLACK);
        board[0][4] = new Piece(PieceType.KING, Colour.BLACK);
        board[0][5] = new Piece(PieceType.BISHOP, Colour.BLACK);
        board[0][6] = new Piece(PieceType.KNIGHT, Colour.BLACK);
        board[0][7] = new Piece(PieceType.ROOK, Colour.BLACK);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Piece(PieceType.PAWN, Colour.BLACK);
        }

        // white
        board[7][0] = new Piece(PieceType.ROOK, Colour.WHITE);
        board[7][1] = new Piece(PieceType.KNIGHT, Colour.WHITE);
        board[7][2] = new Piece(PieceType.BISHOP, Colour.WHITE);
        board[7][3] = new Piece(PieceType.QUEEN, Colour.WHITE);
        board[7][4] = new Piece(PieceType.KING, Colour.WHITE);
        board[7][5] = new Piece(PieceType.BISHOP, Colour.WHITE);
        board[7][6] = new Piece(PieceType.KNIGHT, Colour.WHITE);
        board[7][7] = new Piece(PieceType.ROOK, Colour.WHITE);
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Piece(PieceType.PAWN, Colour.WHITE);
        }
    }
}
