package piece;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.Board;

public class King extends Piece {
    private final int[] dx = {1, -1, 0, 0, 1, 1, -1, -1};
    private final int[] dy = {0, 0, 1, -1, 1, -1, 1, -1};
    
    boolean isChecked;

    public King(Point position, String color, String name, boolean hasMoved) {
        super(position, color, name, hasMoved);
        isChecked = false;
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        int[] dx = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

        for (int i = 0; i < 8; i++) {
            Point to = new Point(prepos.x + dx[i], prepos.y + dy[i]);

            if (board.inBoard(to)) {
                Piece captured = board.getPiece(to);
                if (captured == null || !captured.color.equals(this.color)) {
                    validMoves.add(new Move(prepos, to, this, captured, false, null, null, false));
                }
            }
        }       
        
        //Castling
        if (hasMoved == false){
            if (board.getPiece(new Point(prepos.x + 3, prepos.y)).hasMoved == false 
                && board.checkRowEmpty(prepos.y, prepos.x + 1, 2) == true) {
                    validMoves.add(new Move(prepos, new Point(prepos.x + 2, prepos.y), this, null, false, board.getPiece(new Point(prepos.x + 3, prepos.y)), null, false));
            }
            if (board.getPiece(new Point(prepos.x - 4, prepos.y)).hasMoved == false 
                    && board.checkRowEmpty(prepos.y, prepos.x - 3, 3) == true) {
                	validMoves.add(new Move(prepos, new Point(prepos.x - 2, prepos.y), this, null, false, board.getPiece(new Point(prepos.x - 4, prepos.y)), null, false));
            }
            System.out.println(board.checkRowEmpty(prepos.y, prepos.x - 3, 3));
            System.out.println(board.getPiece(new Point(prepos.x - 4, prepos.y)).hasMoved);
        }

        return validMoves;
    }

}
