package main.java.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import main.java.agent.SmallLittlePoisenDwarfWithGUI;
import main.java.jSwingComponentMenuTree.ComponentTreeNode;
import main.java.jSwingComponentMenuTree.DwarfTreeCellEditor;
import main.java.jSwingComponentMenuTree.TreeButtonUtils;
import main.java.jSwingComponentMenuTree.TreeNodeRenderer;
import main.java.map.MapEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DwarfVisualCenter extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane;
	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
	private JTree agentSettingsTree;
	private SmallLittlePoisenDwarfWithGUI owner;
	private static final String agentSettingsTreeRootNodeLabelEditor = "Agenten";

	public DwarfVisualCenter(SmallLittlePoisenDwarfWithGUI owner) {
		this.owner = owner;
		setTitle("DwarfVisualCenter");
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				killGUIAgent();
			}
		});

		log.info("Create settings panel...");
		tabbedPane = createSettingsPanel();

		log.info("Create map editor...");
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, createMapEditor());
		splitPane.setDividerLocation(300);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		log.info("Creation of dwarf visual center finished.");
		setVisible(true);
	}

	private void killGUIAgent() {
		owner.shutDownAgent();
	}

	private JComponent createMapEditor() {
		JComponent temp = new MapEditor();
		// TODO
		return temp;

	}

	private JTabbedPane createSettingsPanel() {
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("Agenten Einstellungen", createAgentSettingsTree());
		// TODO
		return pane;
	}

	private JComponent createAgentSettingsTree() {
		JButton save = new JButton("Übernehmen");
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (onCheck()) {
					onAccept(agentSettingsTree);
				}
			}

		});
		DefaultMutableTreeNode agentSettingsTreeRootNode = new DefaultMutableTreeNode(
				TreeButtonUtils.createLabelledComponent(agentSettingsTreeRootNodeLabelEditor, save, 100, 150));
		DefaultMutableTreeNode menuOptionNode = new DefaultMutableTreeNode("Test 1");
		ComponentTreeNode propertiesNode = new ComponentTreeNode(TreeButtonUtils.createLabelledComponent("TestButton1",
				new JButton("Temp")), false);
		menuOptionNode.add(propertiesNode);
		agentSettingsTreeRootNode.add(menuOptionNode);

		DefaultMutableTreeNode agentSettingsTreeRootNodeWithButtons = createSettinButtonNodes(agentSettingsTreeRootNode);
		agentSettingsTree = new JTree(agentSettingsTreeRootNodeWithButtons);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		if (agentSettingsTree != null) {
			panel.add(agentSettingsTree, BorderLayout.CENTER);
			TreeNodeRenderer tr = new TreeNodeRenderer();
			DwarfTreeCellEditor te = new DwarfTreeCellEditor();
			agentSettingsTree.setEditable(true);
			agentSettingsTree.setCellRenderer(tr);
			agentSettingsTree.setCellEditor(te);
		}
		JScrollPane treeView = new JScrollPane(panel);
		return treeView;
	}

	private DefaultMutableTreeNode createSettinButtonNodes(DefaultMutableTreeNode palletTreeRootNode) {
		DefaultMutableTreeNode palletTreeRootNodeWithButtons = palletTreeRootNode;

		return palletTreeRootNodeWithButtons;
	}

	public boolean onCheck() {
		return true;
	}

	public void onAccept(JTree tree) {
		// if (tree != null) {
		// ArrayList<ComponentTreeNode> nodeList =
		// TreeUtils.getRootListFromTree(tree);
		// for (int i = 0; i < nodeList.size(); i++) {
		// ComponentTreeNode node = nodeList.get(i);
		// if (node.getUserObject() instanceof JPanel) {
		// JPanel panel = (JPanel) (node.getUserObject());
		// Component[] container = panel.getComponents();
		// for (int j = 0; j < container.length; j++) {
		// if (core.getEntityById(node.getEntityId()) != null) {
		// Entity propertyEntity = core.getEntityById(node.getEntityId());
		// if (propertyEntity.getProperty(node.getProperty()) != null) {
		// Property property = propertyEntity.getProperty(node.getProperty());
		// if (container[j] instanceof JTextField) {
		// JTextField textfield = (JTextField) container[j];
		// if (!property.check(textfield.getText())) {
		// JOptionPane.showMessageDialog(null, property.errorMessage(),
		// "Fehler",
		// JOptionPane.ERROR_MESSAGE);
		// } else {
		// if (draegerEntity.isInEditorMode()) {
		// CoreUtils.changeProperty(propertyEntity, property.getKey(),
		// textfield.getText());
		// propertyEntity.onPropertiesChanged();
		// propertyEntity.onReset();
		// System.out.println("Propertie " + property.getKey() + " for Entity "
		// + propertyEntity.getId() + " changed");
		// } else if (draegerEntity.isInAnimationMode()) {
		// synchronized (core.getMutex()) {
		// //
		// }
		// } else {
		// // AntSim error
		// }
		// }
		// } else if (container[j] instanceof JScrollPane) {
		// if (draegerEntity.isInEditorMode()) {
		// if (property instanceof PojoListProperty<?>) {
		// PojoListProperty<?> listProperty = (PojoListProperty<?>) property;
		// JScrollPane jScroll = (JScrollPane) container[j];
		// if (jScroll.getViewport().getView() instanceof JTable) {
		// JTable table = (JTable) jScroll.getViewport().getView();
		// int row = table.getSelectedRow();
		// int col = table.getSelectedColumn();
		// if (row != -1 && col != -1) {
		// TableCellEditor editor = table.getCellEditor(row, col);
		// if (editor != null) {
		// editor.stopCellEditing();
		// }
		// }
		// StringBuilder builder = new StringBuilder();
		// for (int k = 0; k < table.getRowCount(); ++k) {
		// for (int l = 0; l < table.getColumnCount(); ++l) {
		// builder.append(table.getValueAt(k, l).toString());
		// builder.append(listProperty.getFieldDelimiter());
		// }
		// builder.append(listProperty.getElementDelimiter());
		// }
		// CoreUtils.changeProperty(propertyEntity, listProperty.getKey(),
		// builder.toString());
		// propertyEntity.onPropertiesChanged();
		// propertyEntity.onReset();
		// System.out.println("Propertie " + property.getKey() + " for Entity "
		// + propertyEntity.getId() + " changed");
		// } else {
		// JOptionPane.showMessageDialog(null,
		// "Error! Property " + node.getProperty() + " from Entity "
		// + node.getEntityId() + " not changed!",
		// "Error Massage", JOptionPane.ERROR_MESSAGE);
		// }
		// } else {
		// JOptionPane
		// .showMessageDialog(
		// null,
		// "Error! Property "
		// + node.getProperty()
		// + " from Entity "
		// + node.getEntityId()
		// +
		// " not changed! Viewport in JScrollPane can not be used! JTable required...",
		// "Error Massage", JOptionPane.ERROR_MESSAGE);
		// }
		// } else if (draegerEntity.isInAnimationMode()) {
		// synchronized (core.getMutex()) {
		// // TODO
		// }
		// } else {
		// // AntSim error
		// }
		// } else if (container[j] instanceof JCheckBox) {
		// JCheckBox checkBox = (JCheckBox) container[j];
		// if (draegerEntity.isInEditorMode()) {
		// CoreUtils.changeProperty(propertyEntity, property.getKey(),
		// checkBox.isSelected() ? "true" : "false");
		// propertyEntity.onPropertiesChanged();
		// propertyEntity.onReset();
		// System.out.println("Propertie " + property.getKey() + " for Entity "
		// + propertyEntity.getId() + " changed");
		// } else if (draegerEntity.isInAnimationMode()) {
		// synchronized (core.getMutex()) {
		// // TODO
		// }
		// } else {
		// // AntSim error
		// }
		//
		// }
		// } else {
		// JOptionPane.showMessageDialog(null, "Property" + node.getProperty() +
		// " from Entity "
		// + node.getEntityId() + " not found!", "Error Massage",
		// JOptionPane.ERROR_MESSAGE);
		// }
		// } else {
		// JOptionPane.showMessageDialog(null, "Entity" + node.getEntityId() +
		// " from property "
		// + node.getProperty() + " not found!", "Error Massage",
		// JOptionPane.ERROR_MESSAGE);
		// }
		// }
		// } else {
		// // No JPanel
		// }
		// }
		// Editor.getCurrentInstance().makeDirty();
		// } else {
		// JOptionPane.showMessageDialog(null, "Error! Tree not created...",
		// "Error Massage",
		// JOptionPane.ERROR_MESSAGE);
		// }
	}
}
