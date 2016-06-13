package main.java.utils;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import main.java.DwarfConstants;
import main.java.map.MapLocation;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.aim.antworld.agent.AntWorldConsts;

public class DwarfUtils {

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	public static boolean containsString(String s, String subString) {
		return s.indexOf(subString) > -1 ? true : false;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createLoginMessage(AID receiver, AID sender) {
		log.info("Creating login message...");
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		msg.setSender(sender);

		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		jsonObject.put("color", AntWorldConsts.ANT_COLOR_GREEN);
		jsonObject.put("type", "ANT_ACTION_LOGIN");
		if (jsonObject != null && receiver != null) {
			log.info("Login message: {}", jsonObject.toString());
			msg.setContent(jsonObject.toString());
			return msg;
		}
		log.error("No login message created!");
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createUpdateMapMessage(AID receiver, AID sender, Object row, Object col, Object type, Object food, Object smell, Object stench,
			String dwarfName) {
		log.info("Creating {} message...", DwarfConstants.UPDATE_MAP_MESSAGE_SUBJECT);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(receiver);
		msg.setInReplyTo(DwarfConstants.UPDATE_MAP_MESSAGE_SUBJECT);
		msg.setSender(sender);

		JSONObject obj = new JSONObject();
		msg.setLanguage("JSON");
		obj.put("row", row);
		obj.put("col", col);
		obj.put("type", type);
		obj.put("food", food);
		obj.put("smell", smell);
		obj.put("stench", stench);
		obj.put("dwarfName", dwarfName);
		if (obj != null && receiver != null) {
			log.info("{} message: {}", DwarfConstants.UPDATE_MAP_MESSAGE_SUBJECT, obj.toString());
			msg.setContent(obj.toString());
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.UPDATE_MAP_MESSAGE_SUBJECT);
		return null;
	}

	public static DefaultTableModel toTableModel(Map<?, ?> map) {
		DefaultTableModel model = new DefaultTableModel(new Object[] { "Key", "Value" }, 0);
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			model.addRow(new Object[] { entry.getKey(), entry.getValue() });
		}
		return model;
	}

	public static List<MapLocation.LocationStatus> getLocationStatus(boolean isTrap, boolean isBlockade, int foodUnits, int smellConcentration,
			int stenchConcentration) {
		List<MapLocation.LocationStatus> list = new ArrayList<MapLocation.LocationStatus>();
		if (isTrap) {
			list.add(MapLocation.LocationStatus.TRAP);
		}
		if (isBlockade) {
			list.add(MapLocation.LocationStatus.BLOCKADE);
		}
		if (foodUnits != 0) {
			list.add(MapLocation.LocationStatus.FOOD);
		}
		if (smellConcentration != 0) {
			list.add(MapLocation.LocationStatus.SMELL);
		}
		if (stenchConcentration != 0) {
			list.add(MapLocation.LocationStatus.STENCH);
		}
		return list;
	}
}
