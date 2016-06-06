/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.java.agent.SmallLittlePoisenDwarfWithGUI;
import main.java.installer.AgentInstaller;

public class TreeButtonUtils {

	public static ActionListener createInstallationAction(SmallLittlePoisenDwarfWithGUI owner) {
		ActionListener action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AgentInstaller.installAgentWithRandomName(owner);
			}
		};
		return action;
	}

}
