package game;

import component.Food;
import component.Snake;
import component.Snake.SnakeCell;

public class GameState extends GameConfig {
	
	private Snake snake;
	private Food food;
	private int score = 0;
	private float speed = 1f;
	private boolean gameDone = false;
	private boolean move = false;
	
	public GameState(int width, int height, int cellSize, int fps) {
		super(width, height, cellSize, fps);
		this.restart();
	}
	
	public boolean isGameDone() {
		return this.gameDone;
	}
	
	public void restart() {
		generateStartingState();
		this.score = 0;
		this.gameDone = false;
		
		new Thread(() -> {
			while (true) {
				if (this.isGameDone()) break;
				if (this.move) this.move();
				
				this.speed = 1 + (this.snake.getLength() - 1) / (float) (this.getWidth() * this.getHeight() - 1);
				
				try {
					Thread.sleep((long) (1000 / (float) (this.getFps() * this.speed)));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void foodHandler() {
		int foodX = this.food.getPosition().get("x");
		int foodY = this.food.getPosition().get("y");
		int snakeX = this.snake.getHead().getPosition().get("x");
		int snakeY = this.snake.getHead().getPosition().get("y");
		
		if (foodX == snakeX && foodY == snakeY) {
			this.snake.grow();
			this.spawnFood();
			this.score++;
		}
	}
	
	public void move() {
		if (this.getSnake().isCollided()) {
			this.gameDone = true;
		}
		
		if (this.gameDone) return;
		
		this.getSnake().move(this.getWidth(), this.getHeight());
		this.foodHandler();
	}
	
	private void spawnFood() {
		if (this.getSnake().getLength() == this.getWidth() * this.getHeight()) return;
		
		
		int x = (int) (Math.random() * this.getWidth());
		int y = (int) (Math.random() * this.getHeight());
		
		for (SnakeCell snakeCell : this.snake.getSnake()) {
			if (snakeCell.getPosition().get("x") == x
					&& snakeCell.getPosition().get("y") == y) {
					spawnFood();
					return;
				}
		}

		this.food = new Food(x, y);
	}
	
	public void changeDirection(int xDir, int yDir) {
		this.getSnake().changeDirection(xDir, yDir);
		this.move = true;
	}
	
	private void generateStartingState() {
		int x = (int) (Math.random() * this.getWidth());
		int y = (int) (Math.random() * this.getHeight());
		
		this.snake = new Snake(x, y);
		
		this.spawnFood();
		
		this.move = false;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public Snake getSnake() {
		return this.snake;
	}
	
	public Food getFood() {
		return this.food;
	}
	
	public float getSpeed() {
		return this.speed;
	}
}
