package main.java.installer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import main.java.agent.LittlePoisenDwarf;
import main.java.agent.GUILittlePoisenDwarf;

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

				agentName = "smallLittlePoisenDwarf" + (owner.getDwarfDatabase().getAgentCounter() + 1);
				owner.getDwarfDatabase().incrementAgentCounter();
			} else {
				agentName = name;
				owner.getDwarfDatabase().incrementAgentCounter();
			}
			log.info("Install agent with name {}", agentName);
			agentController = container.createNewAgent(agentName, LittlePoisenDwarf.class.getName(), null);
			agentController.start();
			owner.getDwarfDatabase().installAgent(agentName, agentController);
			owner.updateGUI();
		} catch (Exception e) {
			log.error("Exception in install process with stacktrace {}", e.getStackTrace().toString());
		}
	}

	public static void installAgentWithRandomName(GUILittlePoisenDwarf owner) {
		installAgentWithSpecificName(owner, "");
	}
}
