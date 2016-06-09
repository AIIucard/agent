package main.java.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import main.java.agent.GUILittlePoisenDwarf;

public class MapEditor extends JComponent {

	private static final long serialVersionUID = 1L;
	public static final Color START_FIELD_COLOR = Color.BLUE;
	public static final Color BLOCKADE_FIELD_COLOR = Color.RED;
	public static final Color FOOD_FIELD_COLOR = Color.ORANGE;
	public static final Color SMELL_FIELD_COLOR = Color.YELLOW;
	public static final Color TRAP_FIELD_COLOR = Color.BLACK;
	public static final Color STENCH_FIELD_COLOR = Color.LIGHT_GRAY;
	public static final Color CLEAR_FIELD_COLOR = Color.WHITE;
	public static final Color UNKNOWN_FIELD_COLOR = Color.DARK_GRAY;

	private GUILittlePoisenDwarf owner;
	private int squareDimensionSize = 50;

	public MapEditor(GUILittlePoisenDwarf owner) {
		this.owner = owner;
	}

	public void updateMap() {
		repaint();
		revalidate();
	}

	@Override
	public void paintComponent(Graphics g) {
		// TODO
		super.paintComponent(g); // Call it's parent for proper rendering.
		Graphics2D g2 = (Graphics2D) g;
		for (int x = 0; x < owner.getDwarfDatabase().getMapLocations().length; x++) {
			for (int y = 0; y < owner.getDwarfDatabase().getMapLocations()[0].length; y++) {
				if (owner.getDwarfDatabase().getMapLocations()[x][y] == null) {
					g2.setColor(UNKNOWN_FIELD_COLOR);
				}
				Rectangle grid = new Rectangle(100 + x * squareDimensionSize, 100 + y * squareDimensionSize,
						squareDimensionSize, squareDimensionSize);
				g2.fill(grid);
				// g2.draw(grid);

				// g2.setStroke(new BasicStroke(2));
				// g2.fillRect(this.getLocationOnScreen().x + (x *
				// squareDimensionSize), this.getLocationOnScreen().y + (y *
				// squareDimensionSize), squareDimensionSize,
				// squareDimensionSize);
			}
		}
		g2.dispose();
	}
	// Display dwarfs
}
