package main.java.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

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
	public static final Color STANDARD_BOARDER_COLOR = Color.BLACK;

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

		for (int col = 0; col < owner.getDwarfDatabase().getMapLocations().length; col++) {
			for (int row = 0; row < owner.getDwarfDatabase().getMapLocations()[0].length; row++) {
				Rectangle grid = new Rectangle(100 + col * squareDimensionSize, 100 + row * squareDimensionSize,
						squareDimensionSize, squareDimensionSize);
				Stroke oldStroke = g2.getStroke();
				g2.setStroke(new BasicStroke(2));
				g2.draw(grid);
				if (owner.getDwarfDatabase().getMapLocations()[col][row] != null) {
					if (owner.getDwarfDatabase().getMapLocations()[col][row].isStartField()) {
						g2.setColor(START_FIELD_COLOR);
					}
					// g2.setColor(SMELL_FIELD_COLOR);
					// g2.fill(grid);
					// g2.setColor(STANDARD_BOARDER_COLOR);
				} else {
					g2.setColor(UNKNOWN_FIELD_COLOR);
				}
				g2.fill(grid);
				g2.setColor(STANDARD_BOARDER_COLOR);
				g2.setStroke(oldStroke);
			}
		}
		g2.dispose();
	}
	// Display dwarfs
}
