/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

public class ButtonUtilsDraeger {
	// static JTextField idInput;
	// private static String selectedSRM;
	// static JLabel typeInput;
	// static JLabel widthInput;
	// static JLabel lengthInput;
	// static JLabel heightInput;
	// static JLabel weightInput;
	// private static JButton helpButton;
	//
	// public static JPanel addHelpTextPanel(Component userComponent, String
	// helpText) {
	// JPanel panel = new JPanel();
	// panel.setLayout(new BorderLayout());
	// panel.add(userComponent, BorderLayout.CENTER);
	// String packagepath = "/com/ssn/simulation/img/";
	// if (helpText != null) {
	// ImageIcon imageIcon = new
	// ImageIcon(DialogUtils.class.getResource(packagepath + "InfoIcon.png"));
	// helpButton = new JButton(imageIcon);
	// helpButton.setBorder(BorderFactory.createEmptyBorder());
	// helpButton.setContentAreaFilled(false);
	// ToolTipManager.sharedInstance().setInitialDelay(0);
	// helpButton.setToolTipText(helpText);
	// helpButton.addFocusListener(new FocusAdapter() {
	// @Override
	// public void focusGained(FocusEvent e) {
	// try {
	// KeyEvent ke = new KeyEvent(e.getComponent(), KeyEvent.KEY_PRESSED,
	// System.currentTimeMillis(), InputEvent.CTRL_MASK, KeyEvent.VK_F1,
	// KeyEvent.CHAR_UNDEFINED);
	// e.getComponent().dispatchEvent(ke);
	// } catch (Throwable e1) {
	// e1.printStackTrace();
	// }
	// }
	// });
	// panel.add(helpButton, BorderLayout.EAST);
	// }
	// return panel;
	// }
	//
	// public static SelectedEntityAndActionListener setRGBSettingsAction(JFrame
	// owner, List<? extends AbstractSRM2> srmList) {
	//
	// ActionListener action = new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// String[] srmArray = new String[srmList.size()];
	//
	// for (int i = 0; i < srmList.size(); ++i) {
	// srmArray[i] = srmList.get(i).getId();
	// }
	// JFrame frame = new JFrame("RGB Auswählen");
	// selectedSRM = (String) JOptionPane.showInputDialog(frame,
	// "Bitte RGB auswählen", "RGB Auswählen", JOptionPane.QUESTION_MESSAGE,
	// null, srmArray, srmArray[0]);
	// if (selectedSRM != null) {
	// AbstractSRM2 srm = null;
	// for (AbstractSRM2 temp : srmList) {
	// if (temp.getId().equals(selectedSRM)) {
	// srm = temp;
	// break;
	// }
	// }
	// if (srm != null) {
	// setRGBSettingsAction(owner, srm);
	// } else {
	// JOptionPane.showMessageDialog(null, "SRM:  " + selectedSRM +
	// " wurde nicht gefunden! ", "InfoBox: " + "Fehler",
	// JOptionPane.ERROR_MESSAGE);
	// }
	// }
	// }
	// };
	// return new SelectedEntityAndActionListener(action, selectedSRM);
	// }
	//
	// public static void setRGBSettingsAction(JFrame owner, AbstractSRM2 srm) {
	//
	// if (srm instanceof PalletSRM2 || srm instanceof BinSRM2) {
	//
	// JDialog dialog = createSettingsActionWindow("RGB Einstellungen " +
	// srm.getAliasId(), owner, 420, 380);
	//
	// JComboBox<String> customerError = new JComboBox<String>(new String[] {
	// "", "Fach voll" });
	// JTextField errorItem = new JTextField(".*");
	// JComboBox<String> errorOrder = new JComboBox<String>(new String[] {
	// "GET", "PUT" });
	// JCheckBox errorRack = new JCheckBox("");
	// errorRack.setBorder(BorderFactory.createEmptyBorder());
	// errorRack.setSelected(true);
	// JTextField errorCode = new JTextField("");
	// JCheckBox errorExecute = new JCheckBox("");
	// errorExecute.setBorder(BorderFactory.createEmptyBorder());
	// JCheckBox errorReset = new JCheckBox("");
	// errorReset.setSelected(true);
	// errorReset.setBorder(BorderFactory.createEmptyBorder());
	// JButton applyButton = DialogUtils.createButton("Hinzufügen", new
	// ActionListener() {
	// @Override
	// public synchronized void actionPerformed(ActionEvent ae) {
	// final AbstractSRMError error = new AbstractSRMError();
	// error.setItem(errorItem.getText());
	// error.setOrder((String) errorOrder.getSelectedItem());
	// error.setRack(errorRack.isSelected());
	// error.setCode(IntegerProperty.parseInteger(errorCode.getText(), 0));
	// error.setExecute(errorExecute.isSelected());
	// error.setReset(errorReset.isSelected());
	// error.setTuType(IntegerProperty.parseInteger("-1", -1));
	// error.setTuLength(IntegerProperty.parseInteger("-1", -1));
	// error.setTuWidth(IntegerProperty.parseInteger("-1", -1));
	// error.setTuHeight(IntegerProperty.parseInteger("-1", -1));
	// error.setTuWeight(IntegerProperty.parseInteger("-1", -1));
	// if (error.getCode() > 0) {
	// srm.getCore().addExternalEvent(new Runnable() {
	// @Override
	// public void run() {
	// srm.getForcedErrors().add(error);
	// }
	// });
	// } else {
	// srm.getCore().logError(srm,
	// "unable to create forced error, invalid error code");
	// }
	// }
	// });
	// customerError.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// if (customerError.getSelectedItem().equals("Fach voll")) {
	// errorItem.setText(".*");
	// errorOrder.setSelectedItem("PUT");
	// errorRack.setSelected(true);
	// errorCode.setText("14211");
	// errorExecute.setSelected(false);
	// errorReset.setSelected(true);
	// }
	// }
	// });
	//
	// JPanel errorPanel = new JPanel();
	// errorPanel.setLayout(new GridLayout(8, 1, 5, 5));
	// errorPanel.add(DialogUtils.createLabelledComponent("Benutzerdefinierte Fehler:",
	// customerError, 170, 125));
	// errorPanel.add(DialogUtils.createLabelledComponent("Item-ID:", errorItem,
	// 170, 125));
	// errorPanel.add(DialogUtils.createLabelledComponent("Auftragstyp:",
	// errorOrder, 170, 125));
	// errorPanel.add(DialogUtils.createLabelledComponent("Regal Fehler:",
	// errorRack, 170, 125));
	// errorPanel.add(DialogUtils.createLabelledComponent("Fehlercode:",
	// errorCode, 170, 125));
	// errorPanel.add(DialogUtils.createLabelledComponent("Auftrag trotzdem ausführen:",
	// errorExecute, 170, 125));
	// errorPanel.add(DialogUtils.createLabelledComponent("Fehler zurücksetzen:",
	// errorReset, 170, 125));
	// errorPanel.add(applyButton, BorderLayout.SOUTH);
	//
	// dialog.getContentPane().add(errorPanel, BorderLayout.CENTER);
	// dialog.setVisible(true);
	// } else {
	// JOptionPane.showMessageDialog(null, "Das RGB: " + srm.getId() +
	// " mit dem Typ: " + srm.getClass().getName() + " wird nicht unterstüzt! ",
	// "InfoBox: " + "Fehler", JOptionPane.ERROR_MESSAGE);
	// }
	// }
	//
	//
	//
	// public static ActionListener giveUpAction(Core core, Entity entity) {
	// ActionListener action = new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// if (entity instanceof PIG01SP11PalletInfeedConveyor) {
	// PIG01SP11PalletInfeedConveyor conveyor = (PIG01SP11PalletInfeedConveyor)
	// entity;
	// synchronized (core.getMutex()) {
	// conveyor.giveup();
	// }
	// } else if (entity instanceof CS51FP018PL077BinNOKPointConveyor) {
	// CS51FP018PL077BinNOKPointConveyor conveyor =
	// (CS51FP018PL077BinNOKPointConveyor) entity;
	// synchronized (core.getMutex()) {
	// conveyor.giveup();
	// }
	// } else if (entity instanceof BPG0x00JM0122BinNIOConveyor) {
	// BPG0x00JM0122BinNIOConveyor conveyor = (BPG0x00JM0122BinNIOConveyor)
	// entity;
	// synchronized (core.getMutex()) {
	// conveyor.giveup();
	// }
	// } else {
	// JOptionPane.showMessageDialog(null, "Die Entität: " + entity.getId() +
	// " unterstüzt keine Funktion für das Abnehmen von Paletten!", "InfoBox: "
	// + "Fehler", JOptionPane.ERROR_MESSAGE);
	// }
	// }
	// };
	// return action;
	// }
	//
	// public static ActionListener retryAction(Core core, Entity entity) {
	// ActionListener action = new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// if (entity instanceof PIG01SP11PalletInfeedConveyor) {
	// PIG01SP11PalletInfeedConveyor conveyor = (PIG01SP11PalletInfeedConveyor)
	// entity;
	// synchronized (core.getMutex()) {
	// conveyor.retry();
	// }
	// } else if (entity instanceof CS51FP018PL077BinNOKPointConveyor) {
	// CS51FP018PL077BinNOKPointConveyor conveyor =
	// (CS51FP018PL077BinNOKPointConveyor) entity;
	// synchronized (core.getMutex()) {
	// conveyor.push();
	// }
	// } else {
	// JOptionPane.showMessageDialog(null, "Die Entität: " + entity.getId() +
	// " unterstüzt keine Funktion für das wieder einschleusen von Paletten!",
	// "InfoBox: " + "Fehler", JOptionPane.ERROR_MESSAGE);
	// }
	// }
	// };
	// return action;
	// }
	//
	// static JDialog createSettingsActionWindow(String title, JFrame owner, int
	// sizeX, int sizeY) {
	// JDialog dialog = new JDialog(owner, true);
	// dialog.setTitle(title);
	// dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	// dialog.setSize(sizeX, sizeY);
	// dialog.setLocationRelativeTo(owner);
	// dialog.setLayout(new BorderLayout());
	// // dialog.addWindowListener(new WindowAdapter() {
	// // @Override
	// // public void windowClosing(WindowEvent e) {
	// // setVisible(false);
	// // }
	// // });
	// // getRootPane().registerKeyboardAction(new ActionListener() {
	// // @Override
	// // public void actionPerformed(ActionEvent e) {
	// // setVisible(false);
	// // }
	// // }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	// JComponent.WHEN_IN_FOCUSED_WINDOW);
	// return dialog;
	// }
	//
	// static JPanel createFlagsPanel(List<JComboBox<String>> checks, String
	// caption, String[] labels) {
	// JPanel panel = new JPanel();
	// JPanel flagsPanel = new JPanel();
	// panel.setLayout(new BorderLayout());
	// panel.add(new JLabel(caption), BorderLayout.NORTH);
	// flagsPanel.setLayout(new GridLayout(checks.size(), 1, 5, 5));
	// for (int i = 0; i < checks.size(); ++i) {
	// JPanel group = new JPanel();
	// group.setLayout(new BorderLayout());
	// if (labels != null && i < labels.length) {
	// group.add(new JLabel(labels[i]), BorderLayout.CENTER);
	// } else {
	// group.add(new JLabel("Bit " + i), BorderLayout.CENTER);
	// }
	// JComboBox<String> comboboxCheck = checks.get(i);
	// comboboxCheck.setPreferredSize(new Dimension(70, 20));
	// group.add(comboboxCheck, BorderLayout.EAST);
	// flagsPanel.add(group);
	// }
	// panel.add(flagsPanel, BorderLayout.CENTER);
	// return panel;
	// }

}
