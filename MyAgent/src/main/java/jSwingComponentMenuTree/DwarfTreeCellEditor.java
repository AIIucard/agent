
package main.java.jSwingComponentMenuTree;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

/**
 * @author Maximilian Weidemann
 * 
 *         Editor for JComponent Tree in Menu
 *
 */
public class DwarfTreeCellEditor implements TreeCellEditor {

	public DwarfTreeCellEditor() {
		// No function
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// No function
	}

	@Override
	public void cancelCellEditing() {
		// No function
	}

	@Override
	public Object getCellEditorValue() {
		return this;
	}

	@Override
	public boolean isCellEditable(EventObject evt) {
		if (evt instanceof MouseEvent) {
			MouseEvent mevt = (MouseEvent) evt;

			if (mevt.getClickCount() == 1) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// No function
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		return false;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.getUserObject() instanceof JComponent) {
			JComponent comp = (JComponent) node.getUserObject();
			return comp;
		} else {
			return new JLabel((String) node.getUserObject());
		}
	}
}
