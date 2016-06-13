package main.java.map;

import java.util.ArrayList;
import java.util.List;

public class MapLocation {

	private long row;
	private long col;
	private int smellConcentration;
	private int stenchConcentration;
	private int foodUnits;
	private List<LocationStatus> locationStatus;
	private List<String> dwarfs;

	public enum LocationStatus {
		CLEAR, BLOCKADE, TRAP, FOOD, SMELL, STENCH
	}

	public MapLocation(long row, long col, int smellConcentration, int stenchConcentration, int foodUnits, List<LocationStatus> locationStatus, String dwarfName) {
		setRowCoordinate(row);
		setColumnCoordinate(col);
		setSmellConcentration(smellConcentration);
		setStenchConcentration(stenchConcentration);
		setFoodUnits(foodUnits);
		setLocationStatus(locationStatus);
		dwarfs = new ArrayList<String>();
		addDwarfToLocation(dwarfName);
	}

	public void updateLocation(long row, long col, int smellConcentration, int stenchConcentration, int foodUnits, List<LocationStatus> locationStatus) {
		setRowCoordinate(row);
		setColumnCoordinate(col);
		setSmellConcentration(smellConcentration);
		setStenchConcentration(stenchConcentration);
		setFoodUnits(foodUnits);
		setLocationStatus(locationStatus);
	}

	public void addDwarfToLocation(String dwarfName) {
		dwarfs.add(dwarfName);
	}

	public boolean removeDwarfFromLocation(String dwarfName) {
		for (int i = 0; i < dwarfs.size(); ++i) {
			if (dwarfs.get(i).equals(dwarfName)) {
				dwarfs.remove(i);
				return true;
			}
		}
		return false;
	}

	public long getRowCoordinate() {
		return row;
	}

	public void setRowCoordinate(long row) {
		this.row = row;
	}

	public long getColumnCoordinate() {
		return col;
	}

	public void setColumnCoordinate(long col) {
		this.col = col;
	}

	public int getSmellConcentration() {
		return smellConcentration;
	}

	public void setSmellConcentration(int smellConcentration) {
		this.smellConcentration = smellConcentration;
	}

	public int getStenchConcentration() {
		return stenchConcentration;
	}

	public void setStenchConcentration(int stenchConcentration) {
		this.stenchConcentration = stenchConcentration;
	}

	public int getFoodUnits() {
		return foodUnits;
	}

	public void setFoodUnits(int foodUnits) {
		this.foodUnits = foodUnits;
	}

	public List<LocationStatus> getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(List<LocationStatus> locationStatus) {
		this.locationStatus = locationStatus;
	}

	public List<String> getDwarfList() {
		return dwarfs;
	}

	public void setDwarfList(List<String> dwarfs) {
		this.dwarfs = dwarfs;
	}
}
