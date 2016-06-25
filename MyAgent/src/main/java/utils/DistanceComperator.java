package main.java.utils;

import java.util.Comparator;

import main.java.map.MapLocationForPathFinding;

public class DistanceComperator implements Comparator<MapLocationForPathFinding> {

	@Override
	public int compare(MapLocationForPathFinding o1, MapLocationForPathFinding o2) {
		if (o1.getEstimatedDistanceToTarget() < o2.getEstimatedDistanceToTarget()) {
			return -1;
		}
		if (o1.getEstimatedDistanceToTarget() > o2.getEstimatedDistanceToTarget()) {
			return 1;
		}
		return 0;
	}
}
