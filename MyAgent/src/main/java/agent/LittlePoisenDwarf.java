package main.java.agent;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.aim.antworld.agent.AntWorldConsts;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import main.java.utils.DwarfUtils;

public class LittlePoisenDwarf extends Agent implements InterfaceAgent {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private String name;
	// "%-5p [%-20C%d{dd MMM yyyy HH:mm:ss,SSS}]: %m%n"
	private AID antWorldGameLeaderAID;

	@Override
	protected void setup() {
		name = getLocalName();

		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {

				try {
					log.info("Searching for agents...");

					log.info("---Set filter: " + AntWorldConsts.SEVICE_NAME + " for search.");
					ServiceDescription filter = new ServiceDescription();
					filter.setType(AntWorldConsts.SEVICE_NAME);
					DFAgentDescription dfd = new DFAgentDescription();
					dfd.addServices(filter);

					log.info("---Search started...");
					DFAgentDescription[] results = DFService.search(myAgent, dfd);

					for (int i = 0; i < results.length; ++i) {
						log.info("---" + results[i].getName().getLocalName() + ":");
						if (DwarfUtils.containsString(results[i].getName().getLocalName(), "antWorld")) {
							antWorldGameLeaderAID = new AID(results[i].getName().getLocalName(), AID.ISLOCALNAME);
							log.info("---GameLeaderAgent found: " + antWorldGameLeaderAID.getLocalName());
							break;
						}
					}
					if (antWorldGameLeaderAID == null) {
						log.error("---No GameLeaderAgent found!");
					}
				} catch (Exception e) {
					log.error("GameLeaderAgent not found! " + e.getStackTrace().toString());
				}
				log.info("Searching for agents finished!");

				// create and config message
				if (antWorldGameLeaderAID != null) {
					ACLMessage msg = DwarfUtils.createLoginMessage(antWorldGameLeaderAID, getAID());
					if (msg != null) {
						send(msg);
					}
				}
			}
		});
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					log.info("Receive message:\n" + msg + "\n");
					if (msg.getLanguage().equals("JSON")) {
						try {
							JSONParser parser = new JSONParser();
							Object obj = parser.parse(msg.getContent());
							JSONObject jsonObject = (JSONObject) obj;
							if (msg.getSender() == antWorldGameLeaderAID) {
								if (jsonObject.has("replyId")) {

								}
							} else if (msg.getSender() == antWorldGameLeaderAID) {

							}

							// if(jsonObject) =
							// Set<String> keys = jsonObject.keyset();
							// jsonObject.get
							//
							// String name = (String) jsonObject.get("name");
							// System.out.println(name);
							//
							// long age = (Long) jsonObject.get("age");
							// System.out.println(age);
							//
							// // JSONArray array = (JSONArray) obj;
							// // for (int i = 0; i < array.length(); i++) {
							// // JSONObject jsonObj = array.getJSONObject(i);
							// // }
						} catch (ParseException pe) {
							log.error("Error while parsing message at position {} and Stacktrace {}", pe.getPosition(),
									pe.getStackTrace().toString());
						}
						// catch (JSONException je) {
						// log.error("Error while decoding message into
						// JSONObject with Stacktrace {}",
						// je.getStackTrace().toString());
						// }
					} else {
						log.error(
								"Message type unknown, because language key not set! Can not decode message into JSONObject!");
					}
				} else {
					block();
				}
			}
		});
	}

	@Override
	protected void takeDown() {
		// dwarfVisualCenter.getAgents().remove(name);
	};
}
