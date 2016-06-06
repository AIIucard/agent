package main.java.jSwingComponentMenuTree;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
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

	// public static JPanel
	// createJScorllPaneWithVariableNumberOfJTextfields(Class<PojoListProperty>
	// clazz,
	// String caption, PojoListProperty<?> propertyData) {
	// return createJScorllPaneWithVariableNumberOfJTextfields(clazz, caption,
	// 375, 80, propertyData);
	// }
	//
	// public static JPanel
	// createJScorllPaneWithVariableNumberOfJTextfields(Class<PojoListProperty>
	// clazz,
	// String caption, int textWidth, int textHeight, PojoListProperty<?>
	// propertyData) {
	// JLabel propertyLabel = new JLabel(caption);
	// propertyLabel.setPreferredSize(new Dimension(125, 20));
	// JPanel panelWithScrollPane = new JPanel();
	// table = new JTable();
	// if (clazz.isAssignableFrom(PojoListProperty.class)) {
	// propertyNameList = propertyData.getFieldNames();
	// if (propertyNameList != null) {
	// List<?> propertyDataList = propertyData.getList();
	// if (propertyDataList != null) {
	// Object[][] propertyValues = new
	// Object[propertyDataList.size()][propertyNameList.length];
	// for (int i = 0; i < propertyDataList.size(); i++) {
	// for (int j = 0; j < propertyNameList.length; j++) {
	// Class propertyClass = propertyDataList.get(i).getClass();
	// try {
	// Field field = propertyClass.getDeclaredField(propertyNameList[j]);
	// field.setAccessible(true);
	// propertyValues[i][j] = field.get(propertyDataList.get(i)).toString();
	// field.setAccessible(false);
	// } catch (NoSuchFieldException e) {
	// System.out.println("Unexpected Exception: " + e);
	// } catch (SecurityException e) {
	// System.out.println("Unexpected Exception: " + e);
	// } catch (IllegalArgumentException e) {
	// System.out.println("Unexpected Exception: " + e);
	// } catch (IllegalAccessException e) {
	// System.out.println("Unexpected Exception: " + e);
	// }
	// }
	// }
	// table = new JTable(new DefaultTableModel(propertyValues,
	// propertyNameList));
	// scrollPane = new JScrollPane(table);
	// } else {
	// table = new JTable(new Object[][] { { "", "", "" } }, propertyNameList);
	// scrollPane = new JScrollPane(table);
	// }
	// } else {
	// JOptionPane.showMessageDialog(null,
	// "Fehler! Spaltennamen wurde nicht gefunden!", "Fehler",
	// JOptionPane.ERROR_MESSAGE);
	// scrollPane = new JScrollPane();
	// }
	// } else {
	// JOptionPane.showMessageDialog(null,
	// "Fehler! Konfigurationstyp wird nicht unterstüzt!", "Fehler",
	// JOptionPane.ERROR_MESSAGE);
	// scrollPane = new JScrollPane();
	// }
	// scrollPane.setPreferredSize(new Dimension(textWidth, textHeight));
	// panelWithScrollPane.setLayout(new BorderLayout());
	// panelWithScrollPane.add(propertyLabel, BorderLayout.WEST);
	// panelWithScrollPane.add(scrollPane, BorderLayout.CENTER);
	//
	// JButton addButton = new JButton("Zeile hinzufügen");
	// addButton.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// addNewRow(table, propertyNameList);
	// }
	// });
	// JButton deleteButton = new JButton("Zeile löschen");
	// deleteButton.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	//
	// deleteRow(table);
	// }
	// });
	// JPanel buttonPanel = new JPanel();
	// buttonPanel.setLayout(new BorderLayout());
	// buttonPanel.add(addButton, BorderLayout.NORTH);
	// buttonPanel.add(deleteButton, BorderLayout.SOUTH);
	// panelWithScrollPane.add(buttonPanel, BorderLayout.EAST);
	// return panelWithScrollPane;
	// }

	protected static void addNewRow(JTable _table, String[] _propertyNameList) {
		DefaultTableModel model = (DefaultTableModel) _table.getModel();
		Object[] obj = new Object[_propertyNameList.length];
		model.addRow(obj);
		_table = new JTable(model);
		table.repaint();
		scrollPane.repaint();
	}

	protected static void deleteRow(JTable _table) {
		DefaultTableModel model = (DefaultTableModel) _table.getModel();
		if (table.getSelectedRow() != -1) {
			model.removeRow(table.getSelectedRow());
			_table = new JTable(model);
		} else {
			JOptionPane.showMessageDialog(null, "Fehler! Keine Zeile ausgewählt!", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		table.repaint();
		scrollPane.repaint();
	}
}
