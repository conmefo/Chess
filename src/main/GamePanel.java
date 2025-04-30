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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
	public static int SIZE = 50;
	public static int HALF_SIZE = 50 / 2;
	public static int PADDING_SIZE = 50 / 10;
	public static final int HEIGHT = 8;
	public static final int WIDTH = 12;
	final int FPS = 60;
	final double TIME_PER_FRAME = 1000000000 / FPS;
	
	public static ArrayList<Piece> pieces = new ArrayList<>();
	public static ArrayList<Piece> simPieces = new ArrayList<>();
	public static ArrayList<Piece> promotedPieces = new ArrayList<>();
	
	Thread gameThread;
	Board board = new Board();
	Mouse mouse = new Mouse();
	
	Piece activeP = null;
	boolean promotion = false;	
	String currentColor = "white";
	
	public static Font loadFont(String resourcePath, float size) {
        Font customFont = null;
        InputStream is = null;
        try {
            is = ArcadeFontDemo.class.getResourceAsStream(resourcePath);

            if (is == null) {
                System.err.println("Error: Font resource not found at path: " + resourcePath);
                return new Font(Font.SANS_SERIF, Font.PLAIN, (int) size);
            }

            customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont.deriveFont(size);

        } catch (FontFormatException e) {
            System.err.println("Error loading font: Font format is invalid or unsupported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error loading font: Could not read font file (IO Exception).");
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.err.println("Failed to load custom font '" + resourcePath + "', returning default SansSerif.");
        return new Font(Font.SANS_SERIF, Font.PLAIN, (int) size);
    }

	 
	public GamePanel(){
		setPreferredSize(new Dimension(WIDTH * SIZE, HEIGHT * SIZE));
		setBackground(Color.DARK_GRAY);
		addMouseMotionListener(mouse);
		addMouseListener(mouse);
		
		//testPromotion();
		setPiece();

		copyPiece(pieces, simPieces);
	}

	void resetGame(){
		activeP = null;
		promotion = false;
		currentColor = "white";
		promotedPieces.clear();
		simPieces.clear();
		pieces.clear();
		board = new Board();
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

	void testPromotion(){
		pieces.add(new Pawn(new Point(6, 6), "black", "pawn", false));
		pieces.add(new Pawn(new Point(3, 1), "white", "pawn", false));
		for (Piece p : pieces) {
	        board.setPiece(p.position, p);
	    }
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
	
	void setUpPromotedPieces() {
		promotedPieces.add(new Queen(new Point(9, 2), currentColor, "queen", false));
		promotedPieces.add(new Rook(new Point(9, 3), currentColor, "rook", false));
		promotedPieces.add(new Knight(new Point(9, 4), currentColor, "knight", false));
		promotedPieces.add(new Bishop(new Point(9, 5), currentColor, "bishop", false));
	}

	void choosePromotionPiece() {
		for (Piece piece : promotedPieces) {
			if (piece.position.x == mouse.x / SIZE && piece.position.y == mouse.y / SIZE) {		
				Point prePos = activeP.position;
				simPieces.remove(activeP);
				switch (piece.name) {
					case "queen":
						activeP = new Queen(new Point (prePos), piece.color, piece.name, true);
						break;	
					case "rook":
						activeP = new Rook(new Point (prePos), piece.color, piece.name, true);	
						break;
					case "knight":	
						activeP = new Knight(new Point (prePos), piece.color, piece.name, true);	
						break;
					case "bishop":	
						activeP = new Bishop(new Point (prePos), piece.color, piece.name, true);	
						break;
					default:
						activeP = new Queen(new Point (prePos), piece.color, piece.name, true);
						break;
				}
				simPieces.add(activeP);
				promotion = false;
				activeP = null;
				currentColor = (currentColor == "white" ? "black" : "white");
			}
		}
		if (promotion == false){
			promotedPieces.clear();
		}
	}		
	private void update() {
		if (promotion) {
			if (mouse.pressed) {
				choosePromotionPiece();
			}
			return;
		}

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

					if (tmpMove.getEnPassantPiece() != null) {
						for (Piece iPiece : simPieces) {
							if (iPiece.equals(tmpMove.getEnPassantPiece())) {
								simPieces.remove(iPiece);
								break;
							}
						}
					}
					
					if (tmpMove.isPromotion()){
						promotion = true;
						setUpPromotedPieces();
						return;
					} else {
						currentColor = (currentColor == "white" ? "black" : "white");
					}
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

		int panelWidth = getWidth();
		int panelHeight = getHeight();

	
		SIZE = Math.min(panelWidth, panelHeight) / 8;
		HALF_SIZE = SIZE / 2;
		PADDING_SIZE = SIZE / 10;
		
		Graphics2D g2 = (Graphics2D) g;
		board.draw(g2);
		
		if (activeP != null) {
			g2.setColor(new Color(100, 200, 255, 128));
			g2.fillRect(activeP.position.x * SIZE, activeP.position.y * SIZE, SIZE, SIZE);
			board.drawCanMoveSquare(g2, activeP);
		}

		for (Piece p : simPieces) {
			if (p != activeP)
			p.draw(g2, false);
		}

		for (Piece p : promotedPieces) {
			p.draw(g2, false);
		}

		if (activeP != null){
			activeP.draw(g2, true);
		}

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		String fontResourcePath = "/fonts/ARCADECLASSIC.ttf"; // <--- CHANGE THIS
        float fontSize = SIZE * 2 / 5; // Example size
        final Font arcadeFont = loadFont(fontResourcePath, fontSize);
		g2.setFont(arcadeFont);
		g2.setColor(Color.white);	
		
		int textPos = 8 * SIZE + (Math.max(panelHeight, panelWidth) - SIZE * 8) / 4;
		if (promotion){
			g2.drawString("Promotion", textPos, SIZE + HALF_SIZE);
		} else
		if (currentColor == "white") {
			g2.drawString("White", textPos, SIZE + HALF_SIZE);
		} else {
			g2.drawString("Black", textPos, SIZE + HALF_SIZE);
		}
	}
}
