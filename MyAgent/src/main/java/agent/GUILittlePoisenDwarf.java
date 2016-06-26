package main.java.agent;

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
import main.java.database.DwarfDatabase;
import main.java.gui.DwarfVisualCenter;
import main.java.utils.DwarfUtils;

public class GUILittlePoisenDwarf extends GuiAgent {

	private static final long serialVersionUID = 1L;

	private DwarfVisualCenter dwarfVisualCenter;
	private DwarfDatabase dwarfDatabase;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	@Override
	public void setup() {
		log.info("Start GUIAgent...");
		log.info("Install Database in GUIAgent...");
		dwarfDatabase = new DwarfDatabase();

		log.info("Install GUI in GUIAgent...");
		dwarfVisualCenter = new DwarfVisualCenter(this);

		log.info("Install AgentContainer in GUIAgent...");
		installAgentContainer(this.getContainerController());

		log.info("Install Behaviours in GUIAgent...");
		installBehaviours();
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
										&& jsonObject.containsKey("smell") && jsonObject.containsKey("stench") && jsonObject.containsKey("dwarfName")) {
									boolean isStartfield = false;
									if (jsonObject.get("type").equals(AntCellType.START.name()))
										isStartfield = true;
									boolean isTrap = false;
									// TODO check type
									if (jsonObject.get("type").equals(AntCellType.PIT.name()))
										isTrap = true;
									boolean isBlockade = false;
									// TODO check type
									if (jsonObject.get("type").equals(AntCellType.OBSTACLE.name()))
										isBlockade = true;
									dwarfDatabase.updateMapLocation(isStartfield, isTrap, isBlockade, DwarfUtils.castJSONObjectLongToInt(jsonObject.get("col")),
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("row")), DwarfUtils.castJSONObjectLongToInt(jsonObject.get("food")),
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("smell")), DwarfUtils.castJSONObjectLongToInt(jsonObject.get("stench")),
											jsonObject.get("dwarfName").toString());
									dwarfVisualCenter.repaintMap();
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
								if (jsonObject.containsKey("dwarfName")) {
									String name = jsonObject.get("dwarfName").toString();
									// DwarfPathFindingUtils.checkForPathToLocation(dwarfDatabase.getMapLocations(),
									// startLocation, targetMapLocation)
									// // TODO DO Pathfinding
									// ACLMessage movementOrderMessage =
									// DwarfMessagingUtils.createMovementOrderMessage(receiver, sender, dwarfName);
									// if (movementOrderMessage != null) {
									// send(movementOrderMessage);
									// }
								} else {
									log.error("{} message is incomplete: {}", DwarfConstants.REQUEST_MOVEMENTORDER_MESSAGE_REPLY, receivedMessage);
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

	@Override
	protected void onGuiEvent(GuiEvent ev) {
		switch (ev.getType()) {
		// case null;
		}
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
		dwarfVisualCenter.updateTabsPanel();
	}

	public void installAgentContainer(AgentContainer agentContainer) {
		getDwarfDatabase().setDwarfContainer(agentContainer);
	}

	public DwarfDatabase getDwarfDatabase() {
		return dwarfDatabase;
	}
}
