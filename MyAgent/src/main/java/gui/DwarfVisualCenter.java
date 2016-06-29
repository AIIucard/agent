package main.java.gui;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.agent.GUILittlePoisenDwarf;
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
	private GUILittlePoisenDwarf owner;
	private MapEditor editor;

	// Tab names
	private static final String AGENT_SETTINGS_TAB = "Agenten Einstellungen";
	private static final String AGENT_TABLE_TAB = "Agenten Verwaltung";

	// Tree root
	private static final String agentSettingsTreeRootNodeLabelEditor = "Agenten";

	public DwarfVisualCenter(GUILittlePoisenDwarf owner, int col, int row) {
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
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, createMapEditor(col, row));
		splitPane.setDividerLocation(300);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		log.info("Creation of dwarf visual center finished.");
		setVisible(true);
	}

	private void killGUIAgent() {
		owner.shutDownAgent();
	}

	private JComponent createMapEditor(int col, int row) {
		editor = new MapEditor(owner, col, row);
		editor.setAutoscrolls(true);
		// Drag and Drop
		editor.addMouseListener(new MouseAdapter() {

			private Point origin;

			@Override
			public void mousePressed(MouseEvent e) {
				origin = new Point(e.getPoint());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (origin != null) {
					JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, editor);
					if (viewPort != null) {
						int deltaX = origin.x - e.getX();
						int deltaY = origin.y - e.getY();

						Rectangle view = viewPort.getViewRect();
						view.x += deltaX;
						view.y += deltaY;

						editor.scrollRectToVisible(view);
					}
				}
			}

		});
		JScrollPane viewPortWithScrollbars = new JScrollPane();
		viewPortWithScrollbars.setViewportView(editor);
		viewPortWithScrollbars.getViewport().setViewPosition(new Point(0, 0));
		return viewPortWithScrollbars;
	}

	private JTabbedPane createSettingsPanel() {
		JTabbedPane pane = new JTabbedPane();
		pane.addTab(AGENT_SETTINGS_TAB, createAgentSettingsTree());
		// TODO
		return pane;
	}

	private JComponent createAgentSettingsTree() {
		// ##### Tree root
		DefaultMutableTreeNode agentSettingsTreeRootNode = new DefaultMutableTreeNode(agentSettingsTreeRootNodeLabelEditor);

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

	// TODO remove after work is finished
	public static void showOnScreen(int screen, JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (screen > -1 && screen < gd.length) {
			frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, gd[screen].getDefaultConfiguration().getBounds().y + frame.getY());
		} else if (gd.length > 0) {
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, gd[0].getDefaultConfiguration().getBounds().y + frame.getY());
		} else {
			throw new RuntimeException("No Screens Found");
		}
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
		if ((owner.getDwarfDatabase().getDwarfs().size() == 0) && (agentManagementTabPlace != -1))
			tabbedPane.remove(agentManagementTabPlace);
		else if ((owner.getDwarfDatabase().getDwarfs().size() != 0) && agentManagementTabPlace == -1) {
			agentsTableModel = DwarfUtils.toTableModel(owner.getDwarfDatabase().getDwarfs());
			agentsTable = new JTable(agentsTableModel);
			tabbedPane.addTab(AGENT_TABLE_TAB, agentsTable);
		} else if ((owner.getDwarfDatabase().getDwarfs().size() != 0) && agentManagementTabPlace != -1 && agentsTableModel != null) {
			// TODO UPdate Table dosent work
			agentsTableModel.fireTableDataChanged();
			agentsTable.repaint();
			agentsTable.revalidate();
		}
		tabbedPane.repaint();
		tabbedPane.revalidate();
		log.info("Update tab panel finished.");
	}

	public void repaintMap() {
		log.info("Repaint map...");
		editor.updateMap();
	}

	public MapEditor getEditor() {
		return editor;
	}
}
