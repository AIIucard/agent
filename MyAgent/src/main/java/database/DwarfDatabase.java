package main.java.database;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.util.HashMap;

import main.java.DwarfConstants;
import main.java.map.MapLocation;
import main.java.utils.DwarfUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public boolean updateExistingMapLocation(boolean isTrap, boolean isBlockade, int row, int col, int foodUnits, int smellConcentration,
			int stenchConcentration) {
		if (row > mapLocations.length || col > mapLocations[0].length) {
			return false;
		}
		mapLocations[row][col].updateLocation(row, col, smellConcentration, stenchConcentration, foodUnits,
				DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration, stenchConcentration));
		return true;
	}

	public boolean updateMapLocation(boolean isTrap, boolean isBlockade, int row, int col, int foodUnits, int smellConcentration, int stenchConcentration,
			String dwarfName) {
		if (row > mapLocations.length) {
			log.info("Start resizing...");
			int newRows = resizeRows(row);
			log.info("Resized mapLocations and added {} new rows.", newRows);
		}
		if (col > mapLocations[0].length) {
			log.info("Start resizing...");
			int newColumns = resizeColumns(col);
			log.info("Resized mapLocations and added {} new columns.", newColumns);
		}
		if ((dwarfName != null) && !dwarfName.equals("")) {
			if (mapLocations[row][col] == null) {
				// Location Status
				mapLocations[row][col] = new MapLocation(row, col, smellConcentration, stenchConcentration, foodUnits, DwarfUtils.getLocationStatus(isTrap,
						isBlockade, foodUnits, smellConcentration, stenchConcentration), dwarfName);
				return true;
			} else {
				mapLocations[row][col].updateLocation(row, col, smellConcentration, stenchConcentration, foodUnits,
						DwarfUtils.getLocationStatus(isTrap, isBlockade, foodUnits, smellConcentration, stenchConcentration));
				mapLocations[row][col].addDwarfToLocation(dwarfName);
				return true;
			}
		}
		return false;
	}

	private int resizeRows(int row) {
		int countResizes = 1;
		while (row > ((countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER) + mapLocations.length)) {
			++countResizes;
		}
		int oldRowCount = mapLocations.length;
		int oldColumnCount = mapLocations[0].length;
		MapLocation[][] newMapLocations = new MapLocation[oldRowCount + (countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER)][oldColumnCount];
		for (int i = 0; i < oldRowCount; i++) {
			for (int j = 0; j < oldColumnCount; j++) {
				newMapLocations[i][j] = mapLocations[i][j];
			}
		}
		mapLocations = newMapLocations;
		return countResizes;
	}

	private int resizeColumns(int col) {
		int countResizes = 0;
		while (col > ((countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER) + mapLocations[0].length)) {
			++countResizes;
		}
		int oldRowCount = mapLocations.length;
		int oldColumnCount = mapLocations[0].length;
		MapLocation[][] newMapLocations = new MapLocation[oldRowCount][oldColumnCount + (countResizes * DwarfConstants.RESIZE_CHANGE_NUMBER)];
		for (int i = 0; i < oldRowCount; i++) {
			for (int j = 0; j < oldColumnCount; j++) {
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
