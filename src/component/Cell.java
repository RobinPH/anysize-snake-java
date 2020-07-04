package component;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Cell {
	protected int x;
	protected int y;
	protected Color color;
	
	public Cell(int x, int y, String color) {
		this.x = x;
		this.y = y;
		this.color = Color.decode(color);
	}
	
	public void changeColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public Map<String, Integer> getPosition() {
		Map<String, Integer> position = new HashMap<>();
		position.put("x", this.x);
		position.put("y", this.y);
		
		return position;
	}
	
	@Override
	public String toString() {
		return "Cell [x=" + x + ", y=" + y + "]";
	}
}
