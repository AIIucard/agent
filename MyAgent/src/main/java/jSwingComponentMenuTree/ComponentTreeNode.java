/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

import javax.swing.tree.DefaultMutableTreeNode;

public class ComponentTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	public ComponentTreeNode() {
		super(null);
	}

	public ComponentTreeNode(Object userObject) {
		super(userObject, true);
	}

	public ComponentTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}
}
