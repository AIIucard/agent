package main.java.utils;

import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.aim.antworld.agent.AntWorldConsts;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class DwarfUtils {

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	public static boolean containsString(String s, String subString) {
		return s.indexOf(subString) > -1 ? true : false;
	}

	public static ACLMessage createLoginMessage(AID receiver, AID sender) {
		log.info("Creating login message...");
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		msg.setSender(sender);

		try {
			JSONObject obj = new JSONObject();
			msg.setLanguage("JSON");
			obj.put("color", AntWorldConsts.ANT_COLOR_GREEN);
			obj.put("type", "ANT_ACTION_LOGIN");
			if (obj != null && receiver != null) {
				log.info("Login message: {}", obj.toString());
				msg.setContent(obj.toString());
				return msg;
			}
		} catch (JSONException je) {
			log.info("JSON message can not be generated! " + je.getStackTrace().toString());
		}
		log.error("No login message created!");
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
