/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.agent.KingLittlePoisenDwarf;
import main.java.installer.AgentInstaller;

public class TreeButtonUtils {

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	public static ActionListener createInstallationAction(KingLittlePoisenDwarf owner) {
		ActionListener action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				log.info("Installing new agent with random name...");
				AgentInstaller.installAgentWithRandomName(owner);
			}
		};
		return action;
	}

}
