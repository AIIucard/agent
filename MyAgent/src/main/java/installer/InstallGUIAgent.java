package main.java.installer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import main.java.agent.SmallLittlePoisenDwarfWithGUI;

public class InstallGUIAgent {

	protected static SmallLittlePoisenDwarfWithGUI dwarfVisualCenter;
	protected static AgentController agentController;
	protected static AgentContainer container;
	protected static String agentName;

	private static Logger log = Logger.getLogger(InstallGUIAgent.class.getName());

	public static void main(String[] args) {
		PropertyConfigurator.configure("./src/main/java/cfg/log4j.properties");
		log.info("Install gui agent...");
		try {
			String host = "localhost";
			int port = -1; // default port = 1099
			String plattform = null; // default name
			boolean main = false; // 'normal' container

			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(host, port, plattform, main);

			container = runtime.createAgentContainer(profile);

			if (args == null || args.length == 0 || args.equals("")) {
				agentName = "smallLittlePoisenDwarfWithGUI";
			} else {
				agentName = args[0];
			}
			agentController = container.createNewAgent(agentName, SmallLittlePoisenDwarfWithGUI.class.getName(), args);
			agentController.start();
		} catch (Exception e) {
			log.error("Install exception: " + e.toString());
		}
	}
}
