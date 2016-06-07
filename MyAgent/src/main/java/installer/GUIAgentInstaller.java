package main.java.installer;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import main.java.agent.SmallLittlePoisenDwarfWithGUI;

public class GUIAgentInstaller {

	protected static SmallLittlePoisenDwarfWithGUI dwarfVisualCenter;
	protected static AgentController agentController;
	protected static AgentContainer container;
	protected static String agentName;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {
		log.info("Install GUIAgent...");
		String host = JOptionPane.showInputDialog("Connection IP?");
		try {
			if (host.equals("")) {
				host = "192.168.1.233";
			} else if (host.equals("-1")) {
				host = "localhost";
			}
			int port = -1; // default port = 1099
			String plattform = null; // default name
			boolean main = false; // 'normal' container

			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(host, port, plattform, main);

			container = runtime.createAgentContainer(profile);

			if (args == null || args.length == 0 || args.equals("")) {
				log.info("Install GUIAgent with name smallLittlePoisenDwarfWithGUI.");
				agentName = "smallLittlePoisenDwarfWithGUI";
			} else {
				log.info("Install GUIAgent with name {}.", args[0]);
				agentName = args[0];
			}
			agentController = container.createNewAgent(agentName, SmallLittlePoisenDwarfWithGUI.class.getName(), args);
			agentController.start();
			log.info("GUIAgent installed.");
		} catch (Exception e) {
			log.error("GUIAgent installation exception: " + e.toString());
		}
	}
}