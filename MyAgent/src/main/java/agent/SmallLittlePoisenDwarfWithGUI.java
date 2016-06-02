package main.java.agent;

import java.util.HashMap;

import javax.swing.JFrame;

import jade.core.Profile;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.AgentController;
import main.java.gui.DwarfVisualCenter;

public class SmallLittlePoisenDwarfWithGUI extends GuiAgent {

	private static final long serialVersionUID = 1L;

	private Profile profile;
	private HashMap<String, AgentController> agents;
	private JFrame dwarfVisualCenter;

	@Override
	public void setup() {
		dwarfVisualCenter = new DwarfVisualCenter();
		dwarfVisualCenter.setVisible(true);
		agents = new HashMap<String, AgentController>();
	}

	@Override
	protected void onGuiEvent(GuiEvent ev) {
		// TODO Auto-generated method stub

	}

	public Profile getProfile() {
		return profile;
	}

	public HashMap<String, AgentController> getAgents() {
		return agents;
	}

	public void installAgent(String name, AgentController agent) {
		agents.put(name, agent);
	}

	public JFrame getWindow() {
		return dwarfVisualCenter;
	}

	public void setWindow(JFrame window) {
		this.dwarfVisualCenter = window;
	}
}
