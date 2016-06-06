/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TreeButtonUtils {

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
