package main.java.utils;

import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.aim.antworld.agent.AntWorldConsts;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import main.java.DwarfConstants;
import main.java.agent.MovementOrder.Move;

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
			msg.setContent(jsonObject.toString());
			log.info("Login message created: {}", msg);
			return msg;
		}
		log.error("No login message created!");
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createUpdateMapMessage(AID receiver, AID sender, Object row, Object col, Object type, Object food, Object smell, Object stench,
			String dwarfName, int peformative, boolean isObstacle) {
		log.info("Creating {} message...", DwarfConstants.UPDATE_MAP_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(receiver);
		msg.setInReplyTo(DwarfConstants.UPDATE_MAP_MESSAGE_REPLY);
		msg.setSender(sender);

		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		jsonObject.put("row", row);
		jsonObject.put("col", col);
		jsonObject.put("type", type);
		jsonObject.put("food", food);
		jsonObject.put("smell", smell);
		jsonObject.put("stench", stench);
		jsonObject.put("dwarfName", dwarfName);
		jsonObject.put("performative", peformative);
		jsonObject.put("isObstacle", isObstacle);
		if (jsonObject != null && receiver != null) {
			msg.setContent(jsonObject.toString());
			log.info("{} message created: {}", DwarfConstants.UPDATE_MAP_MESSAGE_REPLY, msg);
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.UPDATE_MAP_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createRequestMovementOrderMessage(AID receiver, AID sender, String dwarfName, boolean collectedFood) {
		log.info("Creating {} message...", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		msg.setInReplyTo(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		msg.setSender(sender);
		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		jsonObject.put("dwarfName", dwarfName);
		jsonObject.put("collectedFood", collectedFood);
		if (jsonObject != null && receiver != null) {
			msg.setContent(jsonObject.toString());
			log.info("{} message created: {}", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY, msg);
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createMovementOrderMessage(AID receiver, AID sender, Queue<Move> moves) {
		log.info("Creating {} message...", DwarfConstants.MOVEMENTORDER_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(receiver);
		msg.setInReplyTo(DwarfConstants.MOVEMENTORDER_MESSAGE_REPLY);
		msg.setSender(sender);
		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		JSONArray moveArray = new JSONArray();
		for (Move move : moves) {
			moveArray.add(move.name());
		}
		jsonObject.put("moves", moveArray);
		if (jsonObject != null && receiver != null) {
			msg.setContent(jsonObject.toString());
			log.info("{} message created: {}", DwarfConstants.MOVEMENTORDER_MESSAGE_REPLY, msg);
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.MOVEMENTORDER_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createMoveUpMessage(AID receiver, AID sender, String antWorldGameLeaderReply) {
		log.info("Creating {} message...", DwarfConstants.MOVE_UP_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		// msg.setInReplyTo(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		msg.setSender(sender);
		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		jsonObject.put("type", AntWorldConsts.ANT_ACTION_UP);
		jsonObject.put("color", AntWorldConsts.ANT_COLOR_GREEN);
		if (jsonObject != null && receiver != null) {
			msg.setContent(jsonObject.toString());
			msg.setInReplyTo(antWorldGameLeaderReply);
			log.info("{} message created: {}", DwarfConstants.MOVE_UP_MESSAGE_REPLY, msg);
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.MOVE_UP_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createMoveDownMessage(AID receiver, AID sender, String antWorldGameLeaderReply) {
		log.info("Creating {} message...", DwarfConstants.MOVE_DOWN_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		// msg.setInReplyTo(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		msg.setSender(sender);
		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		jsonObject.put("type", AntWorldConsts.ANT_ACTION_DOWN);
		jsonObject.put("color", AntWorldConsts.ANT_COLOR_GREEN);
		if (jsonObject != null && receiver != null) {
			msg.setContent(jsonObject.toString());
			msg.setInReplyTo(antWorldGameLeaderReply);
			log.info("{} message created: {}", DwarfConstants.MOVE_DOWN_MESSAGE_REPLY, msg);
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.MOVE_DOWN_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createMoveLeftMessage(AID receiver, AID sender, String antWorldGameLeaderReply) {
		log.info("Creating {} message...", DwarfConstants.MOVE_LEFT_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		// msg.setInReplyTo(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		msg.setSender(sender);
		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		jsonObject.put("type", AntWorldConsts.ANT_ACTION_LEFT);
		jsonObject.put("color", AntWorldConsts.ANT_COLOR_GREEN);
		if (jsonObject != null && receiver != null) {
			msg.setContent(jsonObject.toString());
			msg.setInReplyTo(antWorldGameLeaderReply);
			log.info("{} message created: {}", DwarfConstants.MOVE_LEFT_MESSAGE_REPLY, msg);
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.MOVE_LEFT_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createMoveRightMessage(AID receiver, AID sender, String antWorldGameLeaderReply) {
		log.info("Creating {} message...", DwarfConstants.MOVE_RIGHT_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		// msg.setInReplyTo(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		msg.setSender(sender);
		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		jsonObject.put("type", AntWorldConsts.ANT_ACTION_RIGHT);
		jsonObject.put("color", AntWorldConsts.ANT_COLOR_GREEN);
		if (jsonObject != null && receiver != null) {
			msg.setContent(jsonObject.toString());
			msg.setInReplyTo(antWorldGameLeaderReply);
			log.info("{} message: {}", DwarfConstants.MOVE_RIGHT_MESSAGE_REPLY, msg);
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.MOVE_RIGHT_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createMoveCollectMessage(AID receiver, AID sender, String antWorldGameLeaderReply) {
		log.info("Creating {} message...", DwarfConstants.MOVE_COLLECT_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		// msg.setInReplyTo(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		msg.setSender(sender);
		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		jsonObject.put("type", AntWorldConsts.ANT_ACTION_COLLECT);
		jsonObject.put("color", AntWorldConsts.ANT_COLOR_GREEN);
		if (jsonObject != null && receiver != null) {
			msg.setContent(jsonObject.toString());
			msg.setInReplyTo(antWorldGameLeaderReply);
			log.info("{} message created: {}", DwarfConstants.MOVE_COLLECT_MESSAGE_REPLY, msg);
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.MOVE_COLLECT_MESSAGE_REPLY);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ACLMessage createMoveDropMessage(AID receiver, AID sender, String antWorldGameLeaderReply) {
		log.info("Creating {} message...", DwarfConstants.MOVE_DROP_MESSAGE_REPLY);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		// msg.setInReplyTo(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY);
		msg.setSender(sender);
		JSONObject jsonObject = new JSONObject();
		msg.setLanguage("JSON");
		jsonObject.put("type", AntWorldConsts.ANT_ACTION_DROP);
		jsonObject.put("color", AntWorldConsts.ANT_COLOR_GREEN);
		if (jsonObject != null && receiver != null) {
			msg.setContent(jsonObject.toString());
			msg.setInReplyTo(antWorldGameLeaderReply);
			log.info("{} message created: {}", DwarfConstants.MOVE_DROP_MESSAGE_REPLY, msg);
			return msg;
		}
		log.error("No {} message created!", DwarfConstants.MOVE_DROP_MESSAGE_REPLY);
		return null;
	}
}
