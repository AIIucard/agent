package main.java.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.agent.SmallLittlePoisenDwarfWithGUI;
import main.java.jSwingComponentMenuTree.ComponentTreeNode;
import main.java.jSwingComponentMenuTree.DwarfTreeCellEditor;
import main.java.jSwingComponentMenuTree.MenuOptionCaption;
import main.java.jSwingComponentMenuTree.NodeComponentButtonCaption;
import main.java.jSwingComponentMenuTree.TreeButtonUtils;
import main.java.jSwingComponentMenuTree.TreeNodeRenderer;
import main.java.map.MapEditor;
import main.java.utils.DwarfUtils;

public class DwarfVisualCenter extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane;
	private JTable agentsTable;
	DefaultTableModel agentsTableModel;
	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
	private JTree agentSettingsTree;
	private SmallLittlePoisenDwarfWithGUI owner;

	// Tab names
	private static final String AGENT_SETTINGS_TAB = "Agenten Einstellungen";
	private static final String AGENT_TABLE_TAB = "Agenten Verwaltung";

	// Tree root
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
		// TODO
		JComponent temp = new MapEditor(owner);

		return temp;

	}

	private JTabbedPane createSettingsPanel() {
		JTabbedPane pane = new JTabbedPane();
		pane.addTab(AGENT_SETTINGS_TAB, createAgentSettingsTree());
		// TODO
		return pane;
	}

	private JComponent createAgentSettingsTree() {
		// ##### Tree root
		DefaultMutableTreeNode agentSettingsTreeRootNode = new DefaultMutableTreeNode(
				agentSettingsTreeRootNodeLabelEditor);

		// ##### MenuOption Installation start
		DefaultMutableTreeNode menuOptionNode = new DefaultMutableTreeNode(MenuOptionCaption.INSTALLATION.getLabel());

		// ----- Install new Agent
		JButton newAgentButton = new JButton(NodeComponentButtonCaption.INSTALL_NEW_AGENT.getButtonLabel());
		newAgentButton.addActionListener(TreeButtonUtils.createInstallationAction(owner));
		ComponentTreeNode propertiesNode = new ComponentTreeNode(newAgentButton);
		menuOptionNode.add(propertiesNode);

		agentSettingsTreeRootNode.add(menuOptionNode);
		// ##### MenuOption Installation end

		// ##### Create tree
		DefaultMutableTreeNode agentSettingsTreeRootNodeWithButtons = createSettinButtonNodes(
				agentSettingsTreeRootNode);
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

	public void updateTabsPanel() {
		log.info("Update tab panel...");
		int agentManagementTabPlace = -1;
		for (int i = 0; i < tabbedPane.getTabCount(); ++i) {
			String label = tabbedPane.getTitleAt(i);
			if (label.equals(AGENT_TABLE_TAB)) {
				agentManagementTabPlace = i;
			}
		}
		if ((owner.getDwarfDatabase().getAgents().size() == 0) && (agentManagementTabPlace != -1))
			tabbedPane.remove(agentManagementTabPlace);
		else if ((owner.getDwarfDatabase().getAgents().size() != 0) && agentManagementTabPlace == -1) {
			agentsTableModel = DwarfUtils.toTableModel(owner.getDwarfDatabase().getAgents());
			agentsTable = new JTable(agentsTableModel);
			tabbedPane.addTab(AGENT_TABLE_TAB, agentsTable);
		} else if ((owner.getDwarfDatabase().getAgents().size() != 0) && agentManagementTabPlace != -1) {
			// TODO UPdate Table dosent work
			agentsTableModel = DwarfUtils.toTableModel(owner.getDwarfDatabase().getAgents());
			agentsTableModel.fireTableDataChanged();
			agentsTable.repaint();
			agentsTable.revalidate();
		}
		tabbedPane.repaint();
		tabbedPane.revalidate();
		log.info("Update tab panel finished.");
	}
}
