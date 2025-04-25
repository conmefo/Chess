package piece;

import main.Board;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class Piece {
	public Point position, prepos;
	public String color;
	public String name;
	public boolean hasMoved;

	public int x, y;

	public BufferedImage image;

	public Piece(Point position, String color, String name, boolean hasMoved) {
		this.position = position;
		this.prepos = position;
		this.color = color;
		this.name = name;
		this.hasMoved = hasMoved;

		updateXYFromPosition();
		this.image = loadImage("/assets/" + color + "-" + name + ".png");
	}

	public void draw(Graphics2D g2) {
		if (image != null && position != null) {
			int tmp = GamePanel.PADDING_SIZE;
			tmp = 0;
			g2.drawImage(image, x + tmp, y + tmp, GamePanel.SIZE - 2 * tmp, GamePanel.SIZE - 2 * tmp, null);
		}
	}

	private void updateXYFromPosition() {
		if (position != null) {
			this.x = position.x * GamePanel.SIZE;
			this.y = position.y * GamePanel.SIZE;
		}
	}

	private BufferedImage loadImage(String imagePath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResourceAsStream(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	public void updatePosition() {
		x = getX(position.x);
		y = getY(position.y);
		hasMoved = true;
		prepos = new Point(getCol(x), getRow(y));
	}
	
	public void returnPosition() {
		x = getX(prepos.x);
		y = getY(prepos.y);
		position = new Point(getCol(x), getRow(y));
	}
	
	public int getX(int pos) {
		return pos * GamePanel.SIZE;
	}
	
	public int getY(int pos) {
		return pos * GamePanel.SIZE;
	}
	
	public int getCol(int pos) {
		return (pos + GamePanel.HALF_SIZE) / GamePanel.SIZE;
	}
	
	public int getRow(int pos) {
		return (pos + GamePanel.HALF_SIZE) / GamePanel.SIZE;
	}

	public abstract List<Move> getValidMoves(Board board);
	
	public Move validMove(Board board) {
		List <Move> tmp = getValidMoves(board);
		
		Point checkPosition = new Point(getCol(getX(position.x)), getRow(getY(position.y))); 
		
		
		for (Move iMove : tmp) {
			if (iMove.getTo().equals(checkPosition)) {
				return iMove;
			}
		}

		return null;
	}
}
