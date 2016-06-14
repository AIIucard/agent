package main.java.database;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import main.java.DwarfConstants;
import main.java.map.MapLocation;
import main.java.utils.DwarfUtils;

public class DwarfDatabase {
	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private HashMap<String, AgentController> agents;
	private AgentContainer agentContainer;
	private MapLocation[][] mapLocations;
	private int agentCounter;

	public DwarfDatabase() {
		agents = new HashMap<String, AgentController>();
		mapLocations = new MapLocation[15][15];
		agentCounter = 0;
	}

	public boolean updateExistingMapLocation(boolean isTrap, boolean isBlockade, int col, int row, int foodUnits,
			int smellConcentration, int stenchConcentration) {
		if (col > mapLocations.length || row > mapLocations[0].length) {
			log.error("Location [{},{}] is not an existing mapLocation", row, col);
			return false;
		}
		mapLocations[col][row].updateLocation(col, row, smellConcentration, stenchConcentration, foodUnits,
				DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration, stenchConcentration));
		return true;
	}

	public boolean updateMapLocation(boolean isStartfield, boolean isTrap, boolean isBlockade, int col, int row,
			int foodUnits, int smellConcentration, int stenchConcentration, String dwarfName) {
		if (col >= mapLocations.length) {
			log.info("Start resizing...");
			int newColumns = resizeColumns(col);
			log.info("Resized mapLocations and added {} new columns.",
					(newColumns * DwarfConstants.RESIZE_CHANGE_NUMBER));
		}
		if (row >= mapLocations[0].length) {
			log.info("Start resizing...");
			int newRows = resizeRows(row);
			log.info("Resized mapLocations and added {} new rows.", newRows);
		}
		if ((dwarfName != null) && !dwarfName.equals("")) {
			if (mapLocations[col][row] == null) {
				// Location Status
				mapLocations[col][row] = new MapLocation(col, row, smellConcentration, stenchConcentration, foodUnits,
						DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration,
								stenchConcentration),
						dwarfName);
				mapLocations[col][row].setStartField(isStartfield);
				log.info(
						"Added new mapLocation at [{},{}] with isTrap = {}, isBlockade = {}, smellConcentration = {}, stenchConcentration = {}, foodUnits = {}",
						row, col, isTrap, isBlockade, smellConcentration, stenchConcentration, foodUnits);
				return true;
			} else {
				mapLocations[col][row].updateLocation(col, row, smellConcentration, stenchConcentration, foodUnits,
						DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration,
								stenchConcentration));
				mapLocations[col][row].addDwarfToLocation(dwarfName);
				return true;
			}
		}
		return false;
	}

	private int resizeColumns(int col) {
		int countResizes = 0;
		while (col >= ((countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER) + mapLocations.length)) {
			++countResizes;
		}
		int oldColumnCount = mapLocations.length;
		int oldRowCount = mapLocations[0].length;
		MapLocation[][] newMapLocations = new MapLocation[oldColumnCount
				+ (countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER)][oldRowCount];
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
		MapLocation[][] newMapLocations = new MapLocation[oldColumnCount][oldRowCount
				+ (countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER)];
		for (int i = 0; i < oldColumnCount; i++) {
			for (int j = 0; j < oldRowCount; j++) {
				newMapLocations[i][j] = mapLocations[i][j];
			}
		}
		mapLocations = newMapLocations;
		return countResizes;
	}

	public HashMap<String, AgentController> getAgents() {
		return agents;
	}

	public void installAgent(String name, AgentController agent) {
		agents.put(name, agent);
	}

	public AgentContainer getAgentContainer() {
		return agentContainer;
	}

	public void setAgentContainer(AgentContainer agentContainer) {
		this.agentContainer = agentContainer;
	}

	public int getAgentCounter() {
		return agentCounter;
	}

	public void incrementAgentCounter() {
		agentCounter = agentCounter + 1;
	}

	public void decrementAgentCounter() {
		if (agentCounter > 0)
			agentCounter = agentCounter - 1;
	}

	public MapLocation[][] getMapLocations() {
		return mapLocations;
	}
}
