package main.java.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class DwarfVisualCenter extends JFrame {

	private static final long serialVersionUID = 1L;

	public DwarfVisualCenter() {
		setTitle("DwarfVisualCenter");
		// this.setExtendedState(this.getExtendedState() |
		// JFrame.MAXIMIZED_BOTH);
		setSize(800, 400);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			}
		});
		setVisible(true);
	}
}
