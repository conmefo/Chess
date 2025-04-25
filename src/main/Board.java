package main;

import java.awt.*;
import main.GamePanel;
import javax.swing.JPanel;

import piece.*;

public class Board {
	public static final int ROW = 8;
	public static final int COL = 8;
	private Piece piece[][];
	
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
		piece[move.getFrom().x][move.getFrom().y] = null;
		if (move.getCastlingPiece() != null) {
			Point rookPos = move.getCastlingPiece().position;
			if (rookPos.x == 7) {
				piece[rookPos.x][rookPos.y].position = new Point(rookPos.x - 2, rookPos.y);
				piece[rookPos.x][rookPos.y].updatePosition();
			}
		}
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
}
