package model;

public class Piece {
    private final PieceType type;
    private final Colour colour;

    public Piece(PieceType type, Colour colour) {
        this.type = type;
        this.colour = colour;
    }

    public PieceType getType() {
        return type;
    }

    public Colour getColour() {
        return colour;
    }

    public String getCharacter() {
        switch (type) {
            case PAWN:
                if (colour == Colour.WHITE) {return "P";} else {return "p";}
            case KNIGHT:
                if (colour == Colour.WHITE) {return "N";} else {return "n";}
            case ROOK:
                if (colour == Colour.WHITE) {return "R";} else {return "r";}
            case BISHOP:
                if (colour == Colour.WHITE) {return "B";} else {return "b";}
            case QUEEN:
                if (colour == Colour.WHITE) {return "Q";} else {return "q";}
            case KING:
                if (colour == Colour.WHITE) {return "K";} else {return "k";}
            default:
                return "?";
        }
    }
}
