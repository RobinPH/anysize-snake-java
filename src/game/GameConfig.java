package game;

public class GameConfig {
	private int width = 10;
	private int height = 10;
	private int cellSize = 40;
	private int fps = 4;
	
	public GameConfig(int width, int height, int cellSize, int fps) {
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		this.fps = fps;
	}
	
	public GameConfig() {}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getFps() {
		return this.fps;
	}
	
	public int getCellSize() {
		return this.cellSize;
	}
	
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
}
