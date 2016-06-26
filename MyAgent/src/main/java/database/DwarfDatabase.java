package main.java.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import main.java.DwarfConstants;
import main.java.map.MapLocation;
import main.java.map.UnknownMapLocation;
import main.java.utils.DwarfUtils;

public class DwarfDatabase {
	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private HashMap<String, AgentController> dwarfs;
	private AgentContainer dwarfContainer;
	private MapLocation[][] mapLocations;
	private List<UnknownMapLocation> locationsToBeInvestigated;
	private List<MapLocation> locationsWithFood;
	private HashMap<String, MapLocation> dwarfPositions;
	private int dwarfCounter;

	public DwarfDatabase() {
		dwarfs = new HashMap<String, AgentController>();
		mapLocations = new MapLocation[15][15];
		dwarfCounter = 0;
		locationsToBeInvestigated = new ArrayList<UnknownMapLocation>();
		locationsWithFood = new ArrayList<MapLocation>();
		dwarfPositions = new HashMap<String, MapLocation>();
	}

	public boolean updateExistingMapLocation(boolean isTrap, boolean isBlockade, int col, int row, int foodUnits, int smellConcentration, int stenchConcentration) {
		if (col > mapLocations.length || row > mapLocations[0].length) {
			log.error("Location [{},{}] is not an existing mapLocation", row, col);
			return false;
		}
		mapLocations[col][row].updateLocation(col, row, smellConcentration, stenchConcentration, foodUnits,
				DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration, stenchConcentration));
		return true;
	}

	public boolean updateMapLocation(boolean isStartfield, boolean isTrap, boolean isBlockade, int col, int row, int foodUnits, int smellConcentration, int stenchConcentration, String dwarfName) {
		if (col >= mapLocations.length - 1) {
			log.info("Start resizing...");
			int newColumns = resizeColumns(col);
			log.info("Resized mapLocations and added {} new columns.", (newColumns * DwarfConstants.RESIZE_CHANGE_NUMBER));
		}
		if (row >= mapLocations[0].length - 1) {
			log.info("Start resizing...");
			int newRows = resizeRows(row);
			log.info("Resized mapLocations and added {} new rows.", newRows);
		}
		if ((dwarfName != null) && !dwarfName.equals("")) {
			if (mapLocations[col][row] == null) {
				// Location Status
				mapLocations[col][row] = new MapLocation(col, row, smellConcentration, stenchConcentration, foodUnits,
						DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration, stenchConcentration));
				mapLocations[col][row].setStartField(isStartfield);
				log.info("Added new {}", mapLocations[col][row].toString());

				// Dwarf Position
				if (dwarfPositions.containsKey(dwarfName)) {
					dwarfPositions.remove(dwarfName);
					dwarfPositions.put(dwarfName, mapLocations[col][row]);
					log.info("Moved dwarf {} to MapLocation {}", dwarfName, mapLocations[col][row].toString());
				} else {
					dwarfPositions.put(dwarfName, mapLocations[col][row]);
					log.info("Added dwarf {} to MapLocation {}", dwarfName, mapLocations[col][row].toString());
				}

				// Surrounding Locations
				addSurroundingLocationsToBeInvestigated(row, col);

				// Food Locations
				if (foodUnits > 0) {
					addFoodLocation(row, col, mapLocations[col][row]);
				}
				return true;
			} else {
				mapLocations[col][row].updateLocation(col, row, smellConcentration, stenchConcentration, foodUnits,
						DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration, stenchConcentration));

				// Dwarf Position
				if (dwarfPositions.containsKey("dwarfName")) {
					dwarfPositions.remove("dwarfName");
				}
				dwarfPositions.put(dwarfName, mapLocations[col][row]);
				log.info("Added moved dwarf {} to new location {}", dwarfName, mapLocations[col][row].toString());
				return true;
			}
		}
		return false;
	}

	private void addFoodLocation(int col, int row, MapLocation loc) {
		if (checkForLocationNotInFoodLocationList(col, row)) {
			locationsWithFood.add(loc);
		}
	}

	private void addSurroundingLocationsToBeInvestigated(int row, int col) {
		if (col - 1 >= 0) {
			if (mapLocations[col - 1][row] == null) {
				if (checkForLocationNotInInvestigationQueue(col - 1, row)) {
					mapLocations[col - 1][row] = new UnknownMapLocation(col - 1, row);
					locationsToBeInvestigated.add(new UnknownMapLocation(col - 1, row));
					log.info("Added new {} to investigation list", locationsToBeInvestigated.get(locationsToBeInvestigated.size() - 1));
				}
			}
		}
		if ((row - 1) >= 0) {
			if (mapLocations[col][row - 1] == null) {
				if (checkForLocationNotInInvestigationQueue(col, row - 1)) {
					mapLocations[col][row - 1] = new UnknownMapLocation(col, row - 1);
					locationsToBeInvestigated.add(new UnknownMapLocation(col, row - 1));
					log.info("Added new {} to investigation list", locationsToBeInvestigated.get(locationsToBeInvestigated.size() - 1));
				}
			}
		}
		if (!(col + 1 >= mapLocations.length)) {
			if (mapLocations[col + 1][row] == null) {
				if (checkForLocationNotInInvestigationQueue(col + 1, row)) {
					mapLocations[col + 1][row] = new UnknownMapLocation(col + 1, row);
					locationsToBeInvestigated.add(new UnknownMapLocation(col + 1, row));
					log.info("Added new {} to investigation list", locationsToBeInvestigated.get(locationsToBeInvestigated.size() - 1));
				}
			}
		}
		if (!(row + 1 >= mapLocations[0].length)) {
			if (mapLocations[col][row + 1] == null) {
				if (checkForLocationNotInInvestigationQueue(col, row + 1)) {
					mapLocations[col][row + 1] = new UnknownMapLocation(col, row + 1);
					locationsToBeInvestigated.add(new UnknownMapLocation(col, row + 1));
					log.info("Added new {} to investigation list", locationsToBeInvestigated.get(locationsToBeInvestigated.size() - 1));
				}
			}
		}
	}

	public boolean checkForLocationNotInFoodLocationList(int col, int row) {
		for (int i = 0; i < locationsWithFood.size(); ++i) {
			if ((locationsWithFood.get(i).getColumnCoordinate() == col) && (locationsWithFood.get(i).getRowCoordinate() == row))
				return false;
		}
		return true;
	}

	public boolean checkForLocationNotInInvestigationQueue(int col, int row) {
		for (int i = 0; i < locationsToBeInvestigated.size(); ++i) {
			if ((locationsToBeInvestigated.get(i).getColumnCoordinate() == col) && (locationsToBeInvestigated.get(i).getRowCoordinate() == row))
				return false;
		}
		return true;
	}

	private int resizeColumns(int col) {
		int countResizes = 0;
		while (col >= ((countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER) + mapLocations.length)) {
			++countResizes;
		}
		int oldColumnCount = mapLocations.length;
		int oldRowCount = mapLocations[0].length;
		MapLocation[][] newMapLocations = new MapLocation[oldColumnCount + (countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER)][oldRowCount];
		for (int columns = 0; columns < oldColumnCount; columns++) {
			for (int rows = 0; rows < oldRowCount; rows++) {
				newMapLocations[columns][rows] = mapLocations[columns][rows];
			}
		}
		mapLocations = newMapLocations;
		return countResizes;
	}

	private int resizeRows(int row) {
		int countResizes = 1;
		while (row >= ((countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER) + mapLocations[0].length)) {
			++countResizes;
		}
		int oldColumnCount = mapLocations.length;
		int oldRowCount = mapLocations[0].length;
		MapLocation[][] newMapLocations = new MapLocation[oldColumnCount][oldRowCount + (countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER)];
		for (int i = 0; i < oldColumnCount; i++) {
			for (int j = 0; j < oldRowCount; j++) {
				newMapLocations[i][j] = mapLocations[i][j];
			}
		}
		mapLocations = newMapLocations;
		return countResizes;
	}

	public HashMap<String, AgentController> getDwarfs() {
		return dwarfs;
	}

	public void recruitDwarf(String name, AgentController agent) {
		dwarfs.put(name, agent);
	}

	public AgentContainer getDwarfContainer() {
		return dwarfContainer;
	}

	public void setDwarfContainer(AgentContainer agentContainer) {
		this.dwarfContainer = agentContainer;
	}

	public HashMap<String, MapLocation> getDwarfPositions() {
		return dwarfPositions;
	}

	public int getDwarfCounter() {
		return dwarfCounter;
	}

	public void incrementDwarfCounter() {
		dwarfCounter = dwarfCounter + 1;
	}

	public void decrementDwarfCounter() {
		if (dwarfCounter > 0)
			dwarfCounter = dwarfCounter - 1;
	}

	public MapLocation[][] getMapLocations() {
		return mapLocations;
	}

	public List<UnknownMapLocation> getLocationsToBeInvestigated() {
		return locationsToBeInvestigated;
	}

	public List<MapLocation> getLocationsWithFood() {
		return locationsWithFood;
	}
}
