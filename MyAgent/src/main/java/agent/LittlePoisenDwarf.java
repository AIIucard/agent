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

	private boolean waitingForMovementOrder;
	private boolean isAlive;

	private int expectedRow;
	private int expectedColumn;

	@Override
	protected void setup() {
		name = getLocalName();
		antWorldGameLeaderReply = "";
		waitingForMovementOrder = false;
		isAlive = true;
		expectedRow = -1;
		expectedColumn = -1;

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
						log.info("---M--> {} send {} to {}", name, "LoginMessage", loginMessage.getAllReceiver());
					} else {
						log.info("---X--> Can not send {}", "LoginMessage");
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
							if ((receivedMessage.getInReplyTo() != DwarfConstants.MOVEMENTORDER_MESSAGE_REPLY)) {
								if (receivedMessage.getReplyWith() != null) {
									antWorldGameLeaderReply = receivedMessage.getReplyWith();
								}
								// TODO check if collect and drop are possible
								if (jsonObject.containsKey("cell")) {
									JSONObject structure = (JSONObject) jsonObject.get("cell");
									if (structure.containsKey("row") && structure.containsKey("col")) {

										// Check for Obstacle
										boolean isObstacle;
										ACLMessage updateMapMessage;
										if ((expectedColumn == -1) && (expectedRow == -1)) {
											expectedColumn = DwarfUtils.castJSONObjectLongToInt(structure.get("col"));
											expectedRow = DwarfUtils.castJSONObjectLongToInt(structure.get("row"));
										}
										if (checkForObstacle(expectedColumn, expectedRow, DwarfUtils.castJSONObjectLongToInt(structure.get("col")),
												DwarfUtils.castJSONObjectLongToInt(structure.get("row")), receivedMessage.getPerformative(), jsonObject.get("action"))) {
											log.info("Found obstacle at Location[{},{}]", expectedColumn, expectedRow);
											isObstacle = true;
											updateMapMessage = DwarfMessagingUtils.createUpdateMapMessage(getAID(DwarfConstants.GUI_AGENT_NAME), getAID(), expectedRow,
													expectedColumn, structure.get("type"), 0, 0, 0, name, receivedMessage.getPerformative(), isObstacle);
											expectedColumn = DwarfUtils.castJSONObjectLongToInt(structure.get("col"));
											expectedRow = DwarfUtils.castJSONObjectLongToInt(structure.get("row"));
										} else {
											isObstacle = false;
											updateMapMessage = DwarfMessagingUtils.createUpdateMapMessage(getAID(DwarfConstants.GUI_AGENT_NAME), getAID(),
													structure.get("row"), structure.get("col"), structure.get("type"), structure.get("food"), structure.get("smell"),
													structure.get("stench"), name, receivedMessage.getPerformative(), isObstacle);
										}
										if (updateMapMessage != null) {
											send(updateMapMessage);
											log.info("---M--> {} send {} to {}", name, DwarfConstants.UPDATE_MAP_MESSAGE_REPLY, updateMapMessage.getAllReceiver());
										} else {
											log.info("---X--> Can not send {}", DwarfConstants.UPDATE_MAP_MESSAGE_REPLY);
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
										log.info("####################### Dwarf {} died! #######################", name);
										isAlive = false;
										takeDown();
									}
								}
							} else if (receivedMessage.getInReplyTo().equals(DwarfConstants.MOVEMENTORDER_MESSAGE_REPLY)) {
								if (jsonObject.containsKey("moves")) {
									Queue<Move> moveQueue = new LinkedList<Move>();
									JSONArray moveArray = (JSONArray) jsonObject.get("moves");
									@SuppressWarnings("unchecked")
									Iterator<String> iterator = moveArray.iterator();
									while (iterator.hasNext()) {
										moveQueue.add(Move.valueOf(iterator.next()));
									}
									MovementOrder receivedMovementOrder = new MovementOrder(moveQueue);
									waitingForMovementOrder = false;
									if (activeMovementOrder == null) {
										activeMovementOrder = receivedMovementOrder;
									} else {
										log.error("{} has already active MovementOrder", name);
									}
								} else {
									log.error("No movementorder message was received: {}", receivedMessage);
								}
							}
							if (activeMovementOrder == null && !waitingForMovementOrder && isAlive) {
								ACLMessage requestMovementOrderMessage = DwarfMessagingUtils.createRequestMovementOrderMessage(getAID(DwarfConstants.GUI_AGENT_NAME),
										getAID(), name, collectedFood);
								if (requestMovementOrderMessage != null) {
									send(requestMovementOrderMessage);
									waitingForMovementOrder = true;
									log.info("---M--> {} send {} to {}", name, DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY,
											requestMovementOrderMessage.getAllReceiver());
								}
							} else if (activeMovementOrder != null && isAlive) {
								Move currentMovement = activeMovementOrder.getMoves().remove();
								switch (currentMovement) {
								case UP:
									ACLMessage moveUpMessage = DwarfMessagingUtils.createMoveUpMessage(antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveUpMessage != null) {
										expectedRow -= 1;
										send(moveUpMessage);
										log.info("---M--> {} send {} to {}", name, DwarfConstants.MOVE_UP_MESSAGE_REPLY, moveUpMessage.getAllReceiver());
									} else {
										log.info("---X--> Can not send {}", DwarfConstants.MOVE_UP_MESSAGE_REPLY);
									}
									break;
								case DOWN:
									ACLMessage moveDownMessage = DwarfMessagingUtils.createMoveDownMessage(antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveDownMessage != null) {
										expectedRow += 1;
										send(moveDownMessage);
										log.info("---M--> {} send {} to {}", name, DwarfConstants.MOVE_DOWN_MESSAGE_REPLY, moveDownMessage.getAllReceiver());
									} else {
										log.info("---X--> Can not send {}", DwarfConstants.MOVE_DOWN_MESSAGE_REPLY);
									}
									break;
								case LEFT:
									ACLMessage moveLeftMessage = DwarfMessagingUtils.createMoveLeftMessage(antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveLeftMessage != null) {
										expectedColumn -= 1;
										send(moveLeftMessage);
										log.info("---M--> {} send {} to {}", name, DwarfConstants.MOVE_LEFT_MESSAGE_REPLY, moveLeftMessage.getAllReceiver());
									} else {
										log.info("---X--> Can not send {}", DwarfConstants.MOVE_LEFT_MESSAGE_REPLY);
									}
									break;
								case RIGHT:
									ACLMessage moveRightMessage = DwarfMessagingUtils.createMoveRightMessage(antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveRightMessage != null) {
										expectedColumn += 1;
										send(moveRightMessage);
										log.info("---M--> {} send {} to {}", name, DwarfConstants.MOVE_RIGHT_MESSAGE_REPLY, moveRightMessage.getAllReceiver());
									} else {
										log.info("---X--> Can not send {}", DwarfConstants.MOVE_RIGHT_MESSAGE_REPLY);
									}
									break;
								case COLLECT:
									ACLMessage moveCollectMessage = DwarfMessagingUtils.createMoveCollectMessage(antWorldGameLeaderAID, getAID(),
											antWorldGameLeaderReply);
									if (moveCollectMessage != null) {
										send(moveCollectMessage);
										log.info("---M--> {} send {} to {}", name, DwarfConstants.MOVE_COLLECT_MESSAGE_REPLY, moveCollectMessage.getAllReceiver());
									} else {
										log.info("---X--> Can not send {}", DwarfConstants.MOVE_COLLECT_MESSAGE_REPLY);
									}
									break;
								case DROP:
									ACLMessage moveDropMessage = DwarfMessagingUtils.createMoveDropMessage(antWorldGameLeaderAID, getAID(), antWorldGameLeaderReply);
									if (moveDropMessage != null) {
										send(moveDropMessage);
										log.info("{} send {} to {}", name, DwarfConstants.MOVE_DROP_MESSAGE_REPLY, moveDropMessage.getAllReceiver());
									} else {
										log.info("---X--> Can not send {}", DwarfConstants.MOVE_DROP_MESSAGE_REPLY);
									}
									break;
								}
								if (activeMovementOrder.getMoves().size() == 0) {
									activeMovementOrder = null;
									log.info("Active MovementOrder is finished. Removed MovementOrder");
								}
							}
							log.debug("ExpectedColumn {} ExpectedRow {} for next MapLocation", expectedColumn, expectedRow);
						} catch (ParseException pex) {
							log.error("Error while parsing message at position {}!", pex.getPosition(), pex);
						}
					} else {
						log.error("Message type unknown, because language key not set! Can not decode message into JSONObject!");
					}
				} else {
					block();
				}
			}

			private boolean checkForObstacle(int expectedColumn, int expectedRow, Object column, Object row, int performative, Object action) {
				if (performative == ACLMessage.REFUSE && !action.equals(AntWorldConsts.ANT_ACTION_COLLECT)) {
					if ((expectedColumn != ((Integer) column).intValue()) || (expectedRow != Integer.valueOf((Integer) row))) {
						return true;
					}
				}
				return false;
			}
		});
	}

	@Override
	protected void takeDown() {
		doDelete();
		log.info("Agent {} removed from AgentContainer!", name);
	};
}
