/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

import javax.swing.tree.DefaultMutableTreeNode;

public class ComponentTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	// private String entityId;
	//
	// private String property;

	public ComponentTreeNode() {
		super(null);
	}

	public ComponentTreeNode(Object userObject) {
		super(userObject, true);
	}

	public ComponentTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}
	// public ComponentTreeNode(String entityId, String property) {
	// super(null);
	// setEntity(entityId);
	// setProperty(property);
	// }

	// public ComponentTreeNode(Object userObject, String entityId, String
	// property) {
	// super(userObject, true);
	// setEntity(entityId);
	// setProperty(property);
	// }
	//
	// public ComponentTreeNode(Object userObject, boolean allowsChildren,
	// String entityId, String property) {
	// super(userObject, allowsChildren);
	// setEntity(entityId);
	// setProperty(property);
	// }

	// public String getEntityId() {
	// return entityId;
	// }
	//
	// public void setEntity(String entityId) {
	// this.entityId = entityId;
	// }
	//
	// public String getProperty() {
	// return property;
	// }
	//
	// public void setProperty(String property) {
	// this.property = property;
	// }

}
