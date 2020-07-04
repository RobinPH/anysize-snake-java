package component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Snake implements Cloneable {
	private List<SnakeCell> snake;
	private Map<String, Integer> direction;
	private boolean grow = false;
	private boolean collided = false;
	
	public Snake(int x, int y) {
		this.snake = new ArrayList<SnakeCell>();
		this.snake.add(new SnakeCell(x, y, true));
		this.direction = new HashMap<>();
	}
	
	public void grow() {
		this.grow = true;
	}
	
	public void move(int xBound, int yBound) {
		int xDir = this.getDirection().get("x");
		int yDir = this.getDirection().get("y");
		
		SnakeCell head = this.getHead();
		
		Map<String, Integer> headPosition = head.getPosition();
		
		int newX = headPosition.get("x") + xDir;
		int newY = headPosition.get("y") + yDir;
		
		this.snake.forEach((snakeCell) -> {
			if (snakeCell.getPosition().get("x") == newX
				&& snakeCell.getPosition().get("y") == newY) {
				this.collided = true;
				return;
			}
		});
		
		if (newX >= 0 && newX < xBound && newY >= 0 && newY < yBound && !this.collided) {
			head.setHead(false);
			this.snake.add(0, new SnakeCell(newX,
					newY,
					true));
			if (this.grow) {
				this.grow = false;
			} else {
				this.getSnake().remove(this.getSnake().size() - 1);
			}
		} else {
			this.collided = true;
		}
	}
	
	public void changeDirection(int x, int y) {
		if (this.direction.size() != 0) {
			int currentX = this.direction.get("x");
			int currentY = this.direction.get("y");
			
			if (-1 * x == currentX && -1 * y == currentY) return;
		}
		
		this.getDirection().put("x", x);
		this.getDirection().put("y", y);
	}
	
	public Map<String, Integer> getPosition() {
		return this.getSnake().get(0).getPosition();
	}
	
	public Map<String, Integer> getDirection() {
		return this.direction;
	}
	
	public int getLength() {
		return this.getSnake().size();
	}
	
	public List<SnakeCell> getSnake() {
		return this.snake;
	}
	
	public SnakeCell getHead() {
		return this.snake.get(0);
	}
	
	public boolean isCollided() {
		return this.collided;
	}
	
	public Snake clone() throws CloneNotSupportedException {
	    return (Snake) super.clone();
	}
	
	public class SnakeCell extends Cell {
		private boolean head;
		
		public SnakeCell(int x, int y, boolean b) {
			super(x, y, "#22ff22");
			this.setHead(b);
		}
		
		public void increasePosition(int x, int y) {
			this.x += x;
			this.y += y;
		}
		
		public void setHead(boolean b) {
			this.head = b;
			
			if (this.head) {
				this.changeColor(Color.decode("#009300"));
			} else {
				this.changeColor(Color.decode("#00ff00"));
			}
		}
		
		public boolean isHead() {
			return this.head;
		}
	}
}
