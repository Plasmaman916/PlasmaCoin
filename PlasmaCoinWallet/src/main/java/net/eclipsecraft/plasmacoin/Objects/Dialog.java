package net.eclipsecraft.plasmacoin.Objects;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
/*
 * Created by JFormDesigner on Tue Mar 02 21:53:26 CST 2021
 */



/**
 * @author Sam Forsythe
 */
public class Dialog extends JDialog {
	public Dialog(Window owner) {
		super(owner);
		initComponents();
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public JButton getDiaCont() {
		return diaCont;
	}

	public JButton getDiaCanc() {
		return diaCanc;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Sam Forsythe
		textArea = new JTextArea();
		diaCont = new JButton();
		diaCanc = new JButton();

		//======== this ========
		Container contentPane = getContentPane();

		//---- textArea ----
		textArea.setEditable(false);

		//---- diaCont ----
		diaCont.setText("Continue");

		//---- diaCanc ----
		diaCanc.setText("Cancel");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
				contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
								.addGap(37, 37, 37)
								.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
										.addGroup(contentPaneLayout.createSequentialGroup()
												.addComponent(diaCont, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(diaCanc, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)))
								.addContainerGap(41, Short.MAX_VALUE))
		);
		contentPaneLayout.setVerticalGroup(
				contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(diaCont, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addComponent(diaCanc, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(25, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Sam Forsythe
	private JTextArea textArea;
	private JButton diaCont;
	private JButton diaCanc;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
