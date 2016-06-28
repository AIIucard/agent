package main.java.agent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONArray;
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
import main.java.agent.MovementOrder.Move;
import main.java.utils.DwarfMessagingUtils;
import main.java.utils.DwarfUtils;

public class LittlePoisenDwarf extends Agent {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private String name;
	private AID antWorldGameLeaderAID;

	private MovementOrder activeMovementOrder;
	private boolean collectedFood;

	private String antWorldGameLeaderReply;

	@Override
	protected void setup() {
		name = getLocalName();
		antWorldGameLeaderReply = "";

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
					log.error("GameLeaderAgent not found! ", e);
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
							if ((receivedMessage
									.getInReplyTo() != DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY)) {
								if (receivedMessage.getReplyWith() != null) {
									antWorldGameLeaderReply = receivedMessage.getReplyWith();
								}
								// TODO check if collect and drop are possible
								if (jsonObject.containsKey("cell")) {
									JSONObject structure = (JSONObject) jsonObject.get("cell");
									if (structure.containsKey("row") && structure.containsKey("col")) {
										ACLMessage updateMapMessage = DwarfMessagingUtils.createUpdateMapMessage(
												getAID(DwarfConstants.GUI_AGENT_NAME), getAID(), structure.get("row"),
												structure.get("col"), structure.get("type"), structure.get("food"),
												structure.get("smell"), structure.get("stench"), name);
										if (updateMapMessage != null) {
											send(updateMapMessage);
										}
										// TODO Update activeMovementOrder
										// (isFinished = True if col and row
										// fit)
									} else {
										log.error("Perception message is incomplete: {}", receivedMessage);
									}
								} else {
									log.error("No perception message was received: {}", receivedMessage);
								}
								if (jsonObject.containsKey("state")) {
									if (jsonObject.get("state").equals("DEAD")) {
										log.info("Dwarf {} died!", name);
										takeDown();
									}
								}
							} else if (receivedMessage.getInReplyTo()
									.equals(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY)) {
								if (jsonObject.containsKey("moves")) {
									Queue<Move> moveQueue = new LinkedList<Move>();
									JSONArray moveArray = (JSONArray) jsonObject.get("moves");
									@SuppressWarnings("unchecked")
									Iterator<String> iterator = moveArray.iterator();
									while (iterator.hasNext()) {
										moveQueue.add(Move.valueOf(iterator.next()));
									}
									MovementOrder receivedMovementOrder = new MovementOrder(moveQueue);
									if (activeMovementOrder == null) {
										activeMovementOrder = receivedMovementOrder;
									} else {
										log.error("{} has already active MovementOrder", name);
									}
								} else {
									log.error("No movementorder message was received: {}", receivedMessage);
								}
							}
							if (activeMovementOrder == null) {
								ACLMessage requestMovementOrderMessage = DwarfMessagingUtils
										.createRequestMovementOrderMessage(getAID(DwarfConstants.GUI_AGENT_NAME),
												getAID(), name, collectedFood);
								if (requestMovementOrderMessage != null) {
									send(requestMovementOrderMessage);
								}
							} else if (activeMovementOrder != null) {
								Move currentMovement = activeMovementOrder.getMoves().remove();
								switch (currentMovement) {
								case UP:
									ACLMessage moveUpMessage = DwarfMessagingUtils.createMoveUpMessage(
											antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveUpMessage != null) {
										send(moveUpMessage);
									} else {
										log.info("Can not send {}", DwarfConstants.MOVE_UP_MESSAGE_REPLY);
									}
									break;
								case DOWN:
									ACLMessage moveDownMessage = DwarfMessagingUtils.createMoveDownMessage(
											antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveDownMessage != null) {
										send(moveDownMessage);
									} else {
										log.info("Can not send {}", DwarfConstants.MOVE_DOWN_MESSAGE_REPLY);
									}
									break;
								case LEFT:
									ACLMessage moveLeftMessage = DwarfMessagingUtils.createMoveLeftMessage(
											antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveLeftMessage != null) {
										send(moveLeftMessage);
									} else {
										log.info("Can not send {}", DwarfConstants.MOVE_LEFT_MESSAGE_REPLY);
									}
									break;
								case RIGHT:
									ACLMessage moveRightMessage = DwarfMessagingUtils.createMoveRightMessage(
											antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveRightMessage != null) {
										send(moveRightMessage);
									} else {
										log.info("Can not send {}", DwarfConstants.MOVE_RIGHT_MESSAGE_REPLY);
									}
									break;
								case COLLECT:
									ACLMessage moveCollectMessage = DwarfMessagingUtils.createMoveCollectMessage(
											antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveCollectMessage != null) {
										send(moveCollectMessage);
									} else {
										log.info("Can not send {}", DwarfConstants.MOVE_COLLECT_MESSAGE_REPLY);
									}
									break;
								case DROP:
									ACLMessage moveDropMessage = DwarfMessagingUtils.createMoveDropMessage(
											antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveDropMessage != null) {
										send(moveDropMessage);
									} else {
										log.info("Can not send {}", DwarfConstants.MOVE_DROP_MESSAGE_REPLY);
									}
									break;
								}
								if (activeMovementOrder.getMoves().size() == 0) {
									activeMovementOrder = null;
									log.info("Active MovementOrder is finished. Removed MovementOrder");
								}
							}
						} catch (ParseException pex) {
							log.error("Error while parsing message at position {}!", pex.getPosition(), pex);
						}
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
		doDelete();
		log.info("Agent {} removed from AgentContainer!", name);
	};
}
