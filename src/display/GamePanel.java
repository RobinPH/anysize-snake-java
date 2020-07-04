package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import component.Cell;
import component.Snake.SnakeCell;
import game.GameState;

public class GamePanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = 6220134133434701595L;
	private final GameState state;
	private final int width;
	private final int height;
	private final int cellSize;
	private final int borderSize;
	private final int xOffset;
	private final int yOffset;
	private Graphics2D graphics;
	
	public GamePanel(GameState state) {
		this.state = state;
		this.width = this.state.getWidth();
		this.height = this.state.getHeight();
		this.cellSize = this.state.getCellSize();
		this.borderSize = 1;
		this.xOffset = 200;
		this.yOffset = 40;
	}
	
	public JPanel getJPanel() {
		setPreferredSize(new Dimension(width * cellSize + (width + 1) * borderSize + xOffset + yOffset,
									   height * cellSize + (height + 1) * borderSize + 2 * yOffset));
		setFocusable(true);
        requestFocus(); 
        
        this.addKeyListener(this);
        
		return this;
	}
	
	public void render() {
		this.graphics = (Graphics2D) getGraphics();
		this.setUpFont();
		
		new Thread(() -> {
			while (true) {
				this.rerender();
				try {
					Thread.sleep((long) (1000 / (float) (this.state.getFps() * this.state.getSpeed())));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void rerender() {
		drawBackground();
		drawScoreboard();
		drawCell(this.state.getFood());
		
		try {
			List<SnakeCell> snakeCells = this.state.getSnake().clone().getSnake();
			for (SnakeCell snakeCell: snakeCells) {
				drawCell(snakeCell);
			}
		} catch (Exception e) { e.printStackTrace(); }
		
		drawGameOverScreen();
	}
	
	private void drawCell(Cell cell) {
		Map<String, Integer> position = cell.getPosition();
		int x = position.get("x");
		int y = position.get("y");
		drawCell(cell, x, y);
	}
	
	private void drawCell(Cell cell, int x, int y) {
		graphics.setColor(cell.getColor());
		graphics.fillRect(x * cellSize + borderSize * (x + 1) + xOffset,
					  	  y * cellSize + borderSize * (y + 1) + yOffset,
					  	  cellSize,
					  	  cellSize);
	}
	
	private void drawBackground() {
		graphics.setColor(Color.black);
		graphics.fillRect(0,
						  0,
						  width * cellSize + (width + 1) * borderSize + xOffset + yOffset,
						  height * cellSize + (height + 1) * borderSize + 2 * yOffset);
		
		graphics.setColor(Color.decode("#111111"));
		graphics.fillRect(xOffset,
						  yOffset,
						  width * cellSize + (width + 1) * borderSize,
						  height * cellSize + (height + 1) * borderSize);
	}
	
	private void drawScoreboard() {
		Font currentFont = graphics.getFont();
		Font newFont = currentFont.deriveFont(28f * ((float) xOffset / 200));
		graphics.setFont(newFont);
		
		FontMetrics fm = graphics.getFontMetrics();
		
		
		String snake = "SNAKE";
		graphics.setColor(Color.decode("#F7F7F7"));
		graphics.drawString(snake,
							xOffset / 2 -fm.stringWidth(snake) / 2,
							(yOffset + yOffset) + fm.getHeight() / 4);
		
		fm = graphics.getFontMetrics();
		newFont = currentFont.deriveFont(13f * ((float) xOffset / 200));
		graphics.setFont(newFont);
		
		String score = "Score " + this.state.getScore();
		graphics.setColor(Color.decode("#E0E0E0"));
		graphics.drawString(score,
							yOffset,
							(yOffset + yOffset) + fm.getHeight() / 4 + 50);
		
		String speed = String.format("Speed %.2fx", this.state.getSpeed());
		graphics.drawString(speed,
							yOffset,
							((yOffset + yOffset + fm.getHeight() - 10)+ fm.getHeight() / 4 + 50));
	}
	
	private void drawGameOverScreen() {
		if (!this.state.isGameDone()) return;
		graphics.setColor(new Color(255, 255, 255, 80));
		graphics.fillRect(xOffset,
						  yOffset,
						  width * cellSize + (width + 1) * borderSize,
						  height * cellSize + (height + 1) * borderSize);
		
		Font currentFont = graphics.getFont();
		Font newFont = currentFont.deriveFont(34f * ((float) xOffset / 200));
		graphics.setFont(newFont);
		
		FontMetrics fm = graphics.getFontMetrics();
		String gameOver = "Game Over";
		int stringWidth = fm.stringWidth((gameOver));
		int stringHeight = fm.getHeight();
		
		graphics.setColor(Color.decode("#F7F7F7"));
		graphics.drawString(gameOver,
							(width * cellSize + (width + 1) * borderSize) / 2 - stringWidth / 2 + xOffset,
							(height * cellSize + (height + 1) * borderSize) / 2 + stringHeight / 4 + yOffset);
	}

	public GameState getState() {
		return this.state;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case 40:
		case 83:
			this.state.changeDirection(0, 1);
			return;
		case 39:
		case 68:
			this.state.changeDirection(1, 0);
			return;
		case 38:
		case 87:
			this.state.changeDirection(0, -1);
			return;
		case 37:
		case 65:
			this.state.changeDirection(-1, 0);
			return;
		case 32:
		case 82:
		case 10:
			this.state.restart();
			rerender();
			return;
		default:
			return;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	private void setUpFont() {
		try {
		    Font clearSans = Font.createFont(Font.TRUETYPE_FONT, new File("font/8-Bit.ttf")).deriveFont(128f * ((float) cellSize / 100));
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(clearSans);
		    
		    this.graphics.setFont(clearSans);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		
	}
}
