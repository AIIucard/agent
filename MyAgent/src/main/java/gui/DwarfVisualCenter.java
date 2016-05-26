package main.java.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JFrame;

import jade.core.Profile;
import jade.core.Runtime;
import jade.wrapper.AgentController;

public class DwarfVisualCenter extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private static DwarfVisualCenter instance;
	private Runtime runtime;
	private Profile profile;
	private HashMap<String, AgentController> agents;
	private boolean inRuntime;

	public static DwarfVisualCenter getInstance() {
		if (instance == null) {
			instance = new DwarfVisualCenter();
		}
		return instance;
	}

	@Override
	public void run() {
		inRuntime = true;
		// this.setExtendedState(this.getExtendedState() |
		// JFrame.MAXIMIZED_BOTH);
		this.setSize(800, 400);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				inRuntime = false;
			}
		});
		;
		agents = new HashMap<String, AgentController>();
		// while (inRuntime) {
		//
		// }
	}

	private DwarfVisualCenter() {
		run();
	}

	public Runtime getRuntime() {
		return runtime;
	}

	public Profile getProfile() {
		return profile;
	}

	public HashMap<String, AgentController> getAgents() {
		return agents;
	}

	public void installAgent(String name, AgentController agent) {
		agents.put(name, agent);
	}
}
