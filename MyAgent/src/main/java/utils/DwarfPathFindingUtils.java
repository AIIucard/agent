package main.java.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.DwarfConstants;
import main.java.map.ClosedListMapLocation;
import main.java.map.MapLocation;
import main.java.map.MapLocation.LocationStatus;
import main.java.map.MapLocationForPathFinding;
import main.java.map.PriorityQueueMapLocationForPathFinding;

public class DwarfPathFindingUtils {

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	public static Queue<MapLocation> checkForPathToLocation(MapLocation[][] mapLocations, MapLocation startLocation, MapLocation targetMapLocation) {
		log.info("--?--> Looking for path from {} to {}", startLocation.toShortString(), targetMapLocation.toShortString());
		PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding> openList = new PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding>(new DistanceComperator());
		ClosedListMapLocation<MapLocation> closedList = new ClosedListMapLocation<MapLocation>();
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
		// TODO Check if invert of pathQueue is needed
		Queue<MapLocation> pathQueue = new LinkedList<MapLocation>(pathStack);
		log.info("{}", logString);
		return pathQueue;
	}

	private static void addSurroundingLocationsToPriorityQueue(PriorityQueue<MapLocationForPathFinding> openList, List<MapLocation> closedList, MapLocation[][] mapLocations,
			MapLocationForPathFinding parentMapLocation, MapLocation goalLocation) {
		MapLocation sourceParentMapLocation = parentMapLocation.getSourceMapLocation();
		log.info("S----+----> Start to add surrounding map locations from location {}...", sourceParentMapLocation.toShortString());
		if (sourceParentMapLocation.getIntColumnCoordinate() - 1 >= 0) {
			if (mapLocations[sourceParentMapLocation.getIntColumnCoordinate() - 1][sourceParentMapLocation.getIntRowCoordinate()] != null) {
				MapLocation currentMapLocation = mapLocations[sourceParentMapLocation.getIntColumnCoordinate() - 1][sourceParentMapLocation.getIntRowCoordinate()];
				if (!closedList.contains(currentMapLocation)) {
					if (checkLocationStatus(currentMapLocation)) {
						MapLocationForPathFinding currentPathFindingMapLocation = new MapLocationForPathFinding(currentMapLocation, parentMapLocation);
						int pathCostSoFar = calculatePathCostsSoFar(parentMapLocation);
						// TODO Wieso Cast to PriorityQueueMapLocationForPathFinding?
						if (!(openList.contains(currentPathFindingMapLocation) && pathCostSoFar >= ((PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding>) openList)
								.getSpecificMapLocation(currentPathFindingMapLocation).getPreviousPathCosts())) {
							if (openList.contains(currentPathFindingMapLocation)) {
								currentPathFindingMapLocation = ((PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding>) openList).getSpecificMapLocation(currentPathFindingMapLocation);
								currentPathFindingMapLocation.setPreviousPathCosts(pathCostSoFar);
								currentPathFindingMapLocation.setParentMapLocation(parentMapLocation);
								log.info("----+----> Updated parent location and path costs from {} with new parent: {} and new costs: {}", currentPathFindingMapLocation,
										parentMapLocation.toShortString(), pathCostSoFar);
							} else {
								setDistancesToMapLocation(currentPathFindingMapLocation, mapLocations, parentMapLocation, goalLocation);
								openList.add(currentPathFindingMapLocation);
								log.info("----+----> Added {} to openList", currentPathFindingMapLocation);
							}
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
						int pathCostSoFar = calculatePathCostsSoFar(parentMapLocation);
						// TODO Wieso Cast to PriorityQueueMapLocationForPathFinding?
						if (!(openList.contains(currentPathFindingMapLocation) && pathCostSoFar >= ((PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding>) openList)
								.getSpecificMapLocation(currentPathFindingMapLocation).getPreviousPathCosts())) {
							if (openList.contains(currentPathFindingMapLocation)) {
								currentPathFindingMapLocation = ((PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding>) openList).getSpecificMapLocation(currentPathFindingMapLocation);
								currentPathFindingMapLocation.setPreviousPathCosts(pathCostSoFar);
								currentPathFindingMapLocation.setParentMapLocation(parentMapLocation);
								log.info("----+----> Updated parent location and path costs from {} with new parent: {} and new costs: {}", currentPathFindingMapLocation,
										parentMapLocation.toShortString(), pathCostSoFar);
							} else {
								setDistancesToMapLocation(currentPathFindingMapLocation, mapLocations, parentMapLocation, goalLocation);
								openList.add(currentPathFindingMapLocation);
								log.info("----+----> Added {} to openList", currentPathFindingMapLocation);
							}
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
						int pathCostSoFar = calculatePathCostsSoFar(parentMapLocation);
						// TODO Wieso Cast to PriorityQueueMapLocationForPathFinding?
						if (!(openList.contains(currentPathFindingMapLocation) && pathCostSoFar >= ((PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding>) openList)
								.getSpecificMapLocation(currentPathFindingMapLocation).getPreviousPathCosts())) {
							if (openList.contains(currentPathFindingMapLocation)) {
								currentPathFindingMapLocation = ((PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding>) openList).getSpecificMapLocation(currentPathFindingMapLocation);
								currentPathFindingMapLocation.setPreviousPathCosts(pathCostSoFar);
								currentPathFindingMapLocation.setParentMapLocation(parentMapLocation);
								log.info("----+----> Updated parent location and path costs from {} with new parent: {} and new costs: {}", currentPathFindingMapLocation,
										parentMapLocation.toShortString(), pathCostSoFar);
							} else {
								setDistancesToMapLocation(currentPathFindingMapLocation, mapLocations, parentMapLocation, goalLocation);
								openList.add(currentPathFindingMapLocation);
								log.info("----+----> Added {} to openList", currentPathFindingMapLocation);
							}
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
						int pathCostSoFar = calculatePathCostsSoFar(parentMapLocation);
						// TODO Wieso Cast to PriorityQueueMapLocationForPathFinding?
						if (!(openList.contains(currentPathFindingMapLocation) && pathCostSoFar >= ((PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding>) openList)
								.getSpecificMapLocation(currentPathFindingMapLocation).getPreviousPathCosts())) {
							if (openList.contains(currentPathFindingMapLocation)) {
								currentPathFindingMapLocation = ((PriorityQueueMapLocationForPathFinding<MapLocationForPathFinding>) openList).getSpecificMapLocation(currentPathFindingMapLocation);
								currentPathFindingMapLocation.setPreviousPathCosts(pathCostSoFar);
								currentPathFindingMapLocation.setParentMapLocation(parentMapLocation);
								log.info("----+----> Updated parent location and path costs from {} with new parent: {} and new costs: {}", currentPathFindingMapLocation,
										parentMapLocation.toShortString(), pathCostSoFar);
							} else {
								setDistancesToMapLocation(currentPathFindingMapLocation, mapLocations, parentMapLocation, goalLocation);
								openList.add(currentPathFindingMapLocation);
								log.info("----+----> Added {} to openList", currentPathFindingMapLocation);
							}
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
		for (int i = 0; i < locationStati.size(); i++) {
			if (locationStati.get(i).equals(LocationStatus.CLEAR)) {
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

	private static void setDistancesToMapLocation(MapLocationForPathFinding currendMapLocation, MapLocation[][] mapLocations, MapLocationForPathFinding parentMapLocation, MapLocation destination) {
		int pathCostsSoFar = calculatePathCostsSoFar(parentMapLocation);
		currendMapLocation.setPreviousPathCosts(pathCostsSoFar);
		currendMapLocation.setEstimatedDistanceToTarget(pathCostsSoFar + getManhattanDistance(mapLocations, currendMapLocation.getSourceMapLocation(), destination));
	}

	private static int calculatePathCostsSoFar(MapLocationForPathFinding parentMapLocation) {
		return parentMapLocation.getPreviousPathCosts() + DwarfConstants.MOVEMENT_COSTS;
	}

	private static int getManhattanDistance(MapLocation[][] mapLocations, MapLocation start, MapLocation destination) {
		return Math.abs(start.getIntColumnCoordinate() - destination.getIntColumnCoordinate()) + Math.abs(start.getIntRowCoordinate() - destination.getIntRowCoordinate());
	}
}
