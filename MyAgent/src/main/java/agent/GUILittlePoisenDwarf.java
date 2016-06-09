package main.java.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import main.java.gui.DwarfDatabase;
import main.java.gui.DwarfVisualCenter;

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
		log.info("GUIAgent started.");
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
		} catch (StaleProxyException e) {
			log.error("Error in shut down process {}", e.getStackTrace().toString());
		}
	}

	public void updateGUI() {
		dwarfVisualCenter.updateTabsPanel();
	}

	public void installAgentContainer(AgentContainer agentContainer) {
		getDwarfDatabase().setAgentContainer(agentContainer);
	}

	public DwarfDatabase getDwarfDatabase() {
		return dwarfDatabase;
	}
}
