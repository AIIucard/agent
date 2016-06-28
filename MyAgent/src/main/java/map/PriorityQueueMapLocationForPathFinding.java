package main.java.map;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueMapLocationForPathFinding<E> extends PriorityQueue<E> {
	private static final long serialVersionUID = 1L;

	public PriorityQueueMapLocationForPathFinding(Comparator<E> distanceComperator) {
		super(distanceComperator);
	}

	public MapLocationForPathFinding getSpecificMapLocation(MapLocationForPathFinding location) {
		MapLocationForPathFinding[] array = (MapLocationForPathFinding[]) this.toArray();
		for (MapLocationForPathFinding mapLocationForPathFinding : array) {
			if ((mapLocationForPathFinding.getSourceMapLocation().getIntColumnCoordinate() == location
					.getSourceMapLocation().getIntColumnCoordinate())
					&& (mapLocationForPathFinding.getSourceMapLocation().getIntRowCoordinate() == location
							.getSourceMapLocation().getIntRowCoordinate())) {
				return mapLocationForPathFinding;
			}
		}
		return null;
	}
}
