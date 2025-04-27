package piece;

import main.Board;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    private static final int[] dx = {1, 1, -1, -1};
    private static final int[] dy = {1, -1, 1, -1};

    public Bishop(int x, int y, String color, String name, boolean hasMoved) {
        super(x, y, color, name, hasMoved);
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();

        for (int dir = 0; dir < 4; dir++) {
            int nx = curCol;
            int ny = curRow;

            while (true) {
                nx += dx[dir];
                ny += dy[dir];

                if (!board.inBoard(nx, ny)) break;

                Piece target = board.getPiece(nx, ny);

                if (target == null) {
                    validMoves.add(new Move(curCol, curRow, nx, ny, this, null, false, null, null, false));
                } else {
                    if (!target.color.equals(this.color)) {
                        validMoves.add(new Move(curCol, curRow, nx, ny, this, target, false, null, null, false));
                    }
                    break;
                }
            }
        }

        return validMoves;
    }

    @Override
    public boolean canMove(int toX, int toY, Board board) {
        if (Math.abs(toX - curCol) != Math.abs(toY - curRow)) return false;

        int stepX = (toX > curCol) ? 1 : -1;
        int stepY = (toY > curRow) ? 1 : -1;

        int x = curCol + stepX;
        int y = curRow + stepY;

        while (x != toX && y != toY) {
            if (board.getPiece(x, y) != null) return false;
            x += stepX;
            y += stepY;
        }

        Piece target = board.getPiece(toX, toY);
        return target == null || !target.color.equals(this.color);
    }

    @Override
    public Move validMove (int targetX, int targetY, Board board) {
        for (int dir = 0; dir < 4; dir++) {
            int nx = curCol;
            int ny = curRow;

            while (true) {
                nx += dx[dir];
                ny += dy[dir];

                if (!board.inBoard(nx, ny)) break;

                Piece target = board.getPiece(nx, ny);

                if (nx != targetX && ny != targetY) {
                    continue;
                }

                if (target == null) {
                    return (new Move(curCol, curRow, nx, ny, this, null, false, null, null, false));
                } else {
                    if (!target.color.equals(this.color)) {
                        return (new Move(curCol, curRow, nx, ny, this, target, false, null, null, false));
                    }
                    break;
                }
            }
        }

        return null;
    }
}
