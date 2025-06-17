package model;

public class Evaluator {
    // credit: https://talkchess.com/viewtopic.php?t=76256
    private static final int[][] pawnPST = {
            { 0,  0,  0,  0,  0,  0,  0,  0},
            { 30, 30, 30, 40, 40, 30, 30, 30 },
            { 20, 20, 20, 30, 30, 30, 20, 20 },
            { 10, 10, 15, 25, 25, 15, 10, 10 },
            { 5,  5,  5,  20, 20,  5,  5,  5 },
            { 5,  0,  0,   5,  5,  0,  0,  5 },
            { 5,  5,  5, -10, -10, 5,  5,  5 },
            { 0,  0,  0,   0,   0, 0,  0, 0 }
    };
    private static final int[][] knightPST = {
            { -5,  -5, -5, -5, -5, -5, -5, -5 },
            { -5,   0,  0, 10, 10,  0,  0, -5 },
            { -5,   5, 10, 10, 10, 10,  5, -5 },
            { -5,   5, 10, 15, 15, 10,  5, -5 },
            { -5,   5, 10, 15, 15, 10,  5, -5 },
            { -5,   5, 10, 10, 10, 10,  5, -5 },
            { -5,   0,  0,  5,  5,  0,  0, -5 },
            { -5, -10, -5, -5, -5, -5, -10, -5 }
    };
    private static final int[][] rookPST = {
            { 10, 10, 10, 10, 10, 10, 10, 10 },
            { 10, 10, 10, 10, 10, 10, 10, 10 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0, 10, 10,  0,  0,  0 },
            {  0,  0,  0, 10, 10,  5,  0,  0 }
    };
    private static final int[][] bishopPST = {
            {  0,  0,   0,  0,  0,  0,  0,  0 },
            {  0,  0,   0,  0,  0,  0,  0,  0 },
            {  0,  0,   0,  0,  0,  0,  0,  0 },
            {  0, 10,   0,  0,  0,  0, 10,  0 },
            {  5,  0,  10,  0,  0, 10,  0,  5 },
            {  0, 10,   0, 10, 10,  0, 10,  0 },
            {  0, 10,   0, 10, 10,  0, 10,  0 },
            {  0,  0, -10,  0,  0, -10,  0,  0 }
    };
    private static final int[][] queenPST = {
            { -20, -10, -10, -5, -5, -10, -10, -20 },
            { -10,   0,   0,  0,  0,   0,   0, -10 },
            { -10,   0,   5,  5,  5,   5,   0, -10 },
            {  -5,   0,   5,  5,  5,   5,   0,  -5 },
            {  -5,   0,   5,  5,  5,   5,   0,  -5 },
            { -10,   5,   5,  5,  5,   5,   0, -10 },
            { -10,   0,   5,  0,  0,   0,   0, -10 },
            { -20, -10, -10,  0,  0, -10, -10, -20 }
    };
    private static final int[][] kingPST = {
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0, -5, -5, -5,  0,  0 },
            {  0,  0, 10, -5, -5, -5, 10,  0 }
    };

    public int evaluate(Board board) {
        int totalScore = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board.getPiece(r, c);
                if (piece != null) {
                    int materialValue = getMaterialValue(piece);
                    int positionalValue = getPositionalValue(piece,r,c);
                    int pieceScore = materialValue + positionalValue;

                    if (piece.getColour() == Colour.WHITE) {
                        totalScore += pieceScore;
                    } else {
                        totalScore -= pieceScore;
                    }
                }
            }
        }
        return totalScore;
    }

    private int getPositionalValue(Piece piece, int row, int col) {
        if (piece == null) return 0;
        int[][] pst = switch (piece.getType()) {
            case PAWN -> pawnPST;
            case ROOK -> rookPST;
            case BISHOP -> bishopPST;
            case QUEEN -> queenPST;
            case KING -> kingPST;
            case KNIGHT -> knightPST;
        };
        if (pst == null) return 0;
        return (piece.getColour() == Colour.WHITE) ? pst[row][col] : pst[7-row][col];

    }

    private int getMaterialValue(Piece piece) {
        if (piece == null) return 0;
        return switch (piece.getType()) {
            case PAWN -> 100;
            case KNIGHT -> 320;
            case BISHOP -> 330;
            case ROOK -> 500;
            case QUEEN -> 900;
            case KING -> 20000;
        };
    }


}
