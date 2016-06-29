package main.java.installer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import main.java.DwarfConstants;
import main.java.agent.GUILittlePoisenDwarf;
import main.java.agent.LittlePoisenDwarf;

public class AgentInstaller {

	protected static GUILittlePoisenDwarf dwarfVisualCenter;
	protected static AgentController agentController;
	protected static AgentContainer container;
	protected static String agentName;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	public static void installAgentWithSpecificName(GUILittlePoisenDwarf owner, String name) {
		container = owner.getContainerController();
		try {
			if (name == null || name.equals("")) {

				agentName = DwarfConstants.AGENT_NAME + (owner.getDwarfDatabase().getDwarfCounter() + 1);
				owner.getDwarfDatabase().incrementDwarfCounter();
			} else {
				agentName = name;
				owner.getDwarfDatabase().incrementDwarfCounter();
			}
			log.info("Install agent with name {}", agentName);
			agentController = container.createNewAgent(agentName, LittlePoisenDwarf.class.getName(), null);
			agentController.start();
			owner.getDwarfDatabase().recruitDwarf(agentName, agentController);
			owner.updateGUI();
		} catch (Exception ex) {
			log.error("Exception in install process!", ex);
		}
	}

	public static void installAgentWithRandomName(GUILittlePoisenDwarf owner) {
		installAgentWithSpecificName(owner, "");
	}
}
