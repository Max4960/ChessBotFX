package model;

import java.util.List;
import java.util.Random;

public class Bot {
    public Move findBestMove(Board board) {
        Move bestMove = null;
        int bestScore = (board.getCurrentPlayer() == Colour.WHITE) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        List<Move> legalMoves = board.getLegalMoves(board.getCurrentPlayer());


        for (Move move : legalMoves) {
            Board temp = board.copy();
            temp.movePiece(move.startRow, move.startCol, move.endRow, move.endCol);

            int score = temp.evaluateBoard();

            if (board.getCurrentPlayer() == Colour.WHITE) {
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            } else {
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        if (bestMove == null && !legalMoves.isEmpty()) {
            System.out.println("No best move found");
            Random rand = new Random();
            return legalMoves.get(rand.nextInt(legalMoves.size()));
        }
        return bestMove;
    }
}
