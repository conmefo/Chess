package piece;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import main.Board;
import main.GamePanel;


public abstract class Piece {
	public int x, y, curRow, curCol, newRow, newCol;
	public String color;
	public String name;
	public boolean hasMoved = false;

	public BufferedImage image;
	
	Piece (int col, int row, String color, String name, boolean hasMoved) {
		this.curCol = col;
		this.curRow = row;
		this.newCol = col;
		this.newRow = row;
		this.color = color;
		this.name = name;
		this.hasMoved = hasMoved;

		updateXYFromPosition();
		
		this.image = loadImage("/assets/" + color + "-" + name + ".png");
	}

	public void draw(Graphics2D g2) {
		if (image != null){
			int tmp = GamePanel.PADDING_SIZE;
			tmp = 0;
			g2.drawImage(image, x + tmp, y + tmp, GamePanel.SIZE - 2 * tmp, GamePanel.SIZE - 2 * tmp, null);
		} else {
			System.out.println("Image not found for: " + color + "-" + name);
		}
	}

	private BufferedImage loadImage(String imagePath) {
		BufferedImage img = null;
		System.out.println("Loading image: " + imagePath);
		try {
			img = ImageIO.read(getClass().getResourceAsStream(imagePath));
		} catch (IOException e) {
			System.out.println("Error loading image: " + imagePath);
			e.printStackTrace();
		}
		return img;
	}

	private void updateXYFromPosition() {
		this.x = newCol * GamePanel.SIZE;
		this.y = newRow * GamePanel.SIZE;
	}

	public int getCorX(int pos) {
		return pos * GamePanel.SIZE;
	}
	
	public int getCorY(int pos) {
		return pos * GamePanel.SIZE;
	}
	
	public int getCol(int pos) {
		return (pos + GamePanel.HALF_SIZE) / GamePanel.SIZE;
	}
	
	public int getRow(int pos) {
		return (pos + GamePanel.HALF_SIZE) / GamePanel.SIZE;
	}

	public void setPosition(int col, int row) {
		x = getCorX(col);
		y = getCorY(row);	
	}

	public void returnPosition() {
		x = getCorX(curCol);
		y = getCorY(curRow);
		newCol = curCol;
		newRow = curRow;
	}

	public void finalUpdatePosition() {
		x = getCorX(newCol);
		y = getCorY(newRow);
		hasMoved = true;
		curCol = newCol;
		curRow = newRow;
	}

	public abstract List<Move> getValidMoves(Board board);
	public abstract boolean canMove(int toX, int toY, Board board);
	public abstract Move validMove(int toX, int toY, Board board);
	public Move validMove (Board board) {
		return validMove(newCol, newRow, board);
	}
}
