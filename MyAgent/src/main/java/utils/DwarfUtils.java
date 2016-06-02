package main.java.utils;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import de.aim.antworld.agent.AntWorldConsts;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class DwarfUtils {
	private static Logger log = Logger.getLogger(DwarfUtils.class);

	public static boolean containsString(String s, String subString) {
		return s.indexOf(subString) > -1 ? true : false;
	}

	public static ACLMessage createLoginMessage(AID receiver, AID sender) {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(receiver);
		msg.setSender(sender);

		try {
			JSONObject obj = new JSONObject();
			msg.setLanguage("JSON");
			obj.put("color", AntWorldConsts.ANT_COLOR_GREEN);
			obj.put("type", "ANT_ACTION_LOGIN");
			if (obj != null && receiver != null) {
				System.out.println("Login message: " + obj.toString() + "\n");
				msg.setContent(obj.toString());
				log.info("Test");
				return msg;
			}
		} catch (JSONException je) {
			log.info("JSON message can not be generated! " + je.getStackTrace().toString());
		}
		return null;
	}
}
