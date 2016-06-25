package main.java.map;

import java.util.ArrayList;

public class ClosedListMapLocation<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;

	public ClosedListMapLocation() {
		super();
	}

	@Override
	public boolean contains(Object obj) {
		if (obj instanceof MapLocation) {
			MapLocation location = (MapLocation) obj;
			MapLocation[] array = (MapLocation[]) this.toArray();
			for (MapLocation mapLocation : array) {
				if ((mapLocation.getIntColumnCoordinate() == location.getIntColumnCoordinate()) && (mapLocation.getIntRowCoordinate() == location.getIntRowCoordinate())) {
					return true;
				}
			}
			return false;
		}
		return super.contains(obj);
	}

}
