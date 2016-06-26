package main.java.utils;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.aim.antworld.agent.AntWorldConsts;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import main.java.DwarfConstants;

public class DwarfMessagingUtils {
	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

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
	public static ACLMessage createUpdateMapMessage(AID receiver, AID sender, Object row, Object col, Object type, Object food, Object smell, Object stench, String dwarfName) {
		log.info("Creating {} message...", DwarfConstants.UPDATE_MAP_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(receiver);
		msg.setInReplyTo(DwarfConstants.UPDATE_MAP_MESSAGE_REPLY);
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
			log.info("{} message: {}", DwarfConstants.UPDATE_MAP_MESSAGE_REPLY, obj.toString());
			msg.setContent(obj.toString());
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.UPDATE_MAP_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createRequestMovementOrderMessage(AID receiver, AID sender, String dwarfName) {
		log.info("Creating {} message...", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(receiver);
		msg.setInReplyTo(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		msg.setSender(sender);
		JSONObject obj = new JSONObject();
		msg.setLanguage("JSON");
		obj.put("dwarfName", dwarfName);
		if (obj != null && receiver != null) {
			log.info("{} message: {}", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY, obj.toString());
			msg.setContent(obj.toString());
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createMovementOrderMessage(AID receiver, AID sender, String dwarfName) {
		// log.info("Creating {} message...", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		// ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		// msg.addReceiver(receiver);
		// msg.setInReplyTo(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		// msg.setSender(sender);
		// JSONObject obj = new JSONObject();
		// msg.setLanguage("JSON");
		// obj.put("dwarfName", dwarfName);
		// if (obj != null && receiver != null) {
		// log.info("{} message: {}", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY, obj.toString());
		// msg.setContent(obj.toString());
		// return msg;
		// }
		// log.error("No {} message created!", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		return null;
	}
}
