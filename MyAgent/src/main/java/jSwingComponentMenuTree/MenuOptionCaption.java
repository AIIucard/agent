/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

public enum MenuOptionCaption {
	// TODO
	INSTALLATION("Installation"); //

	protected final String label;

	private MenuOptionCaption(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}