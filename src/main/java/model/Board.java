package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Piece[][] board;
    private Colour currentPlayer;

    public Board() {
        this.board = new Piece[8][8];
        currentPlayer = Colour.WHITE;
        setupBoard();
    }

    public Board(Board original) {
        this.board = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece originalPiece = original.board[r][c];
                if (originalPiece != null) {
                    this.board[r][c] = new Piece(originalPiece.getType(), originalPiece.getColour());
                }
            }
        }
        this.currentPlayer = original.currentPlayer;
    }

    public Colour getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchCurrentPlayer() {
        if (currentPlayer == Colour.WHITE) {
            currentPlayer = Colour.BLACK;
        } else {
            currentPlayer = Colour.WHITE;
        }
    }

    public List<Move> getLegalMoves(Colour playerColour) {
        List<Move> legalMoves = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (getPiece(r, c) != null) {
                    if (getPiece(r, c).getColour() == playerColour) {
                        for (int fr = 0; fr < 8; fr++) {
                            for (int cr = 0; cr < 8; cr++) {
                                if (isvalidMove(r, c, fr, cr)) {
                                    legalMoves.add(new Move(r, c, fr, cr));
                                }
                            }
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    public Board copy() {
        return new Board(this);
    }

    // + good for white, - good for black
    public int evaluateBoard() {
        int total = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (getPiece(r, c) != null) {
                    Piece currentPiece = getPiece(r,c);
                    int value = getPieceValue(currentPiece);
                    if (currentPiece.getColour() == Colour.WHITE) {
                        total += value;
                    } else {
                        total -= value;
                    }
                }
            }
        }
        return total;
    }

    private int getPieceValue(Piece piece) {
        switch (piece.getType()) {
            case PAWN: return 10;
            case KNIGHT: return 30;
            case BISHOP: return 30;
            case ROOK: return 50;
            case QUEEN: return 90;
            case KING: return 1000;
            default: return 0;
        }
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    // move pieces here or whatever
    public boolean isvalidMove(int startRow, int startCol, int endRow, int endCol) {
        if (board[startRow][startCol] == null) {
            return false;
        }
        if (startRow == endRow && startCol == endCol) {
            return false;
        }
        Piece piece = getPiece(startRow, startCol);
        Piece destinationPiece = getPiece(endRow, endCol);
        if (destinationPiece != null && destinationPiece.getColour() == piece.getColour()) {
            return false;
        }

        return isValidPieceMove(piece, startRow, startCol, endRow, endCol);
    }

    private boolean isValidPieceMove(Piece piece, int startRow, int startCol, int endRow, int endCol) {
        int deltaRow = endRow - startRow;
        int deltaCol = endCol - startCol;
        boolean isStraight = startRow == endRow || startCol == endCol;
        boolean isDiagonal = Math.abs(startRow - endRow) == Math.abs(startCol - endCol);
        switch (piece.getType()) {
            case PAWN:
                //TODO:
                int forward = (piece.getColour() == Colour.WHITE) ? -1 : 1;
                Piece destinationPiece = getPiece(endRow, endCol);

                if (deltaCol == 0 && deltaRow == forward && destinationPiece == null) {
                    return true;
                }

                int startingRank = (piece.getColour() == Colour.WHITE) ? 6 : 1;
                if (startRow == startingRank && deltaCol == 0 && deltaRow == 2 * forward && destinationPiece == null) {
                    return true;
                }

                if (Math.abs(deltaCol) == 1 && deltaRow == forward && destinationPiece != null) {
                    return true;
                }
                return false;
            case ROOK:
                return isStraight && isClearPath(startRow, startCol, endRow, endCol);
            case BISHOP:
                return isDiagonal && isClearPath(startRow, startCol, endRow, endCol);
            case QUEEN:
                return (isStraight || isDiagonal) && isClearPath(startRow, startCol, endRow, endCol);
            case KNIGHT:
                return Math.abs(deltaRow) == 2 && Math.abs(deltaCol) == 1 ||  Math.abs(deltaRow) == 1 && Math.abs(deltaCol) == 2;
            case KING:
                return Math.abs(startRow - endRow) <= 1 && Math.abs(startCol - endCol) <= 1;
            default:
                return false;
        }
    }

    private boolean isClearPath(int startRow, int startCol, int endRow, int endCol) {
        int rowDir = Integer.signum(endRow - startRow);
        int colDir = Integer.signum(endCol - startCol);

        int curRow = startRow + rowDir;
        int curCol = startCol + colDir;

        while (curRow != endRow || curCol != endCol) {
            if (getPiece(curRow, curCol) != null) {
                return false;
            }

            curRow += rowDir;
            curCol += colDir;
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
