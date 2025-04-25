package piece;

import java.awt.Point;

public class Move {
    private Point from;
    private Point to;
    private Piece movedPiece;
    private Piece capturedPiece;
    private Piece promotedTo;
    private Piece castlingPiece; // Replace isCastling with a Piece reference
    private Piece enPassantPiece; // Replace isEnPassant with a Piece reference
    private boolean isDoubleForward;

    public Move(Point from, Point to, Piece movedPiece, Piece capturedPiece, Piece promotedTo,
                Piece castlingPiece, Piece enPassantPiece, boolean isDoubleForward) {
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.promotedTo = promotedTo;
        this.castlingPiece = castlingPiece; // Initialize castlingPiece
        this.enPassantPiece = enPassantPiece; // Initialize enPassantPiece
        this.isDoubleForward = isDoubleForward;
    }

    // Getter methods
    public Point getFrom() { return from; }
    public Point getTo() { return to; }
    public Piece getMovedPiece() { return movedPiece; }
    public Piece getCapturedPiece() { return capturedPiece; }
    public Piece getPromotedTo() { return promotedTo; }
    public Piece getCastlingPiece() { return castlingPiece; } // Getter for castlingPiece
    public Piece getEnPassantPiece() { return enPassantPiece; } // Getter for enPassantPiece
    public boolean isDoubleForward() { return isDoubleForward; }
}