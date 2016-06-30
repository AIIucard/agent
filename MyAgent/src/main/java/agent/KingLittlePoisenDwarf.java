package main.java.agent;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
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
import main.java.map.MapLocation.LocationStatus;
import main.java.map.UnknownMapLocation;
import main.java.utils.DwarfMessagingUtils;
import main.java.utils.DwarfPathFindingUtils;
import main.java.utils.DwarfUtils;

public class KingLittlePoisenDwarf extends GuiAgent {

	private static final long serialVersionUID = 1L;

	private DwarfVisualCenter dwarfVisualCenter;
	private DwarfDatabase dwarfDatabase;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private DwarfMood dwarfMood;

	public enum DwarfMood {
		SEARCH_MOOD, DRINK_MOOD
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
		dwarfMood = DwarfMood.SEARCH_MOOD;

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
								if (isUpdateMapMessageComplete(jsonObject)) {
									boolean isStartfield = jsonObject.get("type").equals(AntCellType.START.name()) ? true : false;
									boolean isTrap = jsonObject.get("type").equals(AntCellType.PIT.name()) ? true : false;
									boolean isObstacle = (boolean) jsonObject.get("isObstacle");
									// TODO Remove
									Boolean locationUpdated = dwarfDatabase.updateMapLocation(isStartfield, isTrap, isObstacle,
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("col")), DwarfUtils.castJSONObjectLongToInt(jsonObject.get("row")),
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("food")), DwarfUtils.castJSONObjectLongToInt(jsonObject.get("smell")),
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("stench")), jsonObject.get("dwarfName").toString(),
											DwarfUtils.castJSONObjectLongToInt(jsonObject.get("performative")));
									searchAllMapLocationsForTraps();
									if (locationUpdated) {
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
								if (isRequestMovementOrderMessageComplete(jsonObject)) {
									String dwarfName = jsonObject.get("dwarfName").toString();
									Queue<MapLocation> path = new LinkedList<MapLocation>();
									Queue<Move> moveActionQueue = new LinkedList<Move>();
									Boolean collectActionNeeded = false;
									Boolean dropActionNeeded = false;
									if (true == (Boolean) jsonObject.get("collectedFood")) {
										path = searchForHomePath(dwarfName);
										dropActionNeeded = true;
									} else {
										if (dwarfDatabase.getLocationsToBeInvestigated().size() != 0 || dwarfDatabase.getLocationsWithFood().size() != 0) {
											// Dwarf MovementOrder
											// classification
											if (dwarfDatabase.getLocationsWithFood().size() >= 2) {
												dwarfMood = DwarfMood.DRINK_MOOD;
											} else {
												dwarfMood = DwarfMood.SEARCH_MOOD;
											}
											switch (dwarfMood) {
											case SEARCH_MOOD:
												path = searchForPathToNextLocationToInvestigate(dwarfName);
												if (path == null && dwarfDatabase.getLocationsToBeInvestigated().size() == 0) {
													dwarfMood = DwarfMood.DRINK_MOOD;
													path = searchForPathToFoodLocation(dwarfName);
													collectActionNeeded = true;
												}
												break;
											case DRINK_MOOD:
												path = searchForPathToFoodLocation(dwarfName);
												collectActionNeeded = true;
												if (path == null && dwarfDatabase.getLocationsWithFood().size() == 0) {
													dwarfMood = DwarfMood.SEARCH_MOOD;
													path = searchForPathToNextLocationToInvestigate(dwarfName);
												}
												break;
											}
										} else {
											log.info("No interesting locations left. Start search for Traps in MapLocations...");
											searchAllMapLocationsForTraps();
											log.info("Finished search for Traps in MapLocations.");
											updateMap();
										}
									}
									if (path != null) {
										moveActionQueue = DwarfPathFindingUtils.convertPathToActions(path, collectActionNeeded, dropActionNeeded);
									} else if (dwarfDatabase.getLocationsToBeInvestigated().size() == 0 && dwarfDatabase.getLocationsWithFood().size() == 0) {
										log.info("No interesting locations left. Map is Empty!");
									} else {
										log.error("######################## Error with pathfinding! ########################");
									}
									if ((path != null) && (path.size() != 0) && (moveActionQueue != null) && (moveActionQueue.size() != 0)) {
										ACLMessage movementOrderMessage = DwarfMessagingUtils.createMovementOrderMessage(receivedMessage.getSender(), getAID(),
												moveActionQueue);
										if (movementOrderMessage != null) {
											send(movementOrderMessage);
											log.info("---M--> {} send {} to {}", getAID(), DwarfConstants.MOVEMENTORDER_MESSAGE_REPLY, dwarfName);
										}
									} else {
										log.error("---X--> Unable to send MovementOrderMessage!");
									}
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

	private boolean isRequestMovementOrderMessageComplete(JSONObject jsonObject) {
		return jsonObject.containsKey("dwarfName") && jsonObject.containsKey("collectedFood");
	}

	private boolean isUpdateMapMessageComplete(JSONObject jsonObject) {
		return jsonObject.containsKey("row") && jsonObject.containsKey("col") && jsonObject.containsKey("type") && jsonObject.containsKey("food")
				&& jsonObject.containsKey("smell") && jsonObject.containsKey("stench") && jsonObject.containsKey("dwarfName") && jsonObject.containsKey("performative")
				&& jsonObject.containsKey("isObstacle");
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
			log.info("No locations for investigation left!");
		}
		return null;
	}

	private Queue<MapLocation> searchForPathToFoodLocation(String name) {
		Queue<MapLocation> path;
		if (dwarfDatabase.getLocationsWithFood().size() != 0) {
			for (MapLocation mapLocation : dwarfDatabase.getLocationsWithFood()) {
				path = DwarfPathFindingUtils.checkForPathToLocation(dwarfDatabase.getMapLocations(), dwarfDatabase.getDwarfPositions().get(name), mapLocation);
				if (path != null) {
					return path;
				}
			}
		} else {
			log.info("No locations with Food left!");
		}
		return null;
	}

	private void searchAllMapLocationsForTraps() {
		log.info("Start search for Traps...");
		MapLocation[][] mapLocations = dwarfDatabase.getMapLocations();
		for (int col = 0; col < mapLocations.length; col++) {
			for (int row = 0; row < mapLocations[0].length; row++) {
				if (mapLocations[col][row] == null) {
					searchForTrapsByStench(col, row);

				}
				// else if (!searchForSurrounding4LocationsWithStench(col, row))
				// {
				//
				// }
				// // Solo Stench Traps
				// searchForSoloTraps(col, row);
			}
		}
		log.info("Finish search for Traps.");
	}

	private boolean searchForTrapsByStench(int col, int row) {
		MapLocation[][] mapLocations = dwarfDatabase.getMapLocations();
		int upperStench = checkStenchUpperLocation(col, row, mapLocations);
		int lowerStench = checkStenchLowerLocation(col, row, mapLocations);
		int leftStench = checkStenchLeftLocation(col, row, mapLocations);
		int rightStench = checkStenchRightLocation(col, row, mapLocations);

		if (upperStench >= 1 && lowerStench >= 1 && leftStench >= 1 && rightStench >= 1) {
			return updateTrapAndSurroundingLocations(col, row);
		}
		if (upperStench > 1) {
			if (checkForSurroundingNullLocations(col, row - 1, mapLocations, upperStench)) {
				if (lowerStench == 1 && leftStench == 1 && rightStench == 1) {
					return updateTrapAndSurroundingLocations(col, row);
				} else if (lowerStench > 1) {
					if (checkForSurroundingNullLocations(col, row + 1, mapLocations, lowerStench)) {
						if (leftStench == 1 && rightStench == 1) {
							return updateTrapAndSurroundingLocations(col, row);
						} else if (leftStench > 1) {
							if (checkForSurroundingNullLocations(col - 1, row, mapLocations, leftStench)) {
								if (rightStench == 1) {
									return updateTrapAndSurroundingLocations(col, row);
								} else if (rightStench > 1) {
									if (checkForSurroundingNullLocations(col + 1, row, mapLocations, rightStench)) {
										return updateTrapAndSurroundingLocations(col, row);
									}
								}
							}
						}
					}
				}
			}
		}
		if (lowerStench > 1) {
			if (checkForSurroundingNullLocations(col, row + 1, mapLocations, lowerStench)) {
				if (upperStench == 1 && leftStench == 1 && rightStench == 1) {
					return updateTrapAndSurroundingLocations(col, row);
				} else if (upperStench > 1) {
					if (checkForSurroundingNullLocations(col, row - 1, mapLocations, upperStench)) {
						if (leftStench == 1 && rightStench == 1) {
							return updateTrapAndSurroundingLocations(col, row);
						} else if (leftStench > 1) {
							if (checkForSurroundingNullLocations(col - 1, row, mapLocations, leftStench)) {
								if (rightStench == 1) {
									return updateTrapAndSurroundingLocations(col, row);
								} else if (rightStench > 1) {
									if (checkForSurroundingNullLocations(col + 1, row, mapLocations, rightStench)) {
										return updateTrapAndSurroundingLocations(col, row);
									}
								}
							}
						}
					}
				}
			}
		}
		if (leftStench > 1) {
			if (checkForSurroundingNullLocations(col - 1, row, mapLocations, leftStench)) {
				if (upperStench == 1 && lowerStench == 1 && rightStench == 1) {
					return updateTrapAndSurroundingLocations(col, row);
				} else if (upperStench > 1) {
					if (checkForSurroundingNullLocations(col, row - 1, mapLocations, upperStench)) {
						if (lowerStench == 1 && rightStench == 1) {
							return updateTrapAndSurroundingLocations(col, row);
						} else if (lowerStench > 1) {
							if (checkForSurroundingNullLocations(col, row + 1, mapLocations, lowerStench)) {
								if (rightStench == 1) {
									return updateTrapAndSurroundingLocations(col, row);
								} else if (rightStench > 1) {
									if (checkForSurroundingNullLocations(col + 1, row, mapLocations, rightStench)) {
										return updateTrapAndSurroundingLocations(col, row);
									}
								}
							}
						}
					}
				}
			}
		}
		if (rightStench > 1) {
			if (checkForSurroundingNullLocations(col + 1, row, mapLocations, rightStench)) {
				if (upperStench == 1 && lowerStench == 1 && leftStench == 1) {
					return updateTrapAndSurroundingLocations(col, row);
				} else if (upperStench > 1) {
					if (checkForSurroundingNullLocations(col, row - 1, mapLocations, upperStench)) {
						if (lowerStench == 1 && leftStench == 1) {
							return updateTrapAndSurroundingLocations(col, row);
						} else if (lowerStench > 1) {
							if (checkForSurroundingNullLocations(col, row + 1, mapLocations, lowerStench)) {
								if (leftStench == 1) {
									return updateTrapAndSurroundingLocations(col, row);
								} else if (leftStench > 1) {
									if (checkForSurroundingNullLocations(col - 1, row, mapLocations, leftStench)) {
										return updateTrapAndSurroundingLocations(col, row);
									}
								}
							}
						}
					}
				}
			}
		}
		// if (checkForSurroundingNullLocations(col, row, mapLocations,
		// upperStench) && checkIf3SurroundingLocationsKnown(col, row,
		// mapLocations)) {
		// return updateTrapAndSurroundingLocations(col, row);
		// } else if (checkForSurroundingNullLocations(col, row, mapLocations,
		// lowerStench) && checkIf3SurroundingLocationsKnown(col, row,
		// mapLocations)) {
		// return updateTrapAndSurroundingLocations(col, row);
		// } else if (checkForSurroundingNullLocations(col, row, mapLocations,
		// leftStench) && checkIf3SurroundingLocationsKnown(col, row,
		// mapLocations)) {
		// return updateTrapAndSurroundingLocations(col, row);
		// } else if (checkForSurroundingNullLocations(col, row, mapLocations,
		// rightStench) && checkIf3SurroundingLocationsKnown(col, row,
		// mapLocations)) {
		// return updateTrapAndSurroundingLocations(col, row);
		// } else {
		log.debug("No traps found...");
		return false;
		// }
	}

	private boolean checkIf3SurroundingLocationsKnown(int col, int row, MapLocation[][] mapLocations) {
		int countLocations = 0;
		if (col <= mapLocations.length - 1 && row <= mapLocations[0].length - 1) {
			if (row - 1 < 0 || isValidLocation(col, row - 1, mapLocations) || isObstacleLocation(col, row - 1, mapLocations)) {
				++countLocations;
			}
			if (isValidLocation(col, row + 1, mapLocations) || isObstacleLocation(col, row + 1, mapLocations)) {
				++countLocations;
			}
			if (col - 1 < 0 || isValidLocation(col - 1, row, mapLocations) || isObstacleLocation(col - 1, row, mapLocations)) {
				++countLocations;
			}
			if (isValidLocation(col + 1, row, mapLocations) || isObstacleLocation(col + 1, row, mapLocations)) {
				++countLocations;
			}
			if (countLocations == 3) {
				return true;
			}
		}
		return false;
	}

	private boolean checkForSurroundingNullLocations(int col, int row, MapLocation[][] mapLocations, int stenchCount) {
		if (col >= 0 && col <= mapLocations.length - 1 && row >= 0 && row <= mapLocations[0].length - 1) {
			if (countSurroundingNullLocations(col, row, mapLocations) == stenchCount) {
				return true;
			}
		}
		return false;
	}

	private int countSurroundingNullLocations(int col, int row, MapLocation[][] mapLocations) {
		int nullLocationCounter = 0;
		if (col - 1 >= 0 && row >= 0 && row <= mapLocations[0].length - 1) {
			if (mapLocations[col - 1][row] == null) {
				++nullLocationCounter;
			}
		}
		if (col + 1 <= mapLocations.length - 2 && row >= 0 && row <= mapLocations[0].length - 1) {
			if (mapLocations[col + 1][row] == null) {
				++nullLocationCounter;
			}
		}
		if (row - 1 >= 0 && col >= 0 && col <= mapLocations.length - 1) {
			if (mapLocations[col][row - 1] == null) {
				++nullLocationCounter;
			}
		}
		if (row + 1 <= mapLocations[0].length - 2 && col >= 0 && col <= mapLocations.length - 1) {
			if (mapLocations[col][row + 1] == null) {
				++nullLocationCounter;
			}
		}
		return nullLocationCounter;
	}

	private int checkStenchUpperLocation(int col, int row, MapLocation[][] mapLocations) {
		if (col - 1 >= 0) {
			if (isValidLocation(col - 1, row, mapLocations)) {
				if (isObstacleLocation(col - 1, row, mapLocations)) {
					return 1;
				} else {
					if (locationHasStench(col - 1, row, mapLocations))
						return mapLocations[col - 1][row].getStenchConcentration();
				}
			}
		}
		return 0;
	}

	private int checkStenchLowerLocation(int col, int row, MapLocation[][] mapLocations) {
		if (col + 1 <= mapLocations.length - 2) {
			if (isValidLocation(col + 1, row, mapLocations)) {
				if (isObstacleLocation(col + 1, row, mapLocations)) {
					return 1;
				} else {
					if (locationHasStench(col + 1, row, mapLocations))
						return mapLocations[col + 1][row].getStenchConcentration();
				}
			}
		}
		return 0;
	}

	private int checkStenchLeftLocation(int col, int row, MapLocation[][] mapLocations) {
		if (row - 1 >= 0) {
			if (isValidLocation(col, row - 1, mapLocations)) {
				if (isObstacleLocation(col, row - 1, mapLocations)) {
					return 1;
				} else {
					if (locationHasStench(col, row - 1, mapLocations))
						return mapLocations[col][row - 1].getStenchConcentration();
				}
			}
		}
		return 0;
	}

	private int checkStenchRightLocation(int col, int row, MapLocation[][] mapLocations) {
		if (row + 1 <= mapLocations[0].length - 2) {
			if (isValidLocation(col, row + 1, mapLocations)) {
				if (isObstacleLocation(col, row + 1, mapLocations)) {
					return 1;
				} else {
					if (locationHasStench(col, row + 1, mapLocations))
						return mapLocations[col][row + 1].getStenchConcentration();
				}
			}
		}
		return 0;
	}

	private boolean isObstacleLocation(int col, int row, MapLocation[][] mapLocations) {
		if (col >= 0 && col <= mapLocations.length - 1 && row >= 0 && row <= mapLocations[0].length - 1) {
			if (mapLocations[col][row] != null) {
				if (mapLocations[col][row].getLocationStatus() != null) {
					return mapLocations[col][row].getLocationStatus().contains(LocationStatus.OBSTACLE);
				}
			}
		}
		return false;
	}

	private boolean locationHasStench(int col, int row, MapLocation[][] mapLocations) {
		if (col >= 0 && col <= mapLocations.length - 1 && row >= 0 && row <= mapLocations[0].length - 1) {
			return mapLocations[col][row].getStenchConcentration() > 0;
		}
		return false;
	}

	private boolean isValidLocation(int col, int row, MapLocation[][] mapLocations) {
		if (col >= 0 && col <= mapLocations.length - 1 && row >= 0 && row <= mapLocations[0].length - 1) {
			return mapLocations[col][row] != null && !(mapLocations[col][row] instanceof UnknownMapLocation);
		}
		return false;
	}

	private boolean updateTrapAndSurroundingLocations(int col, int row) {
		MapLocation[][] mapLocations = dwarfDatabase.getMapLocations();
		mapLocations[col][row] = new MapLocation(col, row, 0, 0, 0, new ArrayList<LocationStatus>(Arrays.asList(LocationStatus.PIT)));
		log.info("Found Trap at {}", mapLocations[col][row].toShortString());
		updateSpecificLocation(col - 1, row);
		updateSpecificLocation(col + 1, row);
		updateSpecificLocation(col, row - 1);
		updateSpecificLocation(col, row + 1);
		return true;
	}

	private void updateSpecificLocation(int col, int row) {
		MapLocation[][] mapLocations = dwarfDatabase.getMapLocations();
		if (col >= 0 && col <= mapLocations.length - 1 && row >= 0 && row <= mapLocations[0].length - 1) {
			MapLocation location = dwarfDatabase.getMapLocations()[col][row];
			if (location != null && !(location instanceof UnknownMapLocation)) {
				dwarfDatabase.getMapLocations()[location.getIntColumnCoordinate()][location.getIntRowCoordinate()].updateLocation(location.getIntColumnCoordinate(),
						location.getIntRowCoordinate(), location.getSmellConcentration(), location.getStenchConcentration(), location.getFoodUnits(),
						DwarfUtils.getLocationStatus(location.getLocationStatus().contains(LocationStatus.PIT),
								location.getLocationStatus().contains(LocationStatus.OBSTACLE), location.getFoodUnits(), location.getSmellConcentration(),
								location.getStenchConcentration()));
				if (dwarfDatabase.getMapLocations()[col][row].getStenchConcentration() - 1 == 0) {
					dwarfDatabase.getMapLocations()[col][row].setSave(true);
				}
				dwarfDatabase.getMapLocations()[col][row].setStenchConcentration(dwarfDatabase.getMapLocations()[col][row].getStenchConcentration() - 1);
				if (dwarfDatabase.getMapLocations()[col][row].isSave()) {
					dwarfDatabase.addSurroundingLocationsToBeInvestigated(col, row);
				}
			}
		} else {
			log.error("############### Tryed to update MapLocation [{},{}]", col, row);
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
