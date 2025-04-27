package piece;

import main.Board;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    private static final int[] dx = {1, 2, 2, 1, -1, -2, -2, -1};
    private static final int[] dy = {2, 1, -1, -2, -2, -1, 1, 2};

    public Knight(int x, int y, String color, String name, boolean hasMoved) {
        super(x, y, color, name, hasMoved);
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int nx = curCol + dx[i];
            int ny = curRow + dy[i];

            if (board.inBoard(nx, ny)) {
                Piece target = board.getPiece(nx, ny);
                if (target == null || !target.color.equals(this.color)) {
                    validMoves.add(new Move(curCol, curRow, nx, ny, this, target, false, null, null, false));
                }
            }
        }

        return validMoves;
    }

    @Override
    public boolean canMove(int toX, int toY, Board board) {
        int dx = Math.abs(toX - curCol);
        int dy = Math.abs(toY - curRow);

        if (!((dx == 1 && dy == 2) || (dx == 2 && dy == 1))) return false;

        Piece target = board.getPiece(toX, toY);
        return target == null || !target.color.equals(this.color);
    }

    @Override
    public Move validMove (int toX, int toY, Board board) {
    
        for (int i = 0; i < 8; i++) {
            int nx = curCol + dx[i];
            int ny = curRow + dy[i];

            if (board.inBoard(nx, ny) && nx == toX && ny == toY) {
                Piece target = board.getPiece(nx, ny);
                if (target == null || !target.color.equals(this.color)) {
                    return (new Move(curCol, curRow, nx, ny, this, target, false, null, null, false));
                }
            }
        }

        return null;
    }
}
