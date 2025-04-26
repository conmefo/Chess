package main;

import java.awt.*;
import java.util.*;
import java.util.List;

import main.GamePanel;
import javax.swing.JPanel;

import piece.*;

public class Board {
	public static final int ROW = 8;
	public static final int COL = 8;
	private Piece piece[][];
	public Move lastMove = null;
	
	Board(){
		piece = new Piece[ROW + 2][COL + 2]; 
	}
	
	public void draw(Graphics2D g) {
		for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(new Color(235, 236, 208)); 
                } else {
                    g.setColor(new Color(20, 95, 75)); 
                }

                g.fillRect(col * GamePanel.SIZE, row * GamePanel.SIZE, GamePanel.SIZE, GamePanel.SIZE);
            }
        }
	}

	public boolean isInCheck(String color) {
		for (int row = 0; row < Board.ROW; row++) {
			for (int col = 0; col < Board.COL; col++) {
				Piece p = getPiece(new Point(col, row));
				if (p != null && !p.color.equals(color)) { 
					java.util.List<Move> moves = p.getValidMoves(this);
					for (Move move : moves) {
						if (move.getCapturedPiece().equals(King.class) && move.getCapturedPiece().color.equals(color)){
							return true; 
						}
					}
				}
			}
		}
	
		return false;
	}
	
	public boolean isCheckmate(String color) {
		if (!isInCheck(color)) return false; 

		for (int row = 0; row < Board.ROW; row++) {
			for (int col = 0; col < Board.COL; col++) {
				Piece p = getPiece(new Point(col, row));
				if (p != null && p.color.equals(color)) {
					List<Move> moves = p.getValidMoves(this);
					for (Move move : moves) {
						Piece captured = getPiece(move.getTo());
						setPiece(move.getTo(), p);
						removePiece(move.getFrom());
						Point originalPos = p.position;
						p.position = move.getTo();

						boolean stillInCheck = isInCheck(color);

						// Undo move
						p.position = originalPos;
						setPiece(move.getFrom(), p);
						setPiece(move.getTo(), captured);

						if (!stillInCheck) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	public boolean isMoveSafe(Move move) {
		Piece capturedPiece = getPiece(move.getTo());
		Piece movedPiece = move.getMovedPiece();
		
		setPiece(move.getTo(), movedPiece);
		setPiece(move.getFrom(), null);
		
		Point originalPos = movedPiece.position;
		movedPiece.position = move.getTo();
		
		boolean kingInCheck = isInCheck(movedPiece.color);
		
		movedPiece.position = originalPos;
		setPiece(move.getFrom(), movedPiece);
		setPiece(move.getTo(), capturedPiece);
		
		return !kingInCheck;
	}
	

	
	public boolean checkRowEmpty(int col, int row, int numRow) {
		for (int i = row; i < row + numRow; ++i) {
			if (piece[i][col] != null) {
				return false;
			}
		}
		return true;
	}
	
	public void performMove(Move move) {
		piece[move.getTo().x][move.getTo().y] = move.getMovedPiece();
		piece[move.getFrom().x][move.getFrom().y].hasMoved = true;
		piece[move.getFrom().x][move.getFrom().y] = null;
		if (move.getCastlingPiece() != null) {
			Point rookPos = move.getCastlingPiece().position;
			if (rookPos.x == 7) {
				piece[rookPos.x][rookPos.y].position = new Point(rookPos.x - 2, rookPos.y);
				piece[rookPos.x][rookPos.y].updatePosition();
				piece[rookPos.x - 2][rookPos.y] = piece[rookPos.x][rookPos.y];
				piece[rookPos.x][rookPos.y] = null;
			} else if (rookPos.x == 0) {
				piece[rookPos.x][rookPos.y].position = new Point(rookPos.x + 3, rookPos.y);
				piece[rookPos.x][rookPos.y].updatePosition();
				piece[rookPos.x + 3][rookPos.y] = piece[rookPos.x][rookPos.y];
				piece[rookPos.x][rookPos.y] = null;
			}
		}

		if (move.getEnPassantPiece() != null) {
			piece[move.getEnPassantPiece().position.x][move.getEnPassantPiece().position.y] = null;
		}

		lastMove = move;
	}
	
	public Piece getPiece(Point pos) {
		return piece[pos.x][pos.y];
	}
	
	public void setPiece(Point pos, Piece p) {
		piece[pos.x][pos.y] = p;
	}
	
	
	public void removePiece(Point pos) {
		piece[pos.x][pos.y] = null;
	}

	public boolean inBoard(Point npos) {
		return (npos.x >= 0 && npos.x < ROW && npos.y >= 0 && npos.y < COL);
	}

	void drawCanMoveSquare(Graphics2D g, Piece p) {
		List<Move> moves = p.getValidMoves(this);
		for (Move move : moves) {
			drawSquare(g, move.getTo());
		}
	}

	void drawSquare(Graphics2D g, Point pos) {
		if (piece[pos.x][pos.y] != null) {
			g.setColor(new Color(255, 160, 160, 150));
		} else {
			g.setColor(new Color(160, 160, 255, 150));
		}
		g.fillRect(pos.x * GamePanel.SIZE, pos.y * GamePanel.SIZE, GamePanel.SIZE, GamePanel.SIZE);
	}
}
