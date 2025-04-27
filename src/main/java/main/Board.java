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
		int kingPosCol = -1, kingPosRow = -1;
		for (int i = 0; i < ROW; ++i) {
			for (int j = 0; j < COL; ++j) {
				if (piece[i][j] != null && piece[i][j].color.equals(color) && piece[i][j].name.equals("king")) {
					kingPosCol = i;
					kingPosRow = j;
					break;
				}
			}
		}

		if (kingPosCol == -1 || kingPosRow == -1) {
			return false; 
		}

		for (int i = 0; i < ROW; ++i) {
			for (int j = 0; j < COL; ++j) {
				if (piece[i][j] != null && !piece[i][j].color.equals(color)) {
					if (piece[i][j].canMove(kingPosCol, kingPosRow, this)) {
						return true; 
					}
				}
			}
		}

		return false;
	}

	public boolean isMoveSafe(Move move) {
		Board tempBoard = new Board();
		for (int i = 0; i < ROW; ++i) {
			for (int j = 0; j < COL; ++j) {
				tempBoard.setPiece(i, j, piece[i][j]);
			}
		}

		return true;

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
		piece[move.toX][move.toY] = move.movedPiece;
		piece[move.fromX][move.fromY] = null;

		if (move.castlingPiece != null) {
			int rookPosX = move.castlingPiece.curCol;
			int rookPosY = move.castlingPiece.curRow;

			if (rookPosX == 7) {
				piece[rookPosX - 2][rookPosY] = piece[rookPosX][rookPosY];
				piece[rookPosX][rookPosY] = null;
			} else if (rookPosX == 0) {
				piece[rookPosX + 3][rookPosY] = piece[rookPosX][rookPosY];
				piece[rookPosX][rookPosY] = null;
			}
		}

		if (move.enPassantPiece != null) {
			piece[move.enPassantPiece.curCol][move.enPassantPiece.curRow] = null;
		}

		lastMove = move;
	}
	
	public Piece getPiece(int posX, int posY) {
		return piece[posX][posY];
	}
	
	public void setPiece(int posX, int posY, Piece p) {
		piece[posX][posY] = p;
	}
	
	
	public void removePiece(Point pos) {
		piece[pos.x][pos.y] = null;
	}

	public boolean inBoard(int nposX, int nposY) {
		return (nposX >= 0 && nposX < ROW && nposY >= 0 && nposY < COL);
	}

	void drawCanMoveSquare(Graphics2D g, Piece p) {
		List<Move> moves = p.getValidMoves(this);
		for (Move move : moves) {
			drawSquare(g, move.toX, move.toY);
		}
	}

	void drawSquare(Graphics2D g, int posX, int posY) {
		if (piece[posX][posY] != null) {
			g.setColor(new Color(255, 160, 160, 150));
		} else {
			g.setColor(new Color(160, 160, 255, 150));
		}
		g.fillRect(posX * GamePanel.SIZE, posY * GamePanel.SIZE, GamePanel.SIZE, GamePanel.SIZE);
	}
}
