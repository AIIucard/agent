package main.java.agent;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.aim.antworld.entity.AntCellType;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import main.java.DwarfConstants;
import main.java.agent.MovementOrder.Move;
import main.java.database.DwarfDatabase;
import main.java.gui.DwarfVisualCenter;
import main.java.map.MapLocation;
import main.java.utils.DwarfMessagingUtils;
import main.java.utils.DwarfPathFindingUtils;
import main.java.utils.DwarfUtils;

public class KingLittlePoisenDwarf extends GuiAgent {

	private static final long serialVersionUID = 1L;

	private DwarfVisualCenter dwarfVisualCenter;
	private DwarfDatabase dwarfDatabase;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private AgentenStatus agentStatus;

	public enum AgentenStatus {
		SEARCH, COLLECT
	}

	@Override
	public void setup() {
		log.info("Start GUIAgent...");
		log.info("Install Database in GUIAgent...");
		dwarfDatabase = new DwarfDatabase();

		log.info("Install GUI in GUIAgent...");
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dwarfVisualCenter = new DwarfVisualCenter(KingLittlePoisenDwarf.this, dwarfDatabase.getMapLocations().length, dwarfDatabase.getMapLocations()[0].length);
				DwarfVisualCenter.showOnScreen(1, dwarfVisualCenter);
			}
		});

		log.info("Install AgentContainer in GUIAgent...");
		installAgentContainer(this.getContainerController());

		log.info("Install Behaviours in GUIAgent...");
		installBehaviours();

		log.info("Set AgentStatus to SEARCH.");
		agentStatus = AgentenStatus.SEARCH;

		log.info("GUIAgent started.");
	}

	private void installBehaviours() {
		this.addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage receivedMessage = receive();
				if (receivedMessage != null) {
					if (receivedMessage.getInReplyTo().equals(DwarfConstants.UPDATE_MAP_MESSAGE_REPLY)) {
						log.info("GUIAgent received {} message: {}", DwarfConstants.UPDATE_MAP_MESSAGE_REPLY, receivedMessage);
						if (receivedMessage.getLanguage().equals("JSON")) {
							try {
								JSONParser parser = new JSONParser();
								Object obj = parser.parse(receivedMessage.getContent());
								JSONObject jsonObject = (JSONObject) obj;
								if (jsonObject.containsKey("row") && jsonObject.containsKey("col") && jsonObject.containsKey("type") && jsonObject.containsKey("food")
										&& jsonObject.containsKey("smell") && jsonObject.containsKey("stench") && jsonObject.containsKey("dwarfName")
										&& jsonObject.containsKey("performative") && jsonObject.containsKey("isObstacle")) {
									boolean isStartfield = false;
									if (jsonObject.get("type").equals(AntCellType.START.name()))
										isStartfield = true;
									boolean isTrap = false;
									if (jsonObject.get("type").equals(AntCellType.PIT.name()))
										isTrap = true;
									boolean isObstacle = (boolean) jsonObject.get("isObstacle");
									// TODO Remove
									Boolean updated = dwarfDatabase.updateMapLocation(isStartfield, isTrap, isObstacle,
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("col")), DwarfUtils.castJSONObjectLongToInt(jsonObject.get("row")),
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("food")), DwarfUtils.castJSONObjectLongToInt(jsonObject.get("smell")),
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("stench")), jsonObject.get("dwarfName").toString(),
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("performative")));
									if (updated) {
										updateMap();
									}
								} else {
									log.error("{} message is incomplete: {}", DwarfConstants.UPDATE_MAP_MESSAGE_REPLY, receivedMessage);
								}
							} catch (ParseException pex) {
								log.error("Error while parsing message at position {}!", pex.getPosition(), pex);
							}
						} else {
							log.error("Message type unknown, because language key not set! Can not decode message into JSONObject!");
						}
					} else if (receivedMessage.getInReplyTo().equals(DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY)) {
						log.info("GUIAgent received {} message: {}", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY, receivedMessage);
						if (receivedMessage.getLanguage().equals("JSON")) {
							try {
								JSONParser parser = new JSONParser();
								Object obj = parser.parse(receivedMessage.getContent());
								JSONObject jsonObject = (JSONObject) obj;
								if (jsonObject.containsKey("dwarfName") && jsonObject.containsKey("collectedFood")) {
									String name = jsonObject.get("dwarfName").toString();
									Queue<MapLocation> path = new LinkedList<MapLocation>();
									Queue<Move> moveActionQueue = new LinkedList<Move>();
									if (jsonObject.get("collectedFood").equals("true")) {
										path = searchForHomePath(name);
										if (path != null) {
											moveActionQueue = DwarfPathFindingUtils.convertPathToActions(path, false, true);
										}
									} else {
										switch (agentStatus) {
										case SEARCH:
											path = searchForPathToNextLocationToInvestigate(name);

											if (path != null) {
												moveActionQueue = DwarfPathFindingUtils.convertPathToActions(path, false, false);
											}
											break;
										case COLLECT:
											path = searchForPathToFoodLocation(name);
											if (path != null) {
												// moveActionQueue =
												// convertPathToActions(path,
												// true, false);
											}
											break;
										}
									}
									if ((path != null) && (path.size() != 0) && (moveActionQueue != null) && (moveActionQueue.size() != 0)) {
										ACLMessage movementOrderMessage = DwarfMessagingUtils.createMovementOrderMessage(receivedMessage.getSender(), getAID(),
												moveActionQueue);
										if (movementOrderMessage != null) {
											send(movementOrderMessage);
											log.info("---M--> {} send {} to {}", name, DwarfConstants.MOVEMENTORDER_MESSAGE_REPLY, movementOrderMessage.getAllReceiver());
										}
									} else {
										log.error("---X--> Unable to send MovementOrderMessage!");
									}
								} else {
									log.error("{} message is incomplete: {}", DwarfConstants.MOVEMENTORDER_MESSAGE_REPLY, receivedMessage);
								}
							} catch (ParseException pex) {
								log.error("Error while parsing message at position {}!", pex.getPosition(), pex);
							}
						} else {
							log.error("Message type unknown, because language key not set! Can not decode message into JSONObject!");
						}
					}
					// else if () {
					// // Other message
					// }
					else {
						log.error("Unknown message received! No subject was found!");
					}
				} else {
					block();
				}
			}
		});
	}

	private Queue<MapLocation> searchForHomePath(String name) {
		return DwarfPathFindingUtils.checkForPathToLocation(dwarfDatabase.getMapLocations(), dwarfDatabase.getDwarfPositions().get(name),
				dwarfDatabase.getHomeLocation());
	}

	private Queue<MapLocation> searchForPathToNextLocationToInvestigate(String name) {
		Queue<MapLocation> path;
		if (dwarfDatabase.getLocationsToBeInvestigated().size() != 0) {
			for (MapLocation mapLocation : dwarfDatabase.getLocationsToBeInvestigated()) {
				path = DwarfPathFindingUtils.checkForPathToLocation(dwarfDatabase.getMapLocations(), dwarfDatabase.getDwarfPositions().get(name), mapLocation);
				if (path != null) {
					dwarfDatabase.getLocationsToBeInvestigated().remove(mapLocation);
					return path;
				}
			}
		} else {
			log.info("No locations for investigation left! Can not find Path!");
			
		}
		return null;
	}

	private Queue<MapLocation> searchForPathToFoodLocation(String name) {
		Queue<MapLocation> path;
		for (MapLocation mapLocation : dwarfDatabase.getLocationsWithFood()) {
			path = DwarfPathFindingUtils.checkForPathToLocation(dwarfDatabase.getMapLocations(), dwarfDatabase.getDwarfPositions().get(name), mapLocation);
			if (path != null) {
				return path;
			}
		}
		return null;
	}

	public void shutDownAgent() {
		try {
			log.info("Shut down GUIAgent...");
			getContainerController().kill();
			doDelete();
		} catch (StaleProxyException spex) {
			log.error("Error in shut down process!", spex);
		}
	}

	public void updateGUI() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dwarfVisualCenter.updateTabsPanel();
			}
		});
	}

	private void updateMap() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dwarfVisualCenter.getEditor().setSize(new Dimension(200 + (dwarfDatabase.getMapLocations().length * DwarfConstants.SQUARE_DIMENSION),
						200 + (dwarfDatabase.getMapLocations()[0].length * DwarfConstants.SQUARE_DIMENSION)));
				dwarfVisualCenter.repaintMap();
			}
		});
	}

	public void installAgentContainer(AgentContainer agentContainer) {
		getDwarfDatabase().setDwarfContainer(agentContainer);
	}

	public DwarfDatabase getDwarfDatabase() {
		return dwarfDatabase;
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub

	}
}
