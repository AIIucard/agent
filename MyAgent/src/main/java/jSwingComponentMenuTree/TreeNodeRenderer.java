package main.java.jSwingComponentMenuTree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

public class TreeNodeRenderer implements TreeCellRenderer {
	transient protected Icon closedIcon;
	transient protected Icon openIcon;

	public TreeNodeRenderer() {
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		JComponent comp;
		if (node.getUserObject() instanceof String) {
			comp = new JLabel((String) node.getUserObject());
		} else {
			comp = (JComponent) node.getUserObject();
		}
		comp.setBackground(UIManager.getColor("Tree.textBackground"));

		return comp;
	}

	public void setClosedIcon(Icon newIcon) {
		closedIcon = newIcon;
	}

	public void setOpenIcon(Icon newIcon) {
		openIcon = newIcon;
	}
}