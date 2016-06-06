package main.java.gui;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.util.HashMap;

import main.java.map.MapLocation;

public class DwarfDatabase {
	private HashMap<String, AgentController> agents;
	private AgentContainer agentContainer;
	private MapLocation[][] mapLocations;

	public DwarfDatabase() {
		agents = new HashMap<String, AgentController>();
		mapLocations = new MapLocation[15][15];
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

}
