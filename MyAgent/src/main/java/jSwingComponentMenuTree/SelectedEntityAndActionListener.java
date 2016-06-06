/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

import java.awt.event.ActionListener;

public class SelectedEntityAndActionListener {
	private ActionListener actionListener;
	private String entityId;

	public SelectedEntityAndActionListener(ActionListener actionListener, String entityId) {
		this.actionListener = actionListener;
		this.entityId = entityId;
	}

	public ActionListener getActionListener() {
		return actionListener;
	}

	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

}
