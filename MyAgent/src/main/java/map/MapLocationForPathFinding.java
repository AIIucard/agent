package main.java.map;

public class MapLocationForPathFinding {

	private MapLocation sourceMapLocation;
	private MapLocationForPathFinding parentMapLocation;
	private int estimatedDistanceToTarget;
	private int previousPathCosts;

	public MapLocationForPathFinding(MapLocation sourceMapLocation, MapLocationForPathFinding parentMapLocation) {
		this.sourceMapLocation = sourceMapLocation;
		this.parentMapLocation = parentMapLocation;
		estimatedDistanceToTarget = -1;
		previousPathCosts = -1;
	}

	public MapLocationForPathFinding(MapLocation sourceMapLocation, MapLocationForPathFinding parentMapLocation, int previousPathCosts) {
		this.sourceMapLocation = sourceMapLocation;
		this.parentMapLocation = parentMapLocation;
		estimatedDistanceToTarget = -1;
		this.previousPathCosts = previousPathCosts;
	}

	public MapLocation getSourceMapLocation() {
		return sourceMapLocation;
	}

	public void setSourceMapLocation(MapLocation sourceMapLocation) {
		this.sourceMapLocation = sourceMapLocation;
	}

	public MapLocationForPathFinding getParentMapLocation() {
		return parentMapLocation;
	}

	public void setParentMapLocation(MapLocationForPathFinding parentMapLocation) {
		this.parentMapLocation = parentMapLocation;
	}

	public int getEstimatedDistanceToTarget() {
		return estimatedDistanceToTarget;
	}

	public void setEstimatedDistanceToTarget(int estimatedDistanceToTarget) {
		this.estimatedDistanceToTarget = estimatedDistanceToTarget;
	}

	public int getPreviousPathCosts() {
		return previousPathCosts;
	}

	public void setPreviousPathCosts(int previousPathCosts) {
		this.previousPathCosts = previousPathCosts;
	}

	@Override
	public String toString() {
		return "MapLocation  [" + sourceMapLocation.getIntColumnCoordinate() + "," + sourceMapLocation.getIntRowCoordinate() + "] with parentMapLocation: " + parentMapLocation
				+ " and distance to target: " + estimatedDistanceToTarget;
	}

	public String toShortString() {
		return "MapLocation  [" + sourceMapLocation.getIntColumnCoordinate() + "," + sourceMapLocation.getIntRowCoordinate() + "]";
	}
}
