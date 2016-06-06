package main.java.map;

public class MapLocation {

	private int myX;
	private int myY;
	private int smellConcentration;
	private int stenchConcentration;
	private int foodUnits;
	private LocationStatus locationStatus;

	public enum LocationStatus {
		EMPTY, BLOCKADE, TRAP, FOOD, SMELL, STENCH
	}

	public MapLocation(int x, int y, int smellConcentration, int stenchConcentration, int foodUnits,
			LocationStatus locationStatus) {
		setXCoordinate(x);
		setYCoordinate(y);
		setSmellConcentration(smellConcentration);
		setStenchConcentration(stenchConcentration);
		setFoodUnits(foodUnits);
		setLocationStatus(locationStatus);
	}

	public void updateLocation(int x, int y, int smellConcentration, int stenchConcentration, int foodUnits,
			LocationStatus locationStatus) {
		setXCoordinate(x);
		setYCoordinate(y);
		setSmellConcentration(smellConcentration);
		setStenchConcentration(stenchConcentration);
		setFoodUnits(foodUnits);
		setLocationStatus(locationStatus);
	}

	public int getXCoordinate() {
		return myX;
	}

	public void setXCoordinate(int x) {
		this.myX = x;
	}

	public int getYCoordinate() {
		return myY;
	}

	public void setYCoordinate(int y) {
		this.myY = y;
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
