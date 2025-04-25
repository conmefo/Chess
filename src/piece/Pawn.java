package piece;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.Board;

public class Pawn extends Piece {
    public Pawn(Point position, String color, String name, boolean hasMoved) {
        super(position, color, name, hasMoved);
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        int dir = this.color.equals("white") ? -1 : 1;

        // Move 1 step
        Point oneStep = new Point(prepos.x, prepos.y + dir);
        if (board.inBoard(oneStep) && board.getPiece(oneStep) == null) {
            validMoves.add(new Move(prepos, oneStep, this, null, null, false, false, false));

            // Move 2 steps
            Point twoStep = new Point(prepos.x, prepos.y + 2 * dir);
            if (!hasMoved && board.inBoard(twoStep) && board.getPiece(twoStep) == null) {
                validMoves.add(new Move(prepos, twoStep, this, null, null, false, false, true));
            }
        }

        // Captures
        int[] dx = {-1, 1};
        for (int i : dx) {
            Point to = new Point(prepos.x + i, prepos.y + dir);
            if (board.inBoard(to)) {
                Piece captured = board.getPiece(to);
                if (captured != null && !captured.color.equals(this.color)) {
                    validMoves.add(new Move(prepos, to, this, captured, null, false, false, false));
                }
            }
        }

        return validMoves;
    }

}
