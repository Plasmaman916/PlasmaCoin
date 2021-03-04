package net.eclipsecraft.plasmacoin.Objects;

import javafx.scene.control.ButtonType;
import net.eclipsecraft.plasmacoin.app.Main;
import net.eclipsecraft.plasmacoin.wallet.Wallet;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.GroupLayout;
/*
 * Created by JFormDesigner on Tue Mar 02 21:42:56 CST 2021
 */



/**
 * @author Sam Forsythe
 */
public class GuiFrame extends JFrame {
	public GuiFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		loadEvents();
		new TransactionUpdater(this).start();
	}

	private void loadEvents(){
		openWallet.addActionListener(e->{
			System.out.println("Neat");
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			chooser.setDialogTitle("Choose Directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			//
			// disable the "All files" option.
			//
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.showOpenDialog(this);
            File selectedDirectory = chooser.getSelectedFile();;
            Wallet wallet = new Wallet();
            boolean loaded = false;
            try {
                loaded = wallet.loadKeyPair(selectedDirectory.getAbsolutePath());
            }catch (NullPointerException ex){}
            if(!loaded){
				Dialog dialog = new Dialog(this);
                JButton cont = dialog.getDiaCont();
				JButton canc = dialog.getDiaCanc();
				JTextArea text = dialog.getTextArea();
                cont.setText("Ok");
                text.setText("Error! This directory ("+selectedDirectory.getAbsolutePath()+") does not contain properly named key files!");
                text.setLineWrap(true);
                dialog.setVisible(true);
                cont.addActionListener(event ->{
                    dialog.dispose();
                });
                canc.addActionListener(event ->{
					dialog.dispose();
					return;
				});
                return;
            }
			textArea1.setText("Address: "+wallet.getPublicKeyHex());
			textArea1.setLineWrap(true);
            Main.setPathToCurrentWallet(selectedDirectory.getAbsolutePath());
            Main.loadWallet(wallet);
		});
		saveWallet.addActionListener(e->{
			if(Main.getWallet() == null) {
				Dialog dialog = new Dialog(this);
				JButton cont = dialog.getDiaCont();
				JButton canc = dialog.getDiaCanc();
				JTextArea text = dialog.getTextArea();
				cont.setText("Ok");
				text.setText("Error. You do not have any wallet loaded!");
				text.setLineWrap(true);
				dialog.setVisible(true);
				cont.addActionListener(event ->{
					dialog.dispose();
				});
				canc.addActionListener(event ->{
					dialog.dispose();
				});
				return;
			}
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			chooser.setDialogTitle("Choose Directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			//
			// disable the "All files" option.
			//
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.showOpenDialog(this);
			File selectedDirectory = chooser.getSelectedFile();;
            if(selectedDirectory != null) {
				System.out.println("asd");
				Dialog dialog = new Dialog(this);
				JButton cont = dialog.getDiaCont();
				JButton canc = dialog.getDiaCanc();
				JTextArea text = dialog.getTextArea();
				cont.setText("Ok");
                text.setText("Warning! This action (Save Wallet) will overwrite any existing wallet files in this directory ("+selectedDirectory.getAbsolutePath()+")! You may choose to cancel or continue!");
				text.setLineWrap(true);
				dialog.setVisible(true);
				cont.addActionListener(event ->{
					System.out.println(selectedDirectory.getAbsolutePath());
					Wallet wallet = Main.getWallet();
					wallet.saveKeyPair(selectedDirectory.getAbsolutePath());
					textArea1.setText("Address: "+wallet.getPublicKeyHex());
					textArea1.setLineWrap(true);
					Main.setPathToCurrentWallet(selectedDirectory.getAbsolutePath());
					dialog.dispose();
				});
				canc.addActionListener(event ->{
					dialog.dispose();
					return;
				});
				return;
            }
		});
		genWallet.addActionListener(event->{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			chooser.setDialogTitle("Choose Directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			//
			// disable the "All files" option.
			//
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.showOpenDialog(this);
			File selectedDirectory = chooser.getSelectedFile();;
            if(selectedDirectory != null) {
				Dialog dialog = new Dialog(this);
				JButton cont = dialog.getDiaCont();
				JButton canc = dialog.getDiaCanc();
				JTextArea text = dialog.getTextArea();
				cont.setText("Ok");
				text.setText("Warning! This action (Save Wallet) will overwrite any existing wallet files in this directory ("+selectedDirectory.getAbsolutePath()+")! You may choose to cancel or continue!");
				text.setLineWrap(true);
                text.setText("Warning! This action (Generate New Wallet) will overwrite any exist wallet files in this directory ("+selectedDirectory.getAbsolutePath()+")! You may choose to cancel or continue!");
                cont.addActionListener(e ->{
                    System.out.println(selectedDirectory.getAbsolutePath());
                    Wallet wallet = new Wallet();
                    wallet.saveKeyPair(selectedDirectory.getAbsolutePath());
                    textArea1.setText("Address: "+wallet.getPublicKeyHex());
					textArea1.setLineWrap(true);
                    Main.setPathToCurrentWallet(selectedDirectory.getAbsolutePath());
                    Main.loadWallet(wallet);
                    dialog.dispose();
                });
                canc.addActionListener(e ->{
                    dialog.dispose();
                });
				dialog.setVisible(true);
			}
		});
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Sam Forsythe
		textArea1 = new JTextArea();
		openWallet = new JButton();
		saveWallet = new JButton();
		genWallet = new JButton();
		scrollPane1 = new JScrollPane();

		//======== this ========
		Container contentPane = getContentPane();

		//---- textArea1 ----
		textArea1.setText("Current Address: RANDOMRANDOMRANDOMRANDOMRANDOMRANDOMRANDOMRANDOMRANDOMRANDOMRANDOM");
		textArea1.setEditable(false);

		//---- openWallet ----
		openWallet.setText("Open Wallet");

		//---- saveWallet ----
		saveWallet.setText("Save Wallet");

		//---- genWallet ----
		genWallet.setText("Generate New Wallet");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
				contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
								.addGroup(contentPaneLayout.createParallelGroup()
										.addGroup(contentPaneLayout.createSequentialGroup()
												.addContainerGap()
												.addComponent(textArea1, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
												.addGroup(contentPaneLayout.createParallelGroup()
														.addComponent(openWallet, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
														.addComponent(saveWallet, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)))
										.addGroup(contentPaneLayout.createSequentialGroup()
												.addGap(27, 27, 27)
												.addComponent(genWallet, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
												.addGap(74, 74, 74)
												.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)))
								.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
				contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
								.addGroup(contentPaneLayout.createParallelGroup()
										.addComponent(textArea1, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
										.addGroup(contentPaneLayout.createSequentialGroup()
												.addContainerGap()
												.addComponent(openWallet, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(saveWallet, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)))
								.addGroup(contentPaneLayout.createParallelGroup()
										.addGroup(contentPaneLayout.createSequentialGroup()
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
												.addComponent(genWallet, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
												.addGap(25, 25, 25))
										.addGroup(contentPaneLayout.createSequentialGroup()
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
												.addContainerGap())))
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	public JTextArea getAddressField(){
		return this.textArea1;
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Sam Forsythe
	private JTextArea textArea1;
	private JButton openWallet;
	private JButton saveWallet;
	private JButton genWallet;
	private JScrollPane scrollPane1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
