package main.java.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.DwarfConstants;
import main.java.agent.GUILittlePoisenDwarf;
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

	// Image Dwarfs
	private Image greenDwarf;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private GUILittlePoisenDwarf owner;
	private int squareDimensionSize = 50;

	public MapEditor(GUILittlePoisenDwarf owner) {
		this.owner = owner;
		try {
			// Load MapLocations
			clearField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "clear.jpg"));
			foodAndSmellAndStenchField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "food_smell_stench.jpg"));
			foodAndSmellField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "food_smell.jpg"));
			foodAndStenchField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "food_stench.jpg"));
			foodField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "food.jpg"));
			obstacleAndSmellAndStenchField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "obstacle_smell_stench.jpg"));
			obstacleAndSmellField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "obstacle_smell.jpg"));
			obstacleAndStenchField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "obstacle_stench.jpg"));
			obstacleField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "obstacle.jpg"));
			pitField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "pit.jpg"));
			smellAndStenchField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "smell_stench.jpg"));
			smellField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "smell.jpg"));
			startField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "start.jpg"));
			stenchField = ImageIO.read(new File(DwarfConstants.MAP_LOCATION_IMAGE_FILE_PATH + "stench.jpg"));

			// Load Dwarfs
			greenDwarf = ImageIO.read(new File(DwarfConstants.DWARF_IMAGE_FILE_PATH + "dwarf_green.png"));
		} catch (IOException ioex) {
			log.error("Error while loading images!", ioex);
		}
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

		// Display Map
		for (int col = 0; col < owner.getDwarfDatabase().getMapLocations().length; col++) {
			for (int row = 0; row < owner.getDwarfDatabase().getMapLocations()[0].length; row++) {
				Rectangle grid = new Rectangle(100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize);
				Stroke oldStroke = g2.getStroke();
				g2.setStroke(new BasicStroke(2));
				g2.draw(grid);
				if ((owner.getDwarfDatabase().getMapLocations()[col][row] != null) && (owner.getDwarfDatabase().getMapLocations()[col][row] instanceof UnknownMapLocation)) {
					g2.setColor(INVESTIGATION_FIELD_COLOR);
					g2.fill(grid);
				} else if ((owner.getDwarfDatabase().getMapLocations()[col][row] != null) && (owner.getDwarfDatabase().getMapLocations()[col][row] instanceof MapLocation)) {
					MapLocation location = owner.getDwarfDatabase().getMapLocations()[col][row];
					if (location.isStartField()) {
						g2.drawImage(startField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
					} else if (location.getLocationStatus().contains(LocationStatus.CLEAR)) {
						g2.drawImage(clearField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
					} else if (location.getLocationStatus().contains(LocationStatus.PIT)) {
						g2.drawImage(pitField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
					} else {
						if (location.getLocationStatus().contains(LocationStatus.FOOD) && location.getLocationStatus().contains(LocationStatus.SMELL)
								&& location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(foodAndSmellAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.FOOD) && location.getLocationStatus().contains(LocationStatus.SMELL)) {
							g2.drawImage(foodAndSmellField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.FOOD) && location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(foodAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.FOOD)) {
							g2.drawImage(foodField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.OBSTACLE) && location.getLocationStatus().contains(LocationStatus.SMELL)
								&& location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(obstacleAndSmellAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.OBSTACLE) && location.getLocationStatus().contains(LocationStatus.SMELL)) {
							g2.drawImage(obstacleAndSmellField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.OBSTACLE) && location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(obstacleAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.OBSTACLE)) {
							g2.drawImage(obstacleField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.SMELL) && location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(smellAndStenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.SMELL)) {
							g2.drawImage(smellField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
						} else if (location.getLocationStatus().contains(LocationStatus.STENCH)) {
							g2.drawImage(stenchField, 100 + col * squareDimensionSize, 100 + row * squareDimensionSize, squareDimensionSize, squareDimensionSize, null);
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
			g2.drawImage(greenDwarf, 100 + location.getIntColumnCoordinate() * squareDimensionSize, 100 + location.getIntRowCoordinate() * squareDimensionSize, squareDimensionSize,
					squareDimensionSize, null);
		}
		g2.dispose();
	}
}
