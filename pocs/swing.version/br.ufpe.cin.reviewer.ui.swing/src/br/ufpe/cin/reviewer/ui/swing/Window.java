package br.ufpe.cin.reviewer.ui.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {

	private JPanel windowPanel;
	
	public Window() {
		configure();
		
	}
	
	private void configure() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setResizable(true);
		setVisible(true);
	}

	public static void main(String[] args) {
		Window window = new Window();
	}
	
}
