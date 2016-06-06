
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

public class DwarfTreeCellEditor implements TreeCellEditor {

  public DwarfTreeCellEditor() {
  }

  @Override
  public void addCellEditorListener(CellEditorListener l) {
    // TODO
  }

  @Override
  public void cancelCellEditing() {
    // TODO
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
    //
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
