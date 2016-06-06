package main.java.jSwingComponentMenuTree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

public class TreeUtils {

	private static JTable table;
	private static JScrollPane scrollPane;

	public static ArrayList<ComponentTreeNode> getRootListFromTree(JTree tree) {
		ArrayList<ComponentTreeNode> list = new ArrayList<ComponentTreeNode>();
		TreeModel model = tree.getModel();
		if (model != null) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			ArrayList<Object> nodeList = new ArrayList<Object>();
			nodeList.add(root);
			return getNodes(model, nodeList, list);
		} else {
			return null;
		}
	}

	protected static ArrayList<ComponentTreeNode> getNodes(TreeModel model, ArrayList<Object> nodeList,
			ArrayList<ComponentTreeNode> list) {
		if (nodeList.isEmpty()) {
			return list;
		}
		if (nodeList.get(0) instanceof ComponentTreeNode) {
			list.add((ComponentTreeNode) nodeList.get(0));
			nodeList.remove(0);
			return getNodes(model, nodeList, list);
		} else {
			int childNumber = model.getChildCount(nodeList.get(0));
			for (int i = 0; i < childNumber; i++) {
				nodeList.add(model.getChild(nodeList.get(0), i));
			}
			nodeList.remove(0);
			return getNodes(model, nodeList, list);
		}
	}

	public static JPanel createLabelledComponent(String caption, JComponent component) {
		return createLabelledComponent(caption, component, 125, 125);
	}

	public static JPanel createLabelledComponent(String caption, JComponent component, int labelWidth, int textWidth) {
		JLabel label = new JLabel(caption);
		label.setPreferredSize(new Dimension(labelWidth, 20));
		component.setPreferredSize(new Dimension(textWidth, 20));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		panel.add(component, BorderLayout.EAST);
		return panel;
	}

	public static JPanel createLabelledComponent(String caption, JComponent component, int labelWidth, int textHeight,
			int textWidth) {
		JLabel label = new JLabel(caption);
		label.setPreferredSize(new Dimension(labelWidth, 20));
		component.setPreferredSize(new Dimension(textWidth, textHeight));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		panel.add(component, BorderLayout.EAST);
		return panel;
	}

	public static JPanel createLabelledScrollableComponent(String caption, JComponent component, int labelWidth,
			int textHeight, int textWidth) {
		JLabel label = new JLabel(caption);
		label.setPreferredSize(new Dimension(labelWidth, 20));
		JPanel panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(textWidth, textHeight));
		scrollPane.getViewport().setView(component);
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		panel.add(scrollPane, BorderLayout.EAST);
		return panel;
	}
}
