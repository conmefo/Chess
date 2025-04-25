package piece;
import java.awt.Point;

public class Move {
    private Point from;
    private Point to;
    private Piece movedPiece;
    private Piece capturedPiece;
    private Piece promotedTo;
    private boolean isCastling;
    private boolean isEnPassant;
    private boolean isDoubleForward;

    public Move(Point from, Point to, Piece movedPiece, Piece capturedPiece, Piece promotedTo,
                boolean isCastling, boolean isEnPassant, boolean isDoubleForward) {
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.promotedTo = promotedTo;
        this.isCastling = isCastling;
        this.isEnPassant = isEnPassant;
        this.isDoubleForward = isDoubleForward;
    }

    // Getter methods
    public Point getFrom() { return from; }
    public Point getTo() { return to; }
    public Piece getMovedPiece() { return movedPiece; }
    public Piece getCapturedPiece() { return capturedPiece; }
    public Piece getPromotedTo() { return promotedTo; }
    public boolean isCastling() { return isCastling; }
    public boolean isEnPassant() { return isEnPassant; }
    public boolean isDoubleForward() { return isDoubleForward; }
}
