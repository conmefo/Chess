package piece;

import java.awt.Point;

public class Move {
    public int fromX, fromY;
    public int toX, toY;
    public Piece movedPiece;
    public Piece capturedPiece;
    public Piece castlingPiece; // Replace isCastling with a Piece reference
    public Piece enPassantPiece; // Replace isEnPassant with a Piece reference
    public boolean isDoubleForward;
    public boolean isPromotion; // New field for promotion

    public Move(int fromX, int fromY, int toX, int toY, Piece movedPiece, Piece capturedPiece, boolean isPromotion,
                Piece castlingPiece, Piece enPassantPiece, boolean isDoubleForward) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.isPromotion = isPromotion; // Initialize isPromotion
        this.castlingPiece = castlingPiece; // Initialize castlingPiece
        this.enPassantPiece = enPassantPiece; // Initialize enPassantPiece
        this.isDoubleForward = isDoubleForward;
    }
}