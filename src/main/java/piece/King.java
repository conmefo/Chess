package piece;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.Board;

public class King extends Piece {
    private final int[] dx = {1, -1, 0, 0, 1, 1, -1, -1};
    private final int[] dy = {0, 0, 1, -1, 1, -1, 1, -1};

    public King(int x, int y, String color, String name, boolean hasMoved) {
        super(x, y, color, name, hasMoved);
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int toX = curCol + dx[i];
            int toY = curRow + dy[i];

            if (board.inBoard(toX, toY)) {
                Piece captured = board.getPiece(toX, toY);
                if (captured == null || !captured.color.equals(this.color)) {
                    validMoves.add(new Move(curCol, curRow, toX, toY, this, captured, false, null, null, false));
                }
            }
        }

        // Castling
        if (!hasMoved) {

            Piece rook = board.getPiece(curCol + 3, curRow);
            if (rook != null && !rook.hasMoved && board.checkRowEmpty(curRow, curCol + 1, 2)) {
                validMoves.add(new Move(curCol, curRow, curCol + 2, curRow, this, null, false, rook, null, false));
            }


            rook = board.getPiece(curCol - 4, curRow);
            if (rook != null && !rook.hasMoved && board.checkRowEmpty(curRow, curCol - 3, 3)) {
                validMoves.add(new Move(curCol, curRow, curCol - 2, curRow, this, null, false, rook, null, false));
            }
        }

        return validMoves;
    }

    @Override
    public boolean canMove(int toX, int toY, Board board) {
        for (int i = 0; i < 8; i++) {
            int destX = curCol + dx[i];
            int destY = curRow + dy[i];

            if (destX == toX && destY == toY && board.inBoard(destX, destY)) {
                Piece captured = board.getPiece(destX, destY);
                return captured == null || !captured.color.equals(this.color);
            }
        }

        // Castling
        if (!hasMoved) {
            // King side
            Piece rook = board.getPiece(curCol + 3, curRow);
            if (rook != null && !rook.hasMoved && board.checkRowEmpty(curRow, curCol + 1, 2)) {
                if (toX == curCol + 2 && toY == curRow) {
                    return true;
                }
            }

            // Queen side
            rook = board.getPiece(curCol - 4, curRow);
            if (rook != null && !rook.hasMoved && board.checkRowEmpty(curRow, curCol - 3, 3)) {
                if (toX == curCol - 2 && toY == curRow) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Move validMove(int targetX, int targetY, Board board) {

        for (int i = 0; i < 8; i++) {
            int toX = curCol + dx[i];
            int toY = curRow + dy[i];

            if (board.inBoard(toX, toY) && toX == targetX && toY == targetY) {
                Piece captured = board.getPiece(toX, toY);
                if (captured == null || !captured.color.equals(this.color)) {
                    return (new Move(curCol, curRow, toX, toY, this, captured, false, null, null, false));
                }
            }
        }

        // Castling
        if (!hasMoved) {
            Piece rook = board.getPiece(curCol + 3, curRow);
            if (rook != null && !rook.hasMoved && board.checkRowEmpty(curRow, curCol + 1, 2) && curCol + 2 == targetX && curRow == targetY) {
                return (new Move(curCol, curRow, curCol + 2, curRow, this, null, false, rook, null, false));
            }

            rook = board.getPiece(curCol - 4, curRow);
            if (rook != null && !rook.hasMoved && board.checkRowEmpty(curRow, curCol - 3, 3) && curCol - 2 == targetX && curRow == targetY) {
                return (new Move(curCol, curRow, curCol - 2, curRow, this, null, false, rook, null, false));
            }
        }

        return null;
    }
}
