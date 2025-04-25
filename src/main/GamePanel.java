package main;

import javax.swing.*;

import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Move;
import piece.Pawn;
import piece.Piece;
import piece.Queen;
import piece.Rook;

import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
	public static final int SIZE = 50;
	public static final int HALF_SIZE = SIZE / 2;
	public static final int PADDING_SIZE = SIZE / 10;
	public static final int HEIGHT = 8;
	public static final int WIDTH = 11;
	final int FPS = 60;
	final double TIME_PER_FRAME = 1000000000 / FPS;
	
	public static ArrayList<Piece> pieces = new ArrayList<>();
	public static ArrayList<Piece> simPieces = new ArrayList<>();
	
	Thread gameThread;
	Board board = new Board();
	Mouse mouse = new Mouse();
	
	Piece activeP = null;
	
	String currentColor = "white";
	
	 
	public GamePanel(){
		setPreferredSize(new Dimension(WIDTH * SIZE, HEIGHT * SIZE));
		setBackground(Color.DARK_GRAY);
		addMouseMotionListener(mouse);
		addMouseListener(mouse);
		setPiece();
		copyPiece(pieces, simPieces);
	}
	
	void copyPiece (ArrayList<Piece> source,  ArrayList<Piece> target){
		target.clear();
		for (int i = 0; i < source.size(); ++i) {
			target.add(source.get(i));
		}
	}
	
	public void LaunchGame() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	void setPiece() {
		pieces.add(new Rook(new Point(0, 0), "black", "rook", false));
		pieces.add(new Rook(new Point(7, 0), "black", "rook", false));
		pieces.add(new Rook(new Point(0, 7), "white", "rook", false));
		pieces.add(new Rook(new Point(7, 7), "white", "rook", false));

		pieces.add(new Knight(new Point(1, 0), "black", "knight", false));
		pieces.add(new Knight(new Point(6, 0), "black", "knight", false));
		pieces.add(new Knight(new Point(1, 7), "white", "knight", false));
		pieces.add(new Knight(new Point(6, 7), "white", "knight", false));

		pieces.add(new Bishop(new Point(2, 0), "black", "bishop", false));
		pieces.add(new Bishop(new Point(5, 0), "black", "bishop", false));
		pieces.add(new Bishop(new Point(2, 7), "white", "bishop", false));
		pieces.add(new Bishop(new Point(5, 7), "white", "bishop", false));

		pieces.add(new Queen(new Point(3, 0), "black", "queen", false));
		pieces.add(new Queen(new Point(3, 7), "white", "queen", false));

		pieces.add(new King(new Point(4, 0), "black", "king", false));
		pieces.add(new King(new Point(4, 7), "white", "king", false));

		for (int i = 0; i < 8; i++) {
		    pieces.add(new Pawn(new Point(i, 1), "black", "pawn", false));
		    pieces.add(new Pawn(new Point(i, 6), "white", "pawn", false));
		}
		
		for (Piece p : pieces) {
	        board.setPiece(p.position, p);
	    }
	}
	 
	@Override
	public void run() {
		double delta = 0;
		long lastTime = System.nanoTime();
		
		while (gameThread != null) {
			
			long currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / TIME_PER_FRAME;
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
			
		}
	}
	
	private void update() {
		if (mouse.pressed) {
			if (activeP == null) {
				
				for (Piece piece : simPieces) {
					if (piece.color == currentColor && piece.position.x == mouse.x / SIZE && piece.position.y == mouse.y / SIZE) {
						activeP = piece;	
					}
				}
				
			} else {
				simulate();
			}
		} else {
			if (activeP != null) {
				System.out.println(activeP.color + " " + activeP.name);
				Move tmpMove = activeP.validMove(board);
				if (tmpMove != null) {
					activeP.updatePosition();
					board.performMove(tmpMove);
					if (tmpMove.getCapturedPiece() != null) {
						for (Piece iPiece : simPieces) {
							if (iPiece.equals(tmpMove.getCapturedPiece())) {
								simPieces.remove(iPiece);
								break;
							}
						}
					}
					currentColor = (currentColor == "white" ? "black" : "white");
				} else {
					activeP.returnPosition();
				}
				
				activeP = null;
			}
		}
	}
	
	private void simulate() {
		activeP.x = mouse.x - HALF_SIZE;
		activeP.y = mouse.y - HALF_SIZE;
		activeP.position = new Point(activeP.getCol(activeP.x), activeP.getRow(activeP.y));
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		board.draw(g2);
		
		if (activeP != null) {
			g2.setColor(new Color(100, 200, 255, 128));
			g2.fillRect(activeP.position.x * SIZE, activeP.position.y * SIZE, SIZE, SIZE);
		}

		for (Piece p : simPieces) {
			p.draw(g2);
		}
		
	}
}
