package main.java.installer;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import main.java.agent.SmallLittlePoisenDwarf;
import main.java.agent.SmallLittlePoisenDwarfWithGUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentInstaller {

	protected static SmallLittlePoisenDwarfWithGUI dwarfVisualCenter;
	protected static AgentController agentController;
	protected static AgentContainer container;
	protected static String agentName;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	public static void installAgentWithSpecificName(SmallLittlePoisenDwarfWithGUI owner, String name) {
		container = owner.getContainerController();
		try {
			if (name == null || name.equals("")) {

				agentName = "smallLittlePoisenDwarf" + (owner.getDwarfDatabase().getAgentCounter() + 1);
				owner.getDwarfDatabase().incrementAgentCounter();
			} else {
				agentName = name;
				owner.getDwarfDatabase().incrementAgentCounter();
			}
			agentController = container.createNewAgent(agentName, SmallLittlePoisenDwarf.class.getName(), null);
			agentController.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void installAgentWithRandomName(SmallLittlePoisenDwarfWithGUI owner) {
		installAgentWithSpecificName(owner, "");
	}
}
