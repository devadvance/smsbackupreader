package com.devadvance.smsbackupreader;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GUI {

	private JFrame frmSmsBackupReader;
	private JTextField countryCodeTextBox;
	private JTextField offsetField;
	private JTextField fileLocationField;
	private JTextField numberSMSField;
	private JTextField exportFileField;
	private JTextArea messageTextBox;
	private JList contactListBox;
	private JFileChooser fileChooser;
	private JFileChooser saveChooser;

	private BackupReader reader;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GUI window = new GUI();
					window.frmSmsBackupReader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		reader = new BackupReader();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSmsBackupReader = new JFrame();
		frmSmsBackupReader.setTitle("SMS Backup Reader 0.7");
		frmSmsBackupReader.setBounds(100, 100, 700, 550);
		frmSmsBackupReader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmSmsBackupReader.setJMenuBar(menuBar);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpAction();
			}
		});
		mnHelp.add(mntmHelp);

		JMenuItem mntmAboutSmsBackup = new JMenuItem("About SMS Backup Reader");
		mntmAboutSmsBackup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutAction();
			}
		});
		mnHelp.add(mntmAboutSmsBackup);

		JLabel lblEnterYourCountry = new JLabel("Enter your country code (US is 1, UK is 44, ...):");
		lblEnterYourCountry.setFont(new Font("Tahoma", Font.PLAIN, 11));

		countryCodeTextBox = new JTextField();
		countryCodeTextBox.setText("1");
		countryCodeTextBox.setColumns(5);

		JLabel lblHoursOffsetFor = new JLabel("Hours offset for received SMS:");
		lblHoursOffsetFor.setFont(new Font("Tahoma", Font.PLAIN, 11));

		offsetField = new JTextField();
		offsetField.setText("0");
		offsetField.setColumns(5);

		fileLocationField = new JTextField();
		fileLocationField.setEditable(false);
		fileLocationField.setText("...");
		fileLocationField.setColumns(10);

		JButton chooseButton = new JButton("Choose File");
		chooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chooseFileAction();
			}
		});

		JLabel lblNumberOfSms = new JLabel("Number of SMS:");

		numberSMSField = new JTextField();
		numberSMSField.setEditable(false);
		numberSMSField.setColumns(10);

		JButton loadButton = new JButton("Load!");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadAction();
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JLabel lblNewLabel = new JLabel("Use the options below to export the SMS to a text file. You can either export the selected contact, or all:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));

		exportFileField = new JTextField();
		exportFileField.setText("...");
		exportFileField.setEditable(false);
		exportFileField.setColumns(10);

		JButton exportSelectedButton = new JButton("Export Selected");
		exportSelectedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportSelectedAction();
			}
		});

		JButton exportButton = new JButton("Export All");
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportAllAction();
			}
		});
		GroupLayout groupLayout = new GroupLayout(frmSmsBackupReader.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addGap(7)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createSequentialGroup()
														.addComponent(lblEnterYourCountry)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(countryCodeTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(lblHoursOffsetFor)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(offsetField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
														.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
																.addComponent(fileLocationField, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(chooseButton, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
																.addGroup(groupLayout.createSequentialGroup()
																		.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE))))
																		.addGroup(groupLayout.createSequentialGroup()
																				.addContainerGap()
																				.addComponent(lblNumberOfSms)
																				.addPreferredGap(ComponentPlacement.RELATED)
																				.addComponent(numberSMSField, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(ComponentPlacement.RELATED, 444, Short.MAX_VALUE)
																				.addComponent(loadButton, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(ComponentPlacement.RELATED)))
																				.addGap(5))
																				.addGroup(groupLayout.createSequentialGroup()
																						.addContainerGap()
																						.addComponent(exportFileField, GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
																						.addPreferredGap(ComponentPlacement.RELATED)
																						.addComponent(exportSelectedButton)
																						.addPreferredGap(ComponentPlacement.RELATED)
																						.addComponent(exportButton)
																						.addContainerGap())
																						.addGroup(groupLayout.createSequentialGroup()
																								.addContainerGap()
																								.addComponent(lblNewLabel)
																								.addContainerGap(129, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblEnterYourCountry)
								.addComponent(countryCodeTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblHoursOffsetFor)
								.addComponent(offsetField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(fileLocationField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(chooseButton))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblNumberOfSms)
												.addComponent(numberSMSField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(loadButton))
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
														.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
														.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(lblNewLabel)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
																.addComponent(exportFileField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																.addComponent(exportButton)
																.addComponent(exportSelectedButton))
																.addContainerGap())
				);

		messageTextBox = new JTextArea();
		scrollPane.setViewportView(messageTextBox);
		messageTextBox.setText("This is where the messages will show up...");
		messageTextBox.setLineWrap(true);
		messageTextBox.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		messageTextBox.setEditable(false);

		contactListBox = new JList();
		contactListBox.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		contactListBox.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				contactListValueChanged();
			}
		});
		scrollPane_1.setViewportView(contactListBox);
		contactListBox.setModel(new AbstractListModel() {
			String[] values = new String[] {"Contacts load here..."};
			@Override
			public int getSize() {
				return values.length;
			}
			@Override
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		contactListBox.setEnabled(false);
		contactListBox.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frmSmsBackupReader.getContentPane().setLayout(groupLayout);
	}

	private void chooseFileAction() {
		JOptionPane.showMessageDialog(frmSmsBackupReader, "Not all messages may show up under the correct contact.\nThis is due to Android storing numbers in different formats, such as +44 07xxx xxxxxx instead of just 07xxx xxxxxx.\n");
		String tempLocation;
		fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(frmSmsBackupReader);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File openFile = fileChooser.getSelectedFile();
			tempLocation = openFile.getAbsolutePath();
			fileLocationField.setText(tempLocation);

			reader.setLoadLocation(openFile);
		}
	}

	private void loadAction() {
		BigInteger _timeOffset = BigInteger.valueOf(Integer.parseInt(offsetField.getText()) * 3600000);
		String _countryCode = countryCodeTextBox.getText();

		if (fileLocationField.getText().equals("...")) {
			JOptionPane.showMessageDialog(frmSmsBackupReader, "Choose SMS backup file first!");
		}
		else {
			int returnValue = reader.loadSMS(_countryCode, _timeOffset);
			if (!(returnValue == 0)) { // If it errored in some way
				String errorMessage = "Failed to load messages!\n";
				switch(returnValue) {
				case 1:
					errorMessage += "Error Code " + returnValue + ": Country code or time offset invalid!\n";
					break;
				case 2:
					errorMessage += "Error Code " + returnValue + ": Invalid SMS backup file!\n";
					break;
				case 3:
					errorMessage += "Error Code " + returnValue + ": Problem reading the file!\n";
					break;
				default:
					errorMessage += "Error Code " + returnValue + ": Unknown error!\n";
					break;
				}
				JOptionPane.showMessageDialog(frmSmsBackupReader, errorMessage);
			}
			else {
				contactListBox.clearSelection();
				contactListBox.setListData(reader.getContactArray());
				contactListBox.setEnabled(true);
				numberSMSField.setText("" + reader.getNumberOfSMS());
			}
		}
	}

	private void contactListValueChanged() {
		Contact selectedContact = (Contact)contactListBox.getSelectedValue();
		// If this method has been called because a different file is being loaded while one is already open, there might be a null selection
		if (selectedContact != null) {
			ArrayList<Message> selectedMessages = selectedContact.getMessages();
			messageTextBox.setText("");
			for (int i = 0;i < selectedMessages.size();i++) {
				messageTextBox.append(selectedMessages.get(i).toString() + "\n");
			}
		}
	}

	private void helpAction() {
		HelpFrame frame = new HelpFrame();
		frame.setVisible(true);
	}

	private void aboutAction() {                                                
		JOptionPane.showMessageDialog(frmSmsBackupReader, "SMS Backup Reader\nv0.7 - 2013-05-10\nBy Matt (devadvance)\nxtnetworks@users.sf.net");
	}

	private void exportSelectedAction() {
		if (!(fileLocationField.getText().equals("...")) && (contactListBox.isEnabled())) {
			if (contactListBox.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(frmSmsBackupReader, "Select a contact first!");
			}
			else {
				saveChooser = new JFileChooser();
				saveChooser.setDialogTitle("Choose a file to save as...");
				saveChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
				saveChooser.setSelectedFile(new java.io.File("SMS_Export.txt"));

				int returnValue = saveChooser.showSaveDialog(frmSmsBackupReader);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File saveFile = saveChooser.getSelectedFile();
					String tempLocation = saveFile.getAbsolutePath();
					exportFileField.setText(tempLocation);

					if(reader.exportContactMessages(saveFile, contactListBox.getSelectedIndex())) {
						JOptionPane.showMessageDialog(frmSmsBackupReader, "Messages with selected contact exported successfully!");
					}
					else {
						JOptionPane.showMessageDialog(frmSmsBackupReader, "Failed to export selected messages!");
					}
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(frmSmsBackupReader, "Load messages first!");
		}
	}

	private void exportAllAction() {
		if (!(fileLocationField.getText().equals("...")) && (contactListBox.isEnabled())) {
			saveChooser = new JFileChooser();
			saveChooser.setDialogTitle("Choose a file to save as...");
			saveChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
			saveChooser.setSelectedFile(new java.io.File("SMS_Export.txt"));

			int returnValue = saveChooser.showSaveDialog(frmSmsBackupReader);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File saveFile = saveChooser.getSelectedFile();
				String tempLocation = saveFile.getAbsolutePath();
				exportFileField.setText(tempLocation);

				if(reader.exportAllMessages(saveFile)) {
					JOptionPane.showMessageDialog(frmSmsBackupReader, "All messages exported successfully!");
				}
				else {
					JOptionPane.showMessageDialog(frmSmsBackupReader, "Failed to export all messages!");
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(frmSmsBackupReader, "Load messages first!");
		}
	}
}
