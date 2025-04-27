package main;

import javax.swing.*;
import piece.*;
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
	
	//public static ArrayList<Piece> pieces = new ArrayList<>();
	public static ArrayList<Piece> pieces = new ArrayList<>();
	public static ArrayList<Piece> promotedPieces = new ArrayList<>();
	
	Thread gameThread;
	Board board = new Board();
	Mouse mouse = new Mouse();
	
	Piece activeP = null;
	boolean promotion = false;	
	String currentColor = "white";
	
	 
	public GamePanel(){
		setPreferredSize(new Dimension(WIDTH * SIZE, HEIGHT * SIZE));
		setBackground(Color.DARK_GRAY);
		addMouseMotionListener(mouse);
		addMouseListener(mouse);
		
		setPiece();
	}
	
	
	public void LaunchGame() {
		gameThread = new Thread(this);
		gameThread.start();
	}


	void setPiece() {
		pieces.add(new Rook(0, 0, "black", "rook", false));
		pieces.add(new Rook(7, 0, "black", "rook", false));
		pieces.add(new Rook(0, 7, "white", "rook", false));
		pieces.add(new Rook(7, 7, "white", "rook", false));

		pieces.add(new Knight(1, 0, "black", "knight", false));
		pieces.add(new Knight(6, 0, "black", "knight", false));
		pieces.add(new Knight(1, 7, "white", "knight", false));
		pieces.add(new Knight(6, 7, "white", "knight", false));

		pieces.add(new Bishop(2, 0, "black", "bishop", false));
		pieces.add(new Bishop(5, 0, "black", "bishop", false));
		pieces.add(new Bishop(2, 7, "white", "bishop", false));
		pieces.add(new Bishop(5, 7, "white", "bishop", false));

		pieces.add(new Queen(3, 0, "black", "queen", false));
		pieces.add(new Queen(3, 7, "white", "queen", false));

		pieces.add(new King(4, 0, "black", "king", false));
		pieces.add(new King(4, 7, "white", "king", false));

		for (int i = 0; i < 8; i++) {
		    pieces.add(new Pawn(i, 1, "black", "pawn", false));
		    pieces.add(new Pawn(i, 6, "white", "pawn", false));
		}
		
		for (Piece p : pieces) {
	        board.setPiece(p.curCol, p.curRow, p);
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
	

	void choosePromotionPiece() {

	}		

	private void update() {
		if (mouse.pressed) {
			if (activeP == null) {
				for (Piece piece : pieces) {
					if (piece.color == currentColor && piece.newCol == mouse.x / SIZE && piece.newRow == mouse.y / SIZE) {
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
					
					activeP.finalUpdatePosition();
					board.performMove(tmpMove);
					
					if (tmpMove.capturedPiece != null) {
						for (Piece iPiece : pieces) {
							if (iPiece.equals(tmpMove.capturedPiece)) {
								pieces.remove(iPiece);
								break;
							}
						}
					}

					if (tmpMove.enPassantPiece != null) {
						for (Piece iPiece : pieces) {
							if (iPiece.equals(tmpMove.enPassantPiece)) {
								pieces.remove(iPiece);
								break;
							}
						}
					}
					
					if (tmpMove.isPromotion){
						promotion = true;
						//setUpPromotedPieces();
						return;
					} else {
						currentColor = (currentColor == "white" ? "black" : "white");
					}
				} else {
					System.out.println("Invalid move!");
					activeP.returnPosition();
				}
				
				activeP = null;
			}
		}
	}
	
	private void simulate() {
		activeP.x = mouse.x - HALF_SIZE;
		activeP.y = mouse.y - HALF_SIZE;
		activeP.newCol = activeP.x;
		activeP.newRow = activeP.y;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		board.draw(g2);
		
		if (activeP != null) {
			g2.setColor(new Color(100, 200, 255, 128));
			g2.fillRect(activeP.newCol * SIZE, activeP.newRow * SIZE, SIZE, SIZE);
			board.drawCanMoveSquare(g2, activeP);
		}

		for (Piece p : pieces) {
			p.draw(g2);
		}

		for (Piece p : promotedPieces) {
			p.draw(g2);
		}

		if (activeP != null){
			activeP.draw(g2);
		}

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(new Font("Arial", Font.PLAIN, 20));
		g2.setColor(Color.white);	
		
		if (promotion){
			g2.drawString("Promotion", 8 * SIZE + 20, SIZE + HALF_SIZE);
		} else
		if (currentColor == "white") {
			g2.drawString("White's turn", 8 * SIZE + 20, SIZE + HALF_SIZE);
		} else {
			g2.drawString("Black's turn", 8 * SIZE + 20, SIZE + HALF_SIZE);
		}
	}
}
