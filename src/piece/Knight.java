package piece;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.Board;

public class Knight extends Piece {
    private final int[] dx = {-2, -1, 1, 2, 2, 1, -1, -2};
    private final int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};

    public Knight(Point position, String color, String name, boolean hasMoved) {
        super(position, color, name, hasMoved);
    }
    
    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            Point to = new Point(prepos.x + dx[i], prepos.y + dy[i]);

            if (board.inBoard(to)) {
                Piece captured = board.getPiece(to);
                if (captured == null || !captured.color.equals(this.color)) {
                    validMoves.add(new Move(prepos, to, this, captured, null, false, false, false));
                }
            }
        }

        return validMoves;
    }


}
