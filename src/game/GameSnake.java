package game;

import display.GameFrame;

public class GameSnake {
	public static void main(String[] args) {
		new GameSnake();
	}
	
	public GameSnake() {
		GameState state = new GameState(20, 20, 20, 4);
		new GameFrame(state);
	}
}
