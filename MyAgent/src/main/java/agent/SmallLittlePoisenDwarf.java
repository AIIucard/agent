package main.java.agent;

import jade.core.Agent;
import jade.core.Location;
import main.java.gui.DwarfVisualCenter;

public class SmallLittlePoisenDwarf extends Agent implements InterfaceAgent {
	private static final long serialVersionUID = 1L;

	private String name;
	private DwarfVisualCenter dwarfVisualCenter;

	@Override
	protected void setup() {
		name = getLocalName();
		dwarfVisualCenter = DwarfVisualCenter.getInstance();

		Location loc = here();

		// some info about me
		System.out.println(name + ": my local name is " + name);
		System.out.println(name + ": my global name is " + getName());
		System.out.println(name + ": my state is " + getAgentState());
		// some info about my location
		System.out.println(name + ": my location is " + loc.getName());
		System.out.println(name + ": the id of my location is " + loc.getID());
		System.out.println(name + ": the address of my location is " + loc.getAddress());
		System.out.println(name + ": the protocol of the location is " + loc.getProtocol());
	}

	@Override
	protected void takeDown() {
		dwarfVisualCenter.getAgents().remove(name);
	};
}
