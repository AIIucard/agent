package main.java.agent;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;

import javax.swing.JFrame;

import main.java.gui.DwarfDatabase;
import main.java.gui.DwarfVisualCenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmallLittlePoisenDwarfWithGUI extends GuiAgent {

	private static final long serialVersionUID = 1L;

	private JFrame dwarfVisualCenter;
	private DwarfDatabase dwarfDatabase;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	@Override
	public void setup() {

		log.info("Install Database in GUIAgent...");
		dwarfDatabase = new DwarfDatabase();

		log.info("Install GUI in GUIAgent...");
		dwarfVisualCenter = new DwarfVisualCenter();
		dwarfVisualCenter.setVisible(true);

		log.info("Install AgentContainer in GUIAgent...");
		installAgentContainer(this.getContainerController());
	}

	@Override
	protected void onGuiEvent(GuiEvent ev) {
		// TODO Auto-generated method stub

	}

	public void installAgentContainer(AgentContainer agentContainer) {
		dwarfDatabase.setAgentContainer(agentContainer);
	}

	public JFrame getWindow() {
		return dwarfVisualCenter;
	}

	public void setWindow(JFrame window) {
		this.dwarfVisualCenter = window;
	}
}
