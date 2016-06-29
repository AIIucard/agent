package main.java.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.DwarfConstants;
import main.java.agent.MovementOrder.Move;
import main.java.map.MapLocation;
import main.java.map.MapLocation.LocationStatus;
import main.java.map.MapLocationForPathFinding;
import main.java.map.UnknownMapLocation;

public class DwarfPathFindingUtils {

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	public static Queue<MapLocation> checkForPathToLocation(MapLocation[][] mapLocations, MapLocation startLocation, MapLocation targetMapLocation) {
		if (!startLocation.equals(targetMapLocation)) {
			log.info("--?--> Looking for path from {} to {}", startLocation.toShortString(), targetMapLocation.toShortString());
			PriorityQueue<MapLocationForPathFinding> openList = new PriorityQueue<MapLocationForPathFinding>(new DistanceComperator());
			List<MapLocation> closedList = new ArrayList<MapLocation>();
			openList.add(new MapLocationForPathFinding(startLocation, null, 0));

			while (!openList.isEmpty()) {
				MapLocationForPathFinding currentMapLocation = openList.remove();
				if (currentMapLocation.getSourceMapLocation().equals(targetMapLocation)) {
					log.info("-----> Path found from {} to {}", startLocation.toShortString(), targetMapLocation.toShortString());
					Queue<MapLocation> path = getPath(startLocation, currentMapLocation);
					return path;
				}
				closedList.add(currentMapLocation.getSourceMapLocation());
				addSurroundingLocationsToPriorityQueue(openList, closedList, mapLocations, currentMapLocation, targetMapLocation);
			}
			log.info("--x--> No path found from {} to {}", startLocation.toShortString(), targetMapLocation.toShortString());
		} else {
			log.info("--x--> StartLocation {} is also targetLocation {}", startLocation.toShortString(), targetMapLocation.toShortString());
		}
		return null;
	}

	private static Queue<MapLocation> getPath(MapLocation startLocation, MapLocationForPathFinding currentMapLocation) {
		String logString = "Path: ";
		Deque<MapLocation> pathStack = new ArrayDeque<MapLocation>();
		logString += "" + currentMapLocation.getSourceMapLocation().toShortString() + ", ";
		pathStack.push(currentMapLocation.getSourceMapLocation());
		MapLocationForPathFinding previousMapLocation = currentMapLocation.getParentMapLocation();
		while (!previousMapLocation.getSourceMapLocation().equals(startLocation)) {
			logString += "" + previousMapLocation.getSourceMapLocation().toShortString() + ", ";
			pathStack.push(previousMapLocation.getSourceMapLocation());
			previousMapLocation = previousMapLocation.getParentMapLocation();
		}
		pathStack.push(startLocation);
		// TODO Check if invert of pathQueue is needed
		Queue<MapLocation> pathQueue = new LinkedList<MapLocation>(pathStack);
		log.info("{}", logString);
		return pathQueue;
	}

