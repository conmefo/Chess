package piece;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.Board;

public class Rook extends Piece {
    int[] dx = {1, -1, 0, 0};
    int[] dy = {0, 0, 1, -1};
    public Rook(Point position, String color, String name, boolean hasMoved) {
        super(position, color, name, hasMoved);
    }

    
    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();

        for (int dir = 0; dir < 4; dir++) {
            Point npos = new Point(prepos);

            while (true) {
                npos.translate(dx[dir], dy[dir]);

                if (!board.inBoard(npos)) break;

                Piece target = board.getPiece(npos);

                if (target == null) {
                    validMoves.add(new Move(new Point(prepos), new Point(npos), this, null, null, null, null, false));
                } else {
                    if (!target.color.equals(this.color)) {
                    	validMoves.add(new Move(new Point(prepos), new Point(npos), this, target, null, null, null, false));
                    }
                    break; 
                }
            }
        }

        return validMoves;
    }

    
    
    
}
