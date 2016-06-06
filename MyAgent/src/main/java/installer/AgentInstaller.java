package main.java.installer;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
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

	public static void main(String args[]) {
		// PropertyConfigurator.configure("./src/main/java/cfg/log4j.properties");
		log.info("Install Agent with random name...");
		installAgentWithRandomName(args);
	}

	public static void installAgentWithSpecificName(String name, String[] args) {
		try {
			String host = "localhost";
			int port = -1; // default port = 1099
			String plattform = null; // default name
			boolean main = false; // 'normal' container

			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(host, port, plattform, main);

			container = runtime.createAgentContainer(profile);

			if (name == null || name.equals("")) {
				// agentName = "smallLittlePoisenDwarf" +
				// (dwarfVisualCenter.getAgents().size() + 1);
			} else {
				agentName = name;
			}
			agentController = container.createNewAgent(agentName, SmallLittlePoisenDwarf.class.getName(), args);
			agentController.start();

			// dwarfVisualCenter = SmallLittlePoisenDwarfWithGUI.getInstance();
			// dwarfVisualCenter.installAgent("smallLittlePoisenDwarf",
			// agentController);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void installAgentWithRandomName(String[] args) {
		installAgentWithSpecificName("", args);
	}
}
