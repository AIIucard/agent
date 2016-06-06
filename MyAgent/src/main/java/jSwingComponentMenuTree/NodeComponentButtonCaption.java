/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

public enum NodeComponentButtonCaption {

	INSTALL_NEW_AGENT("Neuen Agenten installieren",
			"<html>Installiert einen neuen Agenten im Container<br> und startet diesen</html>",
			MenuOptionCaption.INSTALLATION);//

	protected final String buttonLabel;
	protected final String helpText;
	protected final MenuOptionCaption menuOptionNodeName;

	private NodeComponentButtonCaption(String buttonLabel, String helpText, MenuOptionCaption menuOptionNodeName) {
		this.buttonLabel = buttonLabel;
		this.helpText = helpText;
		this.menuOptionNodeName = menuOptionNodeName;
	}

	public String getButtonLabel() {
		return buttonLabel;
	}

	public String getHelpText() {
		return helpText;
	}

	public MenuOptionCaption getMenuOptionNodeName() {
		return menuOptionNodeName;
	}

}
