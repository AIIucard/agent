package main.java;

import java.io.File;

public class DwarfConstants {
	// Dwarf Names
	public static final String GUI_AGENT_NAME = "LittlePoisenDwarfWithGUI";

	// File Path
	public static final String MAP_LOCATION_IMAGE_FILE_PATH = "src" + File.separator + "main" + File.separator + "java" + File.separator + "img" + File.separator + "mapLocations" + File.separator;
	public static final String DWARF_IMAGE_FILE_PATH = "src" + File.separator + "main" + File.separator + "java" + File.separator + "img" + File.separator + "dwarfs" + File.separator;

	// MapLocation
	public static final int RESIZE_CHANGE_NUMBER = 3;

	// Movement Orders
	public static final String MOVE_UP = "UP";
	public static final String MOVE_DOWN = "DOWN";
	public static final String MOVE_LEFT = "LEFT";
	public static final String MOVE_RIGHT = "RIGHT";

	// Path Finding
	public static final int MOVEMENT_COSTS = 1;

	// Subjects
	public static final String UPDATE_MAP_MESSAGE_REPLY = "UpdateMap";
	public static final String REQUEST_MOVEMENTORDER_MESSAGE_REPLY = "RequestMovementOrder";
}
