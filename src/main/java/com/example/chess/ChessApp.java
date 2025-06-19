package com.example.chess;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChessApp extends Application {
    private Board board = new Board();
    private GridPane grid = new GridPane();
    private Colour currentPlayer = Colour.WHITE;

    private int startRow = -1, startCol = -1;
    private StackPane selectedTile = null;

    private static final int TILE_SIZE = 75;

    private Map<String, Image> pieceImages = new HashMap<>();

    private Bot bot = new Bot();
    private boolean botsTurn = false;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Chess");
        loadImages();
        refresh();
        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }

    private void loadImages() {
        PieceType[] types = PieceType.values();
        for (Colour colour : Colour.values()) {
            for (PieceType type : types) {
                char pieceChar = getCharPiece(type, colour);
                if (pieceChar == '?') continue; // Use continue, not return

                String colourString = colour.name().toLowerCase();
                String typeString = Character.toString(Character.toLowerCase(pieceChar));
                String key = colourString + typeString; // e.g., "whitep", "blackp"

                String filename = key + ".png";
                String path = "/images/" + filename;

                try {
                    // Use getResourceAsStream to load from resources
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                    pieceImages.put(key, image);
                } catch (Exception e) {
                    System.err.println("Could not load image: " + path);
                    e.printStackTrace();
                }
            }
        }
    }

    private char getCharPiece(PieceType type, Colour colour) {
        char piece;

        switch (type) {
            case PAWN: piece = 'p'; break;
            case KNIGHT: piece = 'n'; break;
            case ROOK: piece = 'r'; break;
            case BISHOP: piece = 'b'; break;
            case QUEEN: piece = 'q'; break;
            case KING: piece = 'k'; break;
            default: piece = '?'; break;
        }
        if (colour == Colour.WHITE) {
            return Character.toUpperCase(piece);
        }
        return piece;
    }

    private void refresh() {
        startRow = -1;
        startCol = -1;
        selectedTile = null;

        grid.getChildren().clear();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                StackPane tile = new StackPane();
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);

                String colour = (r + c) % 2 == 0 ? "#eeeed2" : "#769656";
                tile.setStyle("-fx-background-color: " + colour + ";");

                Piece piece = board.getPiece(r, c);
                if (piece != null) {
                    String colourKey = piece.getColour().name().toLowerCase();
                    char typeChar = Character.toLowerCase(getCharPiece(piece.getType(), piece.getColour()));
                    String key = colourKey + typeChar; // e.g., "whitep"
                    Image image = pieceImages.get(key);

                    if (image != null) {
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(TILE_SIZE * 0.85);
                        imageView.setFitHeight(TILE_SIZE * 0.85);
                        imageView.setPreserveRatio(true);
                        tile.getChildren().add(imageView);
                    } else {
                        Label pieceLabel = new Label(piece.getCharacter());
                        pieceLabel.setStyle("-fx-font-size: 50px;");
                        tile.getChildren().add(pieceLabel);
                    }
                }
                final int finalRow = r;
                final int finalCol = c;
                tile.setOnMouseClicked(event -> {
                    handleTileClick(finalRow, finalCol);
                });

                grid.add(tile, c, r);
            }
        }
    }

    private void handleTileClick(int row, int col) {
        //System.out.println("Clicked tile: " + row + ", " + col);

        if (startRow == -1 || startCol == -1) {
            Piece clickedPiece = board.getPiece(row, col);
            if (clickedPiece != null && clickedPiece.getColour() == currentPlayer) {
                startRow = row;
                startCol = col;

                selectedTile = (StackPane) getNode(row, col, grid);
                if (selectedTile != null) {
                    selectedTile.setStyle(selectedTile.getStyle() + "-fx-border-color: red; -fx-border-width: 2;");
                }
            }
        } else {
            Piece destinationPiece = board.getPiece(row, col);

            if (destinationPiece != null && destinationPiece.getColour() == currentPlayer) {
                // Deselect old tile
                String originalStyle = selectedTile.getStyle().replaceAll("-fx-border-color: red; -fx-border-width: 2;", "");
                selectedTile.setStyle(originalStyle);

                // Select the new piece
                startRow = row;
                startCol = col;
                selectedTile = (StackPane) getNode(row, col, grid);
                selectedTile.setStyle(selectedTile.getStyle() + "-fx-border-color: red; -fx-border-width: 2;");
                return;
            }

            if (board.isvalidMove(startRow, startCol, row, col)) {
                board.movePiece(startRow, startCol, row, col);
                switchPlayer();
                refresh();
            } else {
                refresh();
                System.out.println("Invalid move.");
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == Colour.WHITE) ? Colour.BLACK : Colour.WHITE;
        board.switchCurrentPlayer();

        if (currentPlayer == Colour.BLACK) {
            makeBotMove();
        }

    }

    private void makeBotMove() {
        botsTurn = true;
        grid.setDisable(true);

        PauseTransition pause = new PauseTransition(Duration.millis(100));
        pause.setOnFinished(event -> {
            Move botMove = bot.findBestMove(board);
            if (botMove != null) {
                board.movePiece(botMove.startRow, botMove.startCol, botMove.endRow, botMove.endCol);
                refresh();
                switchPlayer();
            } else {
                System.out.println("Checkmate or Stalemate");
            }
            botsTurn = false;
            grid.setDisable(false);
        });
        pause.play();
    }

    private Node getNode(int row, int col, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}