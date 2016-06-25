package main.java.map;

import java.util.ArrayList;
import java.util.List;

public class MapLocation {

	private long col;
	private long row;
	private int smellConcentration;
	private int stenchConcentration;
	private boolean save;
	private int foodUnits;
	private boolean startField;
	private List<LocationStatus> locationStatus;
	private List<String> dwarfs;

	public enum LocationStatus {
		CLEAR, BLOCKADE, TRAP, FOOD, SMELL, STENCH
	}

	/**
	 * Constructor for UnknownMapLocation class. This will create a MapLoation without location information.
	 *
	 * @param col
	 *            the collumn of the UnknownMapLocation
	 * @param row
	 *            the row of the UnknownMapLocation
	 */
	public MapLocation(long col, long row) {
		setColumnCoordinate(col);
		setRowCoordinate(row);
	}

	public MapLocation(long col, long row, int smellConcentration, int stenchConcentration, int foodUnits, List<LocationStatus> locationStatus, String dwarfName) {
		setColumnCoordinate(col);
		setRowCoordinate(row);
		setSmellConcentration(smellConcentration);
		setStenchConcentration(stenchConcentration);
		setFoodUnits(foodUnits);
		setLocationStatus(locationStatus);
		dwarfs = new ArrayList<String>();
		addDwarfToLocation(dwarfName);
	}

	public void updateLocation(long col, long row, int smellConcentration, int stenchConcentration, int foodUnits, List<LocationStatus> locationStatus) {
		setColumnCoordinate(col);
		setRowCoordinate(row);
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

	public long getColumnCoordinate() {
		return col;
	}

	public int getIntColumnCoordinate() {
		return Math.toIntExact(col);
	}

	public void setColumnCoordinate(long col) {
		this.col = col;
	}

	public long getRowCoordinate() {
		return row;
	}

	public int getIntRowCoordinate() {
		return Math.toIntExact(row);
	}

	public void setRowCoordinate(long row) {
		this.row = row;
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

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
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

	public boolean isStartField() {
		return startField;
	}

	public void setStartField(boolean startField) {
		this.startField = startField;
	}

	@Override
	public String toString() {
		if (isStartField()) {
			return "MapLocation Startfield [" + col + "," + row + "] with smellConcentration = " + smellConcentration + ", stenchConcentration = " + stenchConcentration + ", foodUnits = " + foodUnits
					+ " and LocationStatus: " + locationStatus.toString();
		}
		return "MapLocation  [" + col + "," + row + "] with smellConcentration = " + smellConcentration + ", stenchConcentration = " + stenchConcentration + ", foodUnits = " + foodUnits
				+ " and LocationStatus: " + locationStatus.toString();
	}

	public String toShortString() {
		return "MapLocation  [" + col + "," + row + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MapLocation) {
			MapLocation location = (MapLocation) obj;
			if ((this.getIntColumnCoordinate() == location.getIntColumnCoordinate()) && (this.getIntRowCoordinate() == location.getIntRowCoordinate())) {
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}
}
