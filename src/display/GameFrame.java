package display;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import game.GameState;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private GamePanel panel;
	
	public GameFrame(GameState state) {
		this.panel = new GamePanel(state);
		add(this.panel.getJPanel(), BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Snake");
		pack();
        setVisible(true);
        
        try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        this.panel.render();
	}
}
