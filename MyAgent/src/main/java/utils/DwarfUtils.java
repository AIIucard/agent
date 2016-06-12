package main.java.utils;

import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.aim.antworld.agent.AntWorldConsts;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import main.java.DwarfConstants;

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
	public static ACLMessage createUpdateMapMessage(AID receiver, AID sender, JSONObject row, JSONObject col,
			JSONObject type, JSONObject food, JSONObject smell, JSONObject stench, JSONArray ants) {
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
		obj.put("ants", ants);
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
}
