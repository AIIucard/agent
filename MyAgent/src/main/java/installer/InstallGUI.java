package main.java.installer;

import main.java.gui.DwarfVisualCenter;

public class InstallGUI {

	protected static DwarfVisualCenter dwarfVisualCenter;

	public static void main(String[] args) {
		// Start GUI
		System.out.println("Starting DwarfVisualCenter...");
		dwarfVisualCenter = DwarfVisualCenter.getInstance();
		dwarfVisualCenter.setVisible(true);
		System.out.println("DwarfVisualCenter started");

		// Start Agent
		System.out.println("Install agent with random name...");
		InstallAgent.installAgentWithRandomName(args);
		System.out.println("Agent installed");
	}
}