	private static void addSurroundingLocationsToPriorityQueue(PriorityQueue<MapLocationForPathFinding> openList, List<MapLocation> closedList,
			MapLocation[][] mapLocations, MapLocationForPathFinding parentMapLocation, MapLocation targetLocation) {
		MapLocation sourceParentMapLocation = parentMapLocation.getSourceMapLocation();
		log.info("S----+----> Start to add surrounding map locations from location {}...", sourceParentMapLocation.toShortString());
		if (sourceParentMapLocation.getIntColumnCoordinate() - 1 >= 0) {
			if (mapLocations[sourceParentMapLocation.getIntColumnCoordinate() - 1][sourceParentMapLocation.getIntRowCoordinate()] != null) {
				MapLocation currentMapLocation = mapLocations[sourceParentMapLocation.getIntColumnCoordinate() - 1][sourceParentMapLocation.getIntRowCoordinate()];
				if (!closedList.contains(currentMapLocation)) {
					if (checkLocationStatus(currentMapLocation)) {
						MapLocationForPathFinding currentPathFindingMapLocation = new MapLocationForPathFinding(currentMapLocation, parentMapLocation);
						if ((currentPathFindingMapLocation.getSourceMapLocation() instanceof UnknownMapLocation
								&& currentPathFindingMapLocation.getSourceMapLocation().equals(targetLocation))
								|| !(currentPathFindingMapLocation.getSourceMapLocation() instanceof UnknownMapLocation)) {
							int pathCostSoFar = calculatePathCostsSoFar(parentMapLocation);
							if (!(openList.contains(currentPathFindingMapLocation)
									&& pathCostSoFar >= getSpecificMapLocationInQueue(currentPathFindingMapLocation, openList).getPreviousPathCosts())) {
								if (openList.contains(currentPathFindingMapLocation)) {
									currentPathFindingMapLocation = DwarfPathFindingUtils.getSpecificMapLocationInQueue(currentPathFindingMapLocation, openList);
									currentPathFindingMapLocation.setPreviousPathCosts(pathCostSoFar);
									currentPathFindingMapLocation.setParentMapLocation(parentMapLocation);
									log.info("----+----> Updated parent location and path costs from {} with new parent: {} and new costs: {}",
											currentPathFindingMapLocation, parentMapLocation.toShortString(), pathCostSoFar);
								} else {
									setDistancesToMapLocation(currentPathFindingMapLocation, mapLocations, parentMapLocation, targetLocation);
									openList.add(currentPathFindingMapLocation);
									log.info("----+----> Added {} to openList", currentPathFindingMapLocation);
								}
							}
						} else {
							log.info("Ignore MapLocation! {} is UnknownMapLocation and not targetLoaction",
									currentPathFindingMapLocation.getSourceMapLocation().toShortString());
						}
						log.info("OpenList contains already {} and previous path costs are lower", currentMapLocation.toShortString());
					}
				}
				log.info("---/-/---> CloseList contains already {}", currentMapLocation.toShortString());
			}
		}
		if (sourceParentMapLocation.getIntRowCoordinate() - 1 >= 0) {
			if (mapLocations[sourceParentMapLocation.getIntColumnCoordinate()][sourceParentMapLocation.getIntRowCoordinate() - 1] != null) {
				MapLocation currentMapLocation = mapLocations[sourceParentMapLocation.getIntColumnCoordinate()][sourceParentMapLocation.getIntRowCoordinate() - 1];
				if (!closedList.contains(currentMapLocation)) {
					if (checkLocationStatus(currentMapLocation)) {
						MapLocationForPathFinding currentPathFindingMapLocation = new MapLocationForPathFinding(currentMapLocation, parentMapLocation);
						if ((currentPathFindingMapLocation.getSourceMapLocation() instanceof UnknownMapLocation
								&& currentPathFindingMapLocation.getSourceMapLocation().equals(targetLocation))
								|| !(currentPathFindingMapLocation.getSourceMapLocation() instanceof UnknownMapLocation)) {
							int pathCostSoFar = calculatePathCostsSoFar(parentMapLocation);
							if (!(openList.contains(currentPathFindingMapLocation)
									&& pathCostSoFar >= getSpecificMapLocationInQueue(currentPathFindingMapLocation, openList).getPreviousPathCosts())) {
								if (openList.contains(currentPathFindingMapLocation)) {
									currentPathFindingMapLocation = getSpecificMapLocationInQueue(currentPathFindingMapLocation, openList);
									currentPathFindingMapLocation.setPreviousPathCosts(pathCostSoFar);
									currentPathFindingMapLocation.setParentMapLocation(parentMapLocation);
									log.info("----+----> Updated parent location and path costs from {} with new parent: {} and new costs: {}",
											currentPathFindingMapLocation, parentMapLocation.toShortString(), pathCostSoFar);
								} else {
									setDistancesToMapLocation(currentPathFindingMapLocation, mapLocations, parentMapLocation, targetLocation);
									openList.add(currentPathFindingMapLocation);
									log.info("----+----> Added {} to openList", currentPathFindingMapLocation);
								}
							}
						} else {
							log.info("Ignore MapLocation! {} is UnknownMapLocation and not targetLoaction",
									currentPathFindingMapLocation.getSourceMapLocation().toShortString());
						}
						log.info("OpenList contains already {} and previous path costs are lower", currentMapLocation.toShortString());
					}
				}
				log.info("---/-/---> OpenList contains already {}", currentMapLocation.toShortString());
			}
		}
		if (!(sourceParentMapLocation.getIntColumnCoordinate() + 1 >= mapLocations.length)) {
			if (mapLocations[sourceParentMapLocation.getIntColumnCoordinate() + 1][sourceParentMapLocation.getIntRowCoordinate()] != null) {
				MapLocation currentMapLocation = mapLocations[sourceParentMapLocation.getIntColumnCoordinate() + 1][sourceParentMapLocation.getIntRowCoordinate()];
				if (!closedList.contains(currentMapLocation)) {
					if (checkLocationStatus(currentMapLocation)) {
						MapLocationForPathFinding currentPathFindingMapLocation = new MapLocationForPathFinding(currentMapLocation, parentMapLocation);
						if ((currentPathFindingMapLocation.getSourceMapLocation() instanceof UnknownMapLocation
								&& currentPathFindingMapLocation.getSourceMapLocation().equals(targetLocation))
								|| !(currentPathFindingMapLocation.getSourceMapLocation() instanceof UnknownMapLocation)) {
							int pathCostSoFar = calculatePathCostsSoFar(parentMapLocation);
							if (!(openList.contains(currentPathFindingMapLocation)
									&& pathCostSoFar >= getSpecificMapLocationInQueue(currentPathFindingMapLocation, openList).getPreviousPathCosts())) {
								if (openList.contains(currentPathFindingMapLocation)) {
									currentPathFindingMapLocation = getSpecificMapLocationInQueue(currentPathFindingMapLocation, openList);
									currentPathFindingMapLocation.setPreviousPathCosts(pathCostSoFar);
									currentPathFindingMapLocation.setParentMapLocation(parentMapLocation);
									log.info("----+----> Updated parent location and path costs from {} with new parent: {} and new costs: {}",
											currentPathFindingMapLocation, parentMapLocation.toShortString(), pathCostSoFar);
								} else {
									setDistancesToMapLocation(currentPathFindingMapLocation, mapLocations, parentMapLocation, targetLocation);
									openList.add(currentPathFindingMapLocation);
									log.info("----+----> Added {} to openList", currentPathFindingMapLocation);
								}
							}
						} else {
							log.info("Ignore MapLocation! {} is UnknownMapLocation and not targetLoaction",
									currentPathFindingMapLocation.getSourceMapLocation().toShortString());
						}
						log.info("OpenList contains already {} and previous path costs are lower", currentMapLocation.toShortString());
					}
				}
				log.info("---/-/---> OpenList contains already {}", currentMapLocation.toShortString());
			}
		}
		if (!(sourceParentMapLocation.getIntRowCoordinate() + 1 >= mapLocations[0].length)) {
			if (mapLocations[sourceParentMapLocation.getIntColumnCoordinate()][sourceParentMapLocation.getIntRowCoordinate() + 1] != null) {
				MapLocation currentMapLocation = mapLocations[sourceParentMapLocation.getIntColumnCoordinate()][sourceParentMapLocation.getIntRowCoordinate() + 1];
				if (!closedList.contains(currentMapLocation)) {
					if (checkLocationStatus(currentMapLocation)) {
						MapLocationForPathFinding currentPathFindingMapLocation = new MapLocationForPathFinding(currentMapLocation, parentMapLocation);
						if ((currentPathFindingMapLocation.getSourceMapLocation() instanceof UnknownMapLocation
								&& currentPathFindingMapLocation.getSourceMapLocation().equals(targetLocation))
								|| !(currentPathFindingMapLocation.getSourceMapLocation() instanceof UnknownMapLocation)) {
							int pathCostSoFar = calculatePathCostsSoFar(parentMapLocation);
							if (!(openList.contains(currentPathFindingMapLocation)
									&& pathCostSoFar >= getSpecificMapLocationInQueue(currentPathFindingMapLocation, openList).getPreviousPathCosts())) {
								if (openList.contains(currentPathFindingMapLocation)) {
									currentPathFindingMapLocation = getSpecificMapLocationInQueue(currentPathFindingMapLocation, openList);
									currentPathFindingMapLocation.setPreviousPathCosts(pathCostSoFar);
									currentPathFindingMapLocation.setParentMapLocation(parentMapLocation);
									log.info("----+----> Updated parent location and path costs from {} with new parent: {} and new costs: {}",
											currentPathFindingMapLocation, parentMapLocation.toShortString(), pathCostSoFar);
								} else {
									setDistancesToMapLocation(currentPathFindingMapLocation, mapLocations, parentMapLocation, targetLocation);
									openList.add(currentPathFindingMapLocation);
									log.info("----+----> Added {} to openList", currentPathFindingMapLocation);
								}
							}
						} else {
							log.info("Ignore MapLocation! {} is UnknownMapLocation and not targetLoaction",
									currentPathFindingMapLocation.getSourceMapLocation().toShortString());
						}
						log.info("OpenList contains already {} and previous path costs are lower", currentMapLocation.toShortString());
					}
				}
				log.info("---/-/---> OpenList contains already {}", currentMapLocation.toShortString());
			}
		}
		log.info("F----+----> Finished adding surrounding map locations from location {}.", sourceParentMapLocation.toShortString());
	}

	private static boolean checkLocationStatus(MapLocation mapLocation) {
		List<LocationStatus> locationStati = mapLocation.getLocationStatus();
		if (locationStati == null && mapLocation instanceof UnknownMapLocation) {
			return true;
		}
		for (int i = 0; i < locationStati.size(); i++) {
			if (locationStati.get(i).equals(LocationStatus.FREE)) {
				log.info("--|LS|--> LocationStatus is save from mapLocation: {}", mapLocation.toShortString());
				return true;
			}
			if (locationStati.get(i).equals(LocationStatus.OBSTACLE) || locationStati.get(i).equals(LocationStatus.PIT)) {
				log.info("--|LNS|--> LocationStatus is unsave from mapLocation: {}", mapLocation.toShortString());
				return false;
			}
			if (locationStati.get(i).equals(LocationStatus.STENCH)) {
				if (!mapLocation.isSave()) {
					log.info("--|LNS|--> LocationStatus is unsave from mapLocation: {}", mapLocation.toShortString());
					return false;
				}
			}
		}
		log.info("--|LS|--> LocationStatus is save from mapLocation: {}", mapLocation.toShortString());
		return true;
	}

	private static void setDistancesToMapLocation(MapLocationForPathFinding currendMapLocation, MapLocation[][] mapLocations, MapLocationForPathFinding parentMapLocation,
			MapLocation destination) {
		int pathCostsSoFar = calculatePathCostsSoFar(parentMapLocation);
		currendMapLocation.setPreviousPathCosts(pathCostsSoFar);
		currendMapLocation.setEstimatedDistanceToTarget(pathCostsSoFar + getManhattanDistance(mapLocations, currendMapLocation.getSourceMapLocation(), destination));
	}

