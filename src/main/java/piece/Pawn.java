package piece;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.Board;

public class Pawn extends Piece {    
    public Pawn(int curCol, int curRow, String color, String name, boolean hasMoved) {
        super(curCol, curRow, color, name, hasMoved);
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        int dir = this.color.equals("white") ? -1 : 1;

        // 1 step forward
        int oneStepX = curCol;
        int oneStepY = curRow + dir;
        if (board.inBoard(oneStepX, oneStepY) && board.getPiece(oneStepX, oneStepY) == null) {
            boolean isPromotion = (oneStepY == 0 || oneStepY == 7);
            validMoves.add(new Move(curCol, curRow, oneStepX, oneStepY, this, null, isPromotion, null, null, false));
        }

        // 2 steps forward
        int twoStepX = curCol;
        int twoStepY = curRow + 2 * dir;
        if (!hasMoved &&
            board.inBoard(twoStepX, twoStepY) &&
            board.getPiece(twoStepX, twoStepY) == null &&
            board.getPiece(oneStepX, oneStepY) == null) {
            validMoves.add(new Move(curCol, curRow, twoStepX, twoStepY, this, null, false, null, null, true));
        }

        // Capture
        int[] dx = {-1, 1};
        for (int i : dx) {
            int captureX = curCol + i;
            int captureY = curRow + dir;
            if (board.inBoard(captureX, captureY)) {
                Piece captured = board.getPiece(captureX, captureY);
                if (captured != null && !captured.color.equals(this.color)) {
                    boolean isPromotion = (captureY == 0 || captureY == 7);
                    validMoves.add(new Move(curCol, curRow, captureX, captureY, this, captured, isPromotion, null, null, false));
                }
            }
        }

        // En Passant
        if (board.lastMove != null && board.lastMove.isDoubleForward) {
            Move lastMove = board.lastMove;
            int lastToX = lastMove.toX;
            int lastToY = lastMove.toY;
            if (Math.abs(lastToX - curCol) == 1 && lastToY == curRow) {
                int enPassantTargetX = lastToX;
                int enPassantTargetY = lastToY + dir;
                if (board.inBoard(enPassantTargetX, enPassantTargetY)) {
                    Piece enPassantPawn = board.getPiece(lastToX, lastToY);
                    validMoves.add(new Move(curCol, curRow, enPassantTargetX, enPassantTargetY, this, enPassantPawn, false, null, enPassantPawn, false));
                }
            }
        }

        return validMoves;
    }


    @Override
    public boolean canMove(int toX, int toY, Board board) {
        int dir = this.color.equals("white") ? -1 : 1;
    
        // 1 step forward
        int oneStepX = curCol;
        int oneStepY = curRow + dir;

        if (oneStepX == toX && oneStepY == toY && board.inBoard(oneStepX, oneStepY) && board.getPiece(oneStepX, oneStepY) == null) {
            return true;
        }
    
        // 2 steps forward
        int twoStepX = curCol;
        int twoStepY = curRow + 2 * dir;
        if (!hasMoved && twoStepX == toX && twoStepY == toY && board.inBoard(twoStepX, twoStepY) &&
             board.getPiece(twoStepX, twoStepY) == null && board.getPiece(oneStepX, oneStepY) == null) {
            return true;
        }
    
        // Capture
        int[] dx = {-1, 1};
        for (int i : dx) {
            int captureX = curCol + i;
            int captureY = curRow + dir;
            if (captureX == toX && captureY == toY && board.inBoard(captureX, captureY)) {
                Piece captured = board.getPiece(captureX, captureY);
                if (captured != null && !captured.color.equals(this.color)) {
                    return true;
                }
            }
        }
    
        // En Passant
        if (board.lastMove != null && board.lastMove.isDoubleForward) {
            Move lastMove = board.lastMove;
            int lastToX = lastMove.toX;
            int lastToY = lastMove.toY;
            if (Math.abs(lastToX - curCol) == 1 && lastToY == curRow) {
                int enPassantTargetX = lastToX;
                int enPassantTargetY = lastToY + dir;
                if (enPassantTargetX == toX && enPassantTargetY == toY && board.inBoard(enPassantTargetX, enPassantTargetY)) {
                    return true;
                }
            }
        }
    
        return false;
    }

    @Override
    public Move validMove (int targetX, int targetY, Board board) {
        System.out.println("Validating move for Pawn: " + this.color + " " + this.name + " " + this.hasMoved);
        System.out.println("Current Position: (" + curCol + ", " + curRow + ")");
        System.out.println("Target Position: (" + targetX + ", " + targetY + ")");
        int dir = this.color.equals("white") ? -1 : 1;

        // 1 step forward
        int oneStepX = curCol;
        int oneStepY = curRow + dir;
        if (board.inBoard(oneStepX, oneStepY) && board.getPiece(oneStepX, oneStepY) == null && oneStepX == targetX && oneStepY == targetY) {
            boolean isPromotion = (oneStepY == 0 || oneStepY == 7);
            return (new Move(curCol, curRow, oneStepX, oneStepY, this, null, isPromotion, null, null, false));
        }

        // 2 steps forward
        int twoStepX = curCol;
        int twoStepY = curRow + 2 * dir;

        System.out.println("Two Step: " + twoStepX + ", " + twoStepY);
        
        if (!hasMoved &&
            board.inBoard(twoStepX, twoStepY) &&
            board.getPiece(twoStepX, twoStepY) == null &&
            board.getPiece(oneStepX, oneStepY) == null
            && twoStepX == targetX && twoStepY == targetY) {
            return (new Move(curCol, curRow, twoStepX, twoStepY, this, null, false, null, null, true));
        }

        // Capture
        int[] dx = {-1, 1};
        for (int i : dx) {
            int captureX = curCol + i;
            int captureY = curRow + dir;
            if (board.inBoard(captureX, captureY) && captureX == targetX && captureY == targetY) {
                Piece captured = board.getPiece(captureX, captureY);
                if (captured != null && !captured.color.equals(this.color)) {
                    boolean isPromotion = (captureY == 0 || captureY == 7);
                    return (new Move(curCol, curRow, captureX, captureY, this, captured, isPromotion, null, null, false));
                }
            }
        }

        // En Passant
        if (board.lastMove != null && board.lastMove.isDoubleForward) {
            Move lastMove = board.lastMove;
            int lastToX = lastMove.toX;
            int lastToY = lastMove.toY;
            if (Math.abs(lastToX - curCol) == 1 && lastToY == curRow) {
                int enPassantTargetX = lastToX;
                int enPassantTargetY = lastToY + dir;
                if (board.inBoard(enPassantTargetX, enPassantTargetY) && enPassantTargetX == targetX && enPassantTargetY == targetY) {
                    Piece enPassantPawn = board.getPiece(lastToX, lastToY);
                    return (new Move(curCol, curRow, enPassantTargetX, enPassantTargetY, this, enPassantPawn, false, null, enPassantPawn, false));
                }
            }
        }

        return null;
    }
    
}