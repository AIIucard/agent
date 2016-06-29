package main.java.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import main.java.map.MapLocation;
import main.java.map.MapLocation.LocationStatus;

public class DwarfUtils {

	public static boolean containsString(String s, String subString) {
		return s.indexOf(subString) > -1 ? true : false;
	}

	public static DefaultTableModel toTableModel(Map<?, ?> map) {
		DefaultTableModel model = new DefaultTableModel(new Object[] { "Key", "Value" }, 0);
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			model.addRow(new Object[] { entry.getKey(), entry.getValue() });
		}
		return model;
	}

	public static List<MapLocation.LocationStatus> getLocationStatus(boolean isTrap, boolean isBlockade, int foodUnits, int smellConcentration, int stenchConcentration) {
		List<MapLocation.LocationStatus> list = new ArrayList<MapLocation.LocationStatus>();
		if (isTrap) {
			list.add(MapLocation.LocationStatus.PIT);
		}
		if (isBlockade) {
			list.add(MapLocation.LocationStatus.OBSTACLE);
		}
		if (foodUnits > 0) {
			list.add(MapLocation.LocationStatus.FOOD);
			if (smellConcentration > foodUnits) {
				list.add(MapLocation.LocationStatus.SMELL);
			}
		} else {
			if (smellConcentration > 0) {
				list.add(MapLocation.LocationStatus.SMELL);
			}
		}
		if (stenchConcentration != 0) {
			list.add(MapLocation.LocationStatus.STENCH);
		}
		if (!isTrap && !isBlockade) {
			list.add(LocationStatus.FREE);
		}
		return list;
	}

	public static int castJSONObjectLongToInt(Object obj) {
		int value = -1;
		if (obj instanceof Long) {
			value = Math.toIntExact((Long) obj);
		} else if (obj instanceof Integer) {
			value = (Integer) obj;
		}
		return value;
	}
}
