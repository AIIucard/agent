package main.java.map;

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

	public enum LocationStatus {
		FREE, OBSTACLE, PIT, FOOD, SMELL, STENCH
	}

	/**
	 * Constructor for UnknownMapLocation class. This will create a MapLoation
	 * without location information.
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

	public MapLocation(long col, long row, int smellConcentration, int stenchConcentration, int foodUnits,
			List<LocationStatus> locationStatus) {
		setColumnCoordinate(col);
		setRowCoordinate(row);
		setSmellConcentration(smellConcentration);
		setStenchConcentration(stenchConcentration);
		setFoodUnits(foodUnits);
		setLocationStatus(locationStatus);
	}

	public void updateLocation(long col, long row, int smellConcentration, int stenchConcentration, int foodUnits,
			List<LocationStatus> locationStatus) {
		setColumnCoordinate(col);
		setRowCoordinate(row);
		setSmellConcentration(smellConcentration);
		setStenchConcentration(stenchConcentration);
		setFoodUnits(foodUnits);
		setLocationStatus(locationStatus);
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

	public boolean isStartField() {
		return startField;
	}

	public void setStartField(boolean startField) {
		this.startField = startField;
	}

	@Override
	public String toString() {
		if (isStartField()) {
			return "MapLocation Startfield [" + col + "," + row + "] with smellConcentration = " + smellConcentration
					+ ", stenchConcentration = " + stenchConcentration + ", foodUnits = " + foodUnits
					+ " and LocationStatus: " + locationStatus.toString();
		}
		return "MapLocation  [" + col + "," + row + "] with smellConcentration = " + smellConcentration
				+ ", stenchConcentration = " + stenchConcentration + ", foodUnits = " + foodUnits
				+ " and LocationStatus: " + locationStatus.toString();
	}

	public String toShortString() {
		return "MapLocation  [" + col + "," + row + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj != null) && (obj instanceof MapLocation)) {
			MapLocation location = (MapLocation) obj;
			return location.getIntColumnCoordinate() == this.getIntColumnCoordinate()
					&& location.getIntRowCoordinate() == this.getIntRowCoordinate();
		}
		return this.equals(obj);
	}
}
