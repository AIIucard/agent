package main.java.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.DwarfConstants;
import main.java.agent.KingLittlePoisenDwarf;
import main.java.map.MapLocation.LocationStatus;

public class MapEditor extends JComponent {

	private static final long serialVersionUID = 1L;

	// Colors
	public static final Color UNKNOWN_FIELD_COLOR = Color.LIGHT_GRAY;
	public static final Color INVESTIGATION_FIELD_COLOR = Color.YELLOW;
	public static final Color STANDARD_BOARDER_COLOR = Color.BLACK;

	// Image MapLocation
	private Image clearField;
	private Image foodAndSmellAndStenchField;
	private Image foodAndSmellField;
	private Image foodAndStenchField;
	private Image foodField;
	private Image obstacleAndSmellAndStenchField;
	private Image obstacleAndSmellField;
	private Image obstacleAndStenchField;
	private Image obstacleField;
	private Image pitField;
	private Image smellAndStenchField;
	private Image smellField;
	private Image startField;
	private Image stenchField;

	private Dimension instanceSize;

	// Image Dwarfs
	private Image greenDwarf;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private KingLittlePoisenDwarf owner;
	private int squareDimensionSize = DwarfConstants.SQUARE_DIMENSION;

	public MapEditor(KingLittlePoisenDwarf owner, int col, int row) {
		this.owner = owner;
		instanceSize = new Dimension((col * squareDimensionSize) + 200, (row * squareDimensionSize) + 200);
		// Load MapLocations
		clearField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/clear.jpg"));
		foodAndSmellAndStenchField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/food_smell_stench.jpg"));
		foodAndSmellField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/food_smell.jpg"));
		foodAndStenchField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/food_stench.jpg"));
		foodField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/food.jpg"));
		obstacleAndSmellAndStenchField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/obstacle_smell_stench.jpg"));
		obstacleAndSmellField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/obstacle_smell.jpg"));
		obstacleAndStenchField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/obstacle_stench.jpg"));
		obstacleField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/obstacle.jpg"));
		pitField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/pit.jpg"));
		smellAndStenchField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/smell_stench.jpg"));
		smellField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/smell.jpg"));
		startField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/start.jpg"));
		stenchField = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/stench.jpg"));

		// Load Dwarfs
		greenDwarf = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/dwarf_green.png"));
	}

	@Override
	public Dimension getPreferredSize() {
		return instanceSize;
	}

	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		instanceSize = d;
	}

	public void updateMap() {
		repaint();
		revalidate();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Call it's parent for proper rendering.
		Graphics2D g2 = (Graphics2D) g;

		// Display Map
		for (int col = 0; col < owner.getDwarfDatabase().getMapLocations().length; col++) {
			for (int row = 0; row < owner.getDwarfDatabase().getMapLocations()[0].length; row++) {
				Rectangle grid = new Rectangle(100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize);
				Stroke oldStroke = g2.getStroke();
				g2.setStroke(new BasicStroke(2));
				g2.draw(grid);
				if ((owner.getDwarfDatabase().getMapLocations()[col][row] != null)
						&& (owner.getDwarfDatabase().getMapLocations()[col][row] instanceof UnknownMapLocation)) {
					g2.setColor(INVESTIGATION_FIELD_COLOR);
					g2.fill(grid);
				} else if ((owner.getDwarfDatabase().getMapLocations()[col][row] != null)
						&& (owner.getDwarfDatabase().getMapLocations()[col][row] instanceof MapLocation)) {
					MapLocation location = owner.getDwarfDatabase().getMapLocations()[col][row];
					if (location.isStartField()) {
						g2.drawImage(startField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
					} else if (location.getLocationStatus().contains(LocationStatus.PIT)) {
						g2.drawImage(pitField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
					} else {
						if (location.getLocationStatus().contains(LocationStatus.FOOD) && location.getLocationStatus().contains(LocationStatus.SMELL)
								&& location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(foodAndSmellAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize,
									squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.FOOD) && location.getLocationStatus().contains(LocationStatus.SMELL)) {
							g2.drawImage(foodAndSmellField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize,
									null);
						} else if (location.getLocationStatus().contains(LocationStatus.FOOD) && location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(foodAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize,
									null);
						} else if (location.getLocationStatus().contains(LocationStatus.FOOD)) {
							g2.drawImage(foodField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.OBSTACLE) && location.getLocationStatus().contains(LocationStatus.SMELL)
								&& location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(obstacleAndSmellAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize,
									squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.OBSTACLE) && location.getLocationStatus().contains(LocationStatus.SMELL)) {
							g2.drawImage(obstacleAndSmellField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize,
									squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.OBSTACLE) && location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(obstacleAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize,
									squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.OBSTACLE)) {
							g2.drawImage(obstacleField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.SMELL) && location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(smellAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize,
									null);
						} else if (location.getLocationStatus().contains(LocationStatus.SMELL)) {
							g2.drawImage(smellField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(stenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.FREE)) {
							g2.drawImage(clearField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else {
							log.error("Location status unknown! Can not draw image...");
						}
					}
				} else {
					g2.setColor(UNKNOWN_FIELD_COLOR);
					g2.fill(grid);
				}
				g2.setColor(STANDARD_BOARDER_COLOR);
				g2.setStroke(oldStroke);
			}
		}
		// Display Dwarfs
		Set<Entry<String, MapLocation>> set = owner.getDwarfDatabase().getDwarfPositions().entrySet();
		Iterator<Entry<String, MapLocation>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			MapLocation location = (MapLocation) mentry.getValue();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2.drawImage(greenDwarf, 100 + location.getIntColumnCoordinate() * squareDimensionSize, 100 + location.getIntRowCoordinate() * squareDimensionSize,
					squareDimensionSize, squareDimensionSize, null);
		}
		g2.dispose();
	}
}
