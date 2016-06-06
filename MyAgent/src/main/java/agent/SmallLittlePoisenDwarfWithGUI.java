package main.java.agent;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

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
		log.info("Start GUIAgent...");
		log.info("Install Database in GUIAgent...");
		dwarfDatabase = new DwarfDatabase();

		log.info("Install GUI in GUIAgent...");
		dwarfVisualCenter = new DwarfVisualCenter(this);

		log.info("Install AgentContainer in GUIAgent...");
		installAgentContainer(this.getContainerController());
		log.info("GUIAgent started.");
	}

	@Override
	protected void onGuiEvent(GuiEvent ev) {
		// TODO Auto-generated method stub

	}

	public void shutDownAgent() {
		try {
			log.info("Shut down GUIAgent...");
			getContainerController().kill();
			doDelete();
		} catch (StaleProxyException e) {
			log.error("Error in shut down process {}", e.getStackTrace().toString());
		}
	}

	public void installAgentContainer(AgentContainer agentContainer) {
		getDwarfDatabase().setAgentContainer(agentContainer);
	}

	public JFrame getWindow() {
		return dwarfVisualCenter;
	}

	public void setWindow(JFrame window) {
		this.dwarfVisualCenter = window;
	}

	public DwarfDatabase getDwarfDatabase() {
		return dwarfDatabase;
	}
}
