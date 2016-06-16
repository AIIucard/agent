package main.java.map;

public class UnknownMapLocation extends MapLocation {

	public UnknownMapLocation(long col, long row) {
		super(col, row);
	}

	@Override
	public String toString() {
		return "UnknownLocation [" + getColumnCoordinate() + "][" + getRowCoordinate() + "]";
	}

}
