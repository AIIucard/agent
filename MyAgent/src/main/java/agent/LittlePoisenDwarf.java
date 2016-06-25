package main.java.agent;

import java.util.Queue;

import org.json.simple.JSONObject;
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
import main.java.DwarfConstants;
import main.java.utils.DwarfMessagingUtils;
import main.java.utils.DwarfUtils;

public class LittlePoisenDwarf extends Agent implements InterfaceAgent {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private String name;
	private AID antWorldGameLeaderAID;

	private MovementOrder activeMovementOrder;
	private Queue<MovementOrder> finishedMovementOrders;

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
					ACLMessage loginMessage = DwarfMessagingUtils.createLoginMessage(antWorldGameLeaderAID, getAID());
					if (loginMessage != null) {
						send(loginMessage);
					}
				}
			}
		});
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage receivedMessage = receive();
				if (receivedMessage != null) {
					log.info(" Agent {} received message: {}", name, receivedMessage);
					if (receivedMessage.getLanguage().equals("JSON")) {
						try {
							JSONParser parser = new JSONParser();
							Object obj = parser.parse(receivedMessage.getContent());
							JSONObject jsonObject = (JSONObject) obj;
							if (receivedMessage.getInReplyTo() == null) {
								// TODO check if collect and drop are possible
								if (jsonObject.containsKey("cell")) {
									JSONObject structure = (JSONObject) jsonObject.get("cell");
									if (structure.containsKey("row") && structure.containsKey("col")) {
										ACLMessage updateMapMessage = DwarfMessagingUtils.createUpdateMapMessage(getAID(DwarfConstants.GUI_AGENT_NAME), getAID(), structure.get("row"),
												structure.get("col"), structure.get("type"), structure.get("food"), structure.get("smell"), structure.get("stench"), name);
										if (updateMapMessage != null) {
											send(updateMapMessage);
										}
										// TODO Update activeMovementOrder (isFinished = True if col and row fit)
									} else {
										log.error("Perception message is incomplete: {}", receivedMessage);
									}
								} else {
									log.error("No perception message was received: {}", receivedMessage);
								}
							} else if (receivedMessage.getInReplyTo().equals(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY)) {
								if (jsonObject.containsKey("moves")) {
									JSONObject structure = (JSONObject) jsonObject.get("moves");
									// TODO convert msg into movementorder

									// if (structure.containsKey("row") && structure.containsKey("col")) {
									// ACLMessage updateMapMessage =
									// DwarfMessagingUtils.createUpdateMapMessage(getAID(DwarfConstants.GUI_AGENT_NAME),
									// getAID(), structure.get("row"),
									// structure.get("col"), structure.get("type"), structure.get("food"),
									// structure.get("smell"), structure.get("stench"), name);
									// if (updateMapMessage != null) {
									// send(updateMapMessage);
									// }
									// } else {
									// log.error("Perception message is incomplete: {}", receivedMessage);
									// }

								} else {
									log.error("No movementorder message was received: {}", receivedMessage);
								}
							}
							if (activeMovementOrder == null) {
								ACLMessage requestMovementOrderMessage = DwarfMessagingUtils.createRequestMovementOrderMessage(getAID(DwarfConstants.GUI_AGENT_NAME), getAID());
								if (requestMovementOrderMessage != null) {
									send(requestMovementOrderMessage);
								}
							} else if (activeMovementOrder != null) {
								String currentMovement = activeMovementOrder.getMoves().remove();
								switch (currentMovement) {
								case "UP":
									// TODO UP msg to gameLeader
									break;
								case "DOWN":
									// TODO DOWN msg to gameLeader
									break;
								case "LEFT":
									// TODO LEFT msg to gameLeader
									break;
								case "RIGHT":
									// TODO RIGHT msg to gameLeader
									break;
								case "COLLECT":
									// TODO COLLECT msg to gameLeader
									// Check if collect is possible
									break;
								case "DROP":
									// TODO DROP msg to gameLeader
									// check if drop is possible
									break;
								}
							}
						} catch (ParseException pe) {
							log.error("Error while parsing message at position {} and Stacktrace {}", pe.getPosition(), pe.getStackTrace().toString());
						}
					} else {
						log.error("Message type unknown, because language key not set! Can not decode message into JSONObject!");
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