	private static int calculatePathCostsSoFar(MapLocationForPathFinding parentMapLocation) {
		return parentMapLocation.getPreviousPathCosts() + DwarfConstants.MOVEMENT_COSTS;
	}

	private static int getManhattanDistance(MapLocation[][] mapLocations, MapLocation start, MapLocation destination) {
		return Math.abs(start.getIntColumnCoordinate() - destination.getIntColumnCoordinate())
				+ Math.abs(start.getIntRowCoordinate() - destination.getIntRowCoordinate());
	}

	public static Queue<Move> convertPathToActions(Queue<MapLocation> path, boolean collectAction, boolean dropAction) {
		MapLocation[] arrayFormat = new MapLocation[path.size()];
		MapLocation[] pathTemp = path.toArray(arrayFormat);
		MapLocation nextLocation;
		Queue<Move> moveActionQueue = new LinkedList<Move>();
		for (int i = 0; i < pathTemp.length - 1; i++) {
			nextLocation = pathTemp[i + 1];
			if (((pathTemp[i].getIntColumnCoordinate() + 1) == nextLocation.getIntColumnCoordinate())
					&& (pathTemp[i].getIntRowCoordinate() == nextLocation.getIntRowCoordinate())) {
				moveActionQueue.add(Move.RIGHT);
			} else if (((pathTemp[i].getIntColumnCoordinate() - 1) == nextLocation.getIntColumnCoordinate())
					&& (pathTemp[i].getIntRowCoordinate() == nextLocation.getIntRowCoordinate())) {
				moveActionQueue.add(Move.LEFT);
			} else if ((pathTemp[i].getIntColumnCoordinate() == nextLocation.getIntColumnCoordinate())
					&& ((pathTemp[i].getIntRowCoordinate() - 1) == nextLocation.getIntRowCoordinate())) {
				moveActionQueue.add(Move.UP);
			} else if ((pathTemp[i].getIntColumnCoordinate() == nextLocation.getIntColumnCoordinate())
					&& ((pathTemp[i].getIntRowCoordinate() + 1) == nextLocation.getIntRowCoordinate())) {
				moveActionQueue.add(Move.DOWN);
			} else {
				log.error("Canot add move action to Queue! Locations are not Connected...");
				return null;
			}
		}
		if (collectAction) {
			moveActionQueue.add(Move.COLLECT);
		} else if (dropAction) {
			moveActionQueue.add(Move.DROP);
		}
		return moveActionQueue;
	}

	public static MapLocationForPathFinding getSpecificMapLocationInQueue(MapLocationForPathFinding location, Queue<MapLocationForPathFinding> queue) {
		List<MapLocationForPathFinding> arrayList = new ArrayList<MapLocationForPathFinding>(queue);
		for (MapLocationForPathFinding mapLocationForPathFinding : arrayList) {
			if ((mapLocationForPathFinding.getSourceMapLocation().getIntColumnCoordinate() == location.getSourceMapLocation().getIntColumnCoordinate())
					&& (mapLocationForPathFinding.getSourceMapLocation().getIntRowCoordinate() == location.getSourceMapLocation().getIntRowCoordinate())) {
				return mapLocationForPathFinding;
			}
		}
		return null;
	}
}
