package main.java.map;

public class MapLocation {

	private int row;
	private int col;
	private int smellConcentration;
	private int stenchConcentration;
	private int foodUnits;
	private LocationStatus locationStatus;

	public enum LocationStatus {
		EMPTY, BLOCKADE, TRAP, FOOD, SMELL, STENCH
	}

	public MapLocation(int row, int col, int smellConcentration, int stenchConcentration, int foodUnits,
			LocationStatus locationStatus) {
		setRowCoordinate(row);
		setColumnCoordinate(col);
		setSmellConcentration(smellConcentration);
		setStenchConcentration(stenchConcentration);
		setFoodUnits(foodUnits);
		setLocationStatus(locationStatus);
	}

	public void updateLocation(int row, int col, int smellConcentration, int stenchConcentration, int foodUnits,
			LocationStatus locationStatus) {
		setRowCoordinate(row);
		setColumnCoordinate(col);
		setSmellConcentration(smellConcentration);
		setStenchConcentration(stenchConcentration);
		setFoodUnits(foodUnits);
		setLocationStatus(locationStatus);
	}

	public int getRowCoordinate() {
		return row;
	}

	public void setRowCoordinate(int x) {
		this.row = x;
	}

	public int getColumnCoordinate() {
		return col;
	}

	public void setColumnCoordinate(int y) {
		this.col = y;
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

	public LocationStatus getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(LocationStatus locationStatus) {
		this.locationStatus = locationStatus;
	}
}
