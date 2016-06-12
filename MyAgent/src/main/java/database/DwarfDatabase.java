package main.java.database;

import java.util.HashMap;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import main.java.map.MapLocation;

public class DwarfDatabase {
	private HashMap<String, AgentController> agents;
	private AgentContainer agentContainer;
	private MapLocation[][] mapLocations;
	private int agentCounter;

	public DwarfDatabase() {
		agents = new HashMap<String, AgentController>();
		mapLocations = new MapLocation[15][15];
		agentCounter = 0;
	}

	public void updateMapLocation() {
		// TODO
	}

	public HashMap<String, AgentController> getAgents() {
		return agents;
	}

	public void installAgent(String name, AgentController agent) {
		agents.put(name, agent);
	}

	public AgentContainer getAgentContainer() {
		return agentContainer;
	}

	public void setAgentContainer(AgentContainer agentContainer) {
		this.agentContainer = agentContainer;
	}

	public int getAgentCounter() {
		return agentCounter;
	}

	public void incrementAgentCounter() {
		agentCounter = agentCounter + 1;
	}

	public void decrementAgentCounter() {
		if (agentCounter > 0)
			agentCounter = agentCounter - 1;
	}

	public MapLocation[][] getMapLocations() {
		return mapLocations;
	}
}
