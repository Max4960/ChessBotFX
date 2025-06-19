package model;

import java.util.List;
import java.util.Random;

public class Bot {

    private final Evaluator evaluator;

    public Bot() {
        this.evaluator = new Evaluator();
    }

    public Move findBestMove(Board board) {
        List<Move> legalMoves = board.getLegalMoves(board.getCurrentPlayer());
        if (legalMoves.isEmpty()) {
            return null;
        }
        Move bestMove = null;
        int bestScore;
        boolean maximising = board.getCurrentPlayer() == Colour.WHITE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        if (maximising) {
            bestScore = Integer.MIN_VALUE;
        } else {
            bestScore = Integer.MAX_VALUE;
        }

        int totalMoves = legalMoves.size();
        int currentMoves = 0;

        for (Move move : legalMoves) {
            currentMoves++;
            ProgressBar.print(currentMoves, totalMoves);

            Board tempBoard = board.copy();
            tempBoard.movePiece(move.startRow, move.startCol, move.endRow, move.endCol);
            tempBoard.switchCurrentPlayer();

            int score = minimax(tempBoard, 4, !maximising, alpha, beta);
            if (maximising) {
                if (score > bestScore) {
                    bestMove = move;
                    bestScore = score;
                }
            } else {
                if (score < bestScore) {
                    bestMove = move;
                    bestScore = score;
                }
            }
        }

        if (bestMove == null && !legalMoves.isEmpty()) {
            return legalMoves.get(new Random().nextInt(legalMoves.size()));
        }

        return bestMove;
    }

    private int minimax(Board board, int depth, boolean maximising, int alpha, int beta) {
        if (depth == 0) {
            return this.evaluator.evaluate(board);
        }

        List<Move> moves = board.getLegalMoves(board.getCurrentPlayer());

        if (maximising) {
            int maxScore =  Integer.MIN_VALUE;
            for (Move move : moves) {
                Board temp = board.copy();
                temp.movePiece(move.startRow, move.startCol, move.endRow, move.endCol);
                temp.switchCurrentPlayer();
                int eval = minimax(temp, depth - 1, false, alpha, beta);
                maxScore = Math.max(eval, maxScore);
                alpha = Math.max(alpha, maxScore);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (Move move : moves) {
                Board temp = board.copy();
                temp.movePiece(move.startRow, move.startCol, move.endRow, move.endCol);
                temp.switchCurrentPlayer();
                int eval = minimax(temp, depth - 1, true, alpha, beta);
                minScore = Math.min(eval, minScore);
                beta = Math.min(beta, minScore);
                if (beta <= alpha) {
                    break;
                }
            }
            return minScore;
        }
    }
}
