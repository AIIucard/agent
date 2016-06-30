package main.java.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.lang.acl.ACLMessage;
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
	private MapLocation homeLocation;
	private int dwarfCounter;

	public DwarfDatabase() {
		dwarfs = new HashMap<String, AgentController>();
		mapLocations = new MapLocation[15][15];
		dwarfCounter = 0;
		locationsToBeInvestigated = new ArrayList<UnknownMapLocation>();
		locationsWithFood = new ArrayList<MapLocation>();
		dwarfPositions = new HashMap<String, MapLocation>();
	}

	public boolean updateMapLocation(boolean isStartfield, boolean isTrap, boolean isBlockade, int col, int row, int foodUnits, int smellConcentration,
			int stenchConcentration, String dwarfName, int performative) {
		checkForResize(col, row);
		if ((dwarfName != null) && !dwarfName.equals("")) {
			if (mapLocations[col][row] == null || mapLocations[col][row] instanceof UnknownMapLocation) {
				// Location Status
				mapLocations[col][row] = new MapLocation(col, row, smellConcentration, stenchConcentration, foodUnits,
						DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration, stenchConcentration));
				if (isStartfield && homeLocation == null) {
					mapLocations[col][row].setStartField(isStartfield);
					setHomeLocation(mapLocations[col][row]);
					log.info("Added new home location at position [{},{}]", col, row);
				}
				log.info("Added new {}", mapLocations[col][row].toString());

				updateDwarfPosition(col, row, dwarfName, performative);

				// Surrounding Locations
				if (stenchConcentration == 0 && !isBlockade && !isTrap) {
					addSurroundingLocationsToBeInvestigated(col, row);
				}

				// Food Locations
				if (foodUnits > 0) {
					updateFoodLocation(mapLocations[col][row]);
				}

				return true;
			} else {
				log.info("Updated exsistng MapLocation {}", mapLocations[col][row].toShortString());
				mapLocations[col][row].updateLocation(col, row, smellConcentration, stenchConcentration, foodUnits,
						DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration, stenchConcentration));

				// Dwarf Position
				if (dwarfPositions.containsKey("dwarfName")) {
					dwarfPositions.remove("dwarfName");
				}

				// Food Locations
				if (foodUnits == 0) {
					removeFoodLocation(mapLocations[col][row]);
				}
				dwarfPositions.put(dwarfName, mapLocations[col][row]);
				log.info("Moved dwarf {} to new location {}", dwarfName, mapLocations[col][row].toString());
				return true;
			}
		}
		return false;
	}

	private void updateDwarfPosition(int col, int row, String dwarfName, int performative) {
		if (performative == ACLMessage.INFORM) {
			if (dwarfPositions.containsKey(dwarfName)) {
				dwarfPositions.remove(dwarfName);
				dwarfPositions.put(dwarfName, mapLocations[col][row]);
				log.info("Moved dwarf {} to MapLocation {}", dwarfName, mapLocations[col][row].toString());
			} else {
				dwarfPositions.put(dwarfName, mapLocations[col][row]);
				log.info("Added dwarf {} to MapLocation {}", dwarfName, mapLocations[col][row].toString());
			}
		} else {
			log.info("Position from Dwarf {} can not be updated");
		}
	}

	private void checkForResize(int col, int row) {
		int newColumns = 0;
		int newRows = 0;
		// "-2" => space for UnknownMapLocation
		if (col >= mapLocations.length - 2) {
			log.info("Start resizing...");
			newColumns = resizeColumns(col);
			log.info("Resized mapLocations and added {} new columns.", (newColumns * DwarfConstants.RESIZE_CHANGE_NUMBER));
		}
		// "-2" => space for UnknownMapLocation
		if (row >= mapLocations[0].length - 2) {
			log.info("Start resizing...");
			newRows = resizeRows(row);
			log.info("Resized mapLocations and added {} new rows.", newRows);
		}
	}

	private void updateFoodLocation(MapLocation mapLocation) {
		for (int i = 0; i < locationsWithFood.size(); ++i) {
			if (locationsWithFood.get(i).equals(mapLocation)) {
				if (locationsWithFood.get(i).getFoodUnits() == 0) {
					locationsWithFood.remove(i);
					log.info("{} removed from FoodLocations", mapLocation.toShortString());
				}
				return;
			}
		}
		mapLocation.setSave(true);
		locationsWithFood.add(mapLocation);
	}

	private void removeFoodLocation(MapLocation mapLocation) {
		for (int i = 0; i < locationsWithFood.size(); ++i) {
			if (locationsWithFood.get(i).equals(mapLocation)) {
				if (locationsWithFood.get(i).getFoodUnits() == 0) {
					locationsWithFood.remove(mapLocation);
					log.info("{} removed from FoodLocations", mapLocation.toShortString());
				}
				return;
			}
		}
	}

	public void addSurroundingLocationsToBeInvestigated(int col, int row) {
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
		if (!(col + 1 >= mapLocations.length - 1)) {
			if (mapLocations[col + 1][row] == null) {
				if (checkForLocationNotInInvestigationQueue(col + 1, row)) {
					mapLocations[col + 1][row] = new UnknownMapLocation(col + 1, row);
					locationsToBeInvestigated.add(new UnknownMapLocation(col + 1, row));
					log.info("Added new {} to investigation list", locationsToBeInvestigated.get(locationsToBeInvestigated.size() - 1));
				}
			}
		}
		if (!(row + 1 >= mapLocations[0].length - 1)) {
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
		// "-2" => space for UnknownMapLocation
		while (col >= ((countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER) + mapLocations.length - 2)) {
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
		// "-2" => space for UnknownMapLocation
		while (row >= ((countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER) + mapLocations[0].length - 2)) {
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

	public MapLocation getHomeLocation() {
		return homeLocation;
	}

	public void setHomeLocation(MapLocation homeLocation) {
		this.homeLocation = homeLocation;
	}
}
