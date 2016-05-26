package main.java.installer;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import main.java.agent.SmallLittlePoisenDwarf;
import main.java.gui.DwarfVisualCenter;

public class InstallAgent {

	protected static DwarfVisualCenter dwarfVisualCenter;
	protected static AgentController agentController;
	protected static AgentContainer container;
	protected static String agentName;

	public static void main(String args[]) {
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

			dwarfVisualCenter = DwarfVisualCenter.getInstance();

			if (name == null || name.equals("")) {
				agentName = "smallLittlePoisenDwarf" + (dwarfVisualCenter.getAgents().size() + 1);
			} else {
				agentName = name;
			}
			agentController = container.createNewAgent(agentName, SmallLittlePoisenDwarf.class.getName(), args);

			agentController.start();

			dwarfVisualCenter = DwarfVisualCenter.getInstance();
			dwarfVisualCenter.installAgent("smallLittlePoisenDwarf", agentController);
			dwarfVisualCenter.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void installAgentWithRandomName(String[] args) {
		installAgentWithSpecificName("", args);
	}
}
