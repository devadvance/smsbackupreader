/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * smsBackupReaderGUI.java
 *
 * Created on Jul 30, 2011, 11:48:28 AM
 */
package smsBR;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author xtnetworks
 */
public class smsBackupReaderGUI extends javax.swing.JFrame {

    // Semi-global variables. Used in more than one method.
    File openFile;
    File saveFile;
    private static String countryCode;
    private static BigInteger timeOffset;
    
    private static String englishHelp = "English:\nHow to use this program:\n\n1) Enter your country code. This will make it so the SMS conversations are correct.\n2) Choose a XML backup file to load. This needs to be a file from SMS Backup & Restore by Ritesh.\n3) After you choose a file, click the Load! button. This will display the messages.\n\nYou can view different conversations by click on different contacts.\n\nTo export a conversation, you can either click Export All, or choose a conversation and click Export Selected.\n";
    private static String hindiHelp = "हिंदी:\nकैसे इस कार्यक्रम का उपयोग करने के लिए\n1) अपने देश कोड दर्ज करें. यह बना तो एसएमएस वार्तालापों सही हैं.\n2) एक XML बैकअप फ़ाइल लोड करने के लिए चुनें. यह SMS Backup and Restore से एक फ़ाइल की जरूरत है.\n3) आप किसी फ़ाइल का चयन करने के बाद, Load! पर क्लिक करें. यह संदेश प्रदर्शित करेगा.\n\nआप विभिन्न संपर्कों पर क्लिक करके विभिन्न वार्तालापों को देख सकते हैं.\n\nनिर्यात एक बातचीत करने के लिए, आप या तो क्लिक करें सभी निर्यात कर सकते हैं या एक वार्तालाप चुनें और चयनित निर्यात क्लिक करें .\n";
    private static String spanishHelp = "Español:\nCómo utilizar este programa:\n\n1) Ponga tu código de país. Esto lo hará para presentar a Ud. las conversaciones SMS en la orden correcta.\n2) Selecciona un archivo XML para cargar. Esto tiene que ser un archivo de SMS Backup & Restore por Ritesh.\n3) Después de seleccionar un archivo, oprima Load!. Esto mostrará los mensajes.\n\nPara ver las conversaciones diferentes, haga un clic en los diferentes contactos.\n\nPara exportar una conversación, pueda hacer clic en Export All, o eliger una conversación y haga un clic en Export Selected.\n";
    private static String germanHelp = "Deutsch:\nWie dieses Programm zu verwenden:\n\n1) Geben Sie Ihre Vorwahl. Dadurch wird es so den SMS Gespräche korrekt sind.\n2) Wählen Sie ein XML-Backup-Datei zu laden. Dies muss eine Datei von SMS Backup & Restore von Ritesh werden.\n3) Nachdem Sie eine Datei auswählen, klicken Sie auf Load! .Dies zeigt die Nachrichten.\n\nSie können verschiedene Gespräche per Klick auf verschiedene Kontakte anzuzeigen.\n\nSo exportieren Sie ein Gespräch, können Sie entweder auf Export All, oder wählen Sie ein Gespräch und klicken Sie auf Export Selected.\n";
    
    private static message getText(Element empEl)
	{
		String addressString = empEl.getAttribute("address");
		addressString = removeExtraDigits(addressString);
		
                // Just in case, make sure its not empty
                if(addressString.isEmpty())
                    addressString = "0";
                
		BigInteger address = new BigInteger(addressString);
		
		BigInteger date = new BigInteger(empEl.getAttribute("date"));
		String body = empEl.getAttribute("body");
		int type = Integer.parseInt(empEl.getAttribute("type"));

		
		message e = new message(address,date,body,type);
		
		return e;
	}

    // Removes non-digits and country code from phone number
    private static String removeExtraDigits(String inputAddress)
    {
        // If it is a draft message, the address is "null"
	if (inputAddress.equalsIgnoreCase("null"))
            return "0";
        
        inputAddress = inputAddress.replaceAll("\\D", "");
        
        // For short codes/other similar stuff
        if (inputAddress.length() <= 5)
            return inputAddress;
        else if (inputAddress.startsWith(countryCode))
		inputAddress = inputAddress.substring(countryCode.length());
	return inputAddress;
    }
    
    // message object
    private static class message implements Comparable<message>
	{
		private String messageText;
		private int messageType;
		private BigInteger messageDate;
		private BigInteger messageAddress;
                private Date messageDateFormat;
		
		public message()
		{
			messageType = -1;
			messageDate = BigInteger.valueOf(-1);
			messageText = "";
			messageAddress = BigInteger.valueOf(-1);
                        messageDateFormat = new Date();
		}
		
		public message(BigInteger address,BigInteger date,String body,int type)
		{
			messageType = type;
			//messageDate = date;
                        
                        messageDate = date;
                        // If it is a received message, add the offset
                        if (messageType == 1)
                            messageDate = messageDate.add(timeOffset);
			messageText = body;
			messageAddress = address;
                        messageDateFormat = new Date(messageDate.longValue());
		}
		
        @Override
                public int compareTo(message msg2) 
                { 
                    
                        return (this.getMessageDate().compareTo(msg2.getMessageDate())); 
                }
                
		public void setMessageText(String input)
		{
			messageText = input;
		}
		
		public String getMessageText()
		{
			return messageText;
		}
		
		public void setMessageType(int input)
		{
			messageType = input;
		}
		
		public int getMessageType()
		{
			return messageType;
		}
		
		public void setMessageDate(BigInteger input)
		{
			messageDate = input;
                        messageDateFormat = new Date(messageDate.longValue());
		}
		
		public BigInteger getMessageDate()
		{
			return messageDate;
		}
		
		public void setMessageAddress(BigInteger input)
		{
			messageAddress = input;
		}
		
		public BigInteger getMessageAddress()
		{
			return messageAddress;
		}
		
        @Override
		public String toString()
		{
			String tempString;
			if (messageType == 2)
				tempString = "Sent: ";
			else
				tempString = "Received: ";
                        tempString += messageDateFormat.toString() + ":  ";
			tempString += messageText;
			return tempString;
		}
		
	}
	
	private static class contact
	{
		private String name;
		private BigInteger phoneNumber;
		ArrayList<message> messageList;
		
		public contact()
		{
			name = "";
			phoneNumber = new BigInteger("");
			messageList = new ArrayList<message>();
		}
		
		public contact(String inputName, BigInteger inputNumber)
		{
			name = inputName;
			phoneNumber = inputNumber;
			messageList = new ArrayList<message>();
		}
		
		public String getName()
		{
			return name;
		}
		
		public BigInteger getNumber()
		{
			return phoneNumber;
		}
		
		public ArrayList<message> getMessages()
		{
			return messageList;
		}
		
		public void addMessage(message msgToAdd)
		{
			messageList.add(msgToAdd);
		}
		
		public String toString()
		{
			String tempString;
			tempString = name;
			tempString += " - " + phoneNumber.toString();
			return tempString;
		}
		
	}
    
    
    /** Creates new form smsBackupReaderGUI */
    public smsBackupReaderGUI() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        saveChooser = new javax.swing.JFileChooser();
        helpFrame = new javax.swing.JFrame();
        jButton1 = new javax.swing.JButton();
        englishRadio = new javax.swing.JRadioButton();
        spanishRadio = new javax.swing.JRadioButton();
        hindiRadio = new javax.swing.JRadioButton();
        germanRadio = new javax.swing.JRadioButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        helpTextArea = new javax.swing.JTextArea();
        languageGroup = new javax.swing.ButtonGroup();
        fileLocationField = new javax.swing.JTextField();
        chooseButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contactListBox = new javax.swing.JList();
        loadButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        messageTextBox = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        numberTextBox = new javax.swing.JTextField();
        areaCodeTextBox = new javax.swing.JTextField();
        exportButton = new javax.swing.JButton();
        exportFileField = new javax.swing.JTextField();
        exportSelectedButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        offsetField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exitMenuButton = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        helpMenuButton = new javax.swing.JMenuItem();
        aboutMenuButton = new javax.swing.JMenuItem();

        saveChooser.setDialogTitle("Choose a file to save as...");
        saveChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        saveChooser.setSelectedFile(new java.io.File("C:\\Program Files (x86)\\NetBeans 7.0\\SMS_Export.txt"));

        helpFrame.setTitle("Help");
        helpFrame.setMinimumSize(new java.awt.Dimension(700, 550));
        helpFrame.setResizable(false);

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        languageGroup.add(englishRadio);
        englishRadio.setSelected(true);
        englishRadio.setText("English");
        englishRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                englishRadioActionPerformed(evt);
            }
        });

        languageGroup.add(spanishRadio);
        spanishRadio.setText("Español");
        spanishRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spanishRadioActionPerformed(evt);
            }
        });

        languageGroup.add(hindiRadio);
        hindiRadio.setText("हिंदी");
        hindiRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hindiRadioActionPerformed(evt);
            }
        });

        languageGroup.add(germanRadio);
        germanRadio.setText("Deutsch");
        germanRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                germanRadioActionPerformed(evt);
            }
        });

        helpTextArea.setColumns(20);
        helpTextArea.setEditable(false);
        helpTextArea.setLineWrap(true);
        helpTextArea.setRows(5);
        helpTextArea.setText("English:\nHow to use this program:\n\n1) Enter your country code. This will make it so the SMS conversations are correct.\n2) Choose a XML backup file to load. This needs to be a file from SMS Backup & Restore by Ritesh.\n3) After you choose a file, click the Load! button. This will display the messages.\n\nYou can view different conversations by click on different contacts.\n\nTo export a conversation, you can either click Export All, or choose a conversation and click Export Selected.");
        helpTextArea.setOpaque(false);
        helpTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                helpTextAreaFocusGained(evt);
            }
        });
        jScrollPane3.setViewportView(helpTextArea);

        javax.swing.GroupLayout helpFrameLayout = new javax.swing.GroupLayout(helpFrame.getContentPane());
        helpFrame.getContentPane().setLayout(helpFrameLayout);
        helpFrameLayout.setHorizontalGroup(
            helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(helpFrameLayout.createSequentialGroup()
                        .addGroup(helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spanishRadio)
                            .addComponent(hindiRadio)
                            .addComponent(germanRadio)
                            .addComponent(englishRadio))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)))
                .addContainerGap())
        );
        helpFrameLayout.setVerticalGroup(
            helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(helpFrameLayout.createSequentialGroup()
                        .addComponent(englishRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spanishRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(hindiRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(germanRadio))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SMS Backup Reader v0.6");

        fileLocationField.setEditable(false);
        fileLocationField.setText("...");
        fileLocationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileLocationFieldActionPerformed(evt);
            }
        });

        chooseButton.setText("Choose File");
        chooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Enter your country code (US is 1, UK is 44, ...) :");

        contactListBox.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Contacts load here..." };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        contactListBox.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        contactListBox.setEnabled(false);
        contactListBox.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                contactListBoxValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(contactListBox);

        loadButton.setText("Load!");
        loadButton.setMaximumSize(new java.awt.Dimension(87, 23));
        loadButton.setMinimumSize(new java.awt.Dimension(87, 23));
        loadButton.setPreferredSize(new java.awt.Dimension(87, 23));
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        messageTextBox.setColumns(20);
        messageTextBox.setEditable(false);
        messageTextBox.setFont(new java.awt.Font("Arial", 0, 12));
        messageTextBox.setLineWrap(true);
        messageTextBox.setRows(5);
        messageTextBox.setText("This is where the messages will show up.");
        jScrollPane2.setViewportView(messageTextBox);

        jLabel2.setText("Number of SMS:");

        numberTextBox.setEditable(false);
        numberTextBox.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        areaCodeTextBox.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        areaCodeTextBox.setText("1");
        areaCodeTextBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                areaCodeTextBoxActionPerformed(evt);
            }
        });

        exportButton.setText("Export All");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        exportFileField.setText("...");
        exportFileField.setOpaque(false);
        exportFileField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportFileFieldActionPerformed(evt);
            }
        });

        exportSelectedButton.setText("Export Selected");
        exportSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportSelectedButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Use the options below to export the SMS to a text file. You can either export the selected contact, or all:");

        offsetField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        offsetField.setText("0");

        jLabel4.setText("Hours offset for received SMS:");

        jMenu1.setText("File");

        exitMenuButton.setText("Exit");
        exitMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuButtonActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuButton);

        menuBar.add(jMenu1);

        jMenu2.setText("Help");

        helpMenuButton.setText("Help");
        helpMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuButtonActionPerformed(evt);
            }
        });
        jMenu2.add(helpMenuButton);

        aboutMenuButton.setText("About...");
        aboutMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuButtonActionPerformed(evt);
            }
        });
        jMenu2.add(aboutMenuButton);

        menuBar.add(jMenu2);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(exportFileField, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(exportSelectedButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(areaCodeTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(offsetField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fileLocationField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(numberTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(loadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chooseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(areaCodeTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(offsetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseButton)
                    .addComponent(fileLocationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(numberTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exportFileField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportButton)
                    .addComponent(exportSelectedButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseButtonActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, "Not all messages may show up under the correct contact.\nThis is due to Android storing numbers in different formats, such as +44 07xxx xxxxxx instead of just 07xxx xxxxxx.\n");
        String tempLocation;
        int returnValue = fileChooser.showOpenDialog(smsBackupReaderGUI.this);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
        openFile = fileChooser.getSelectedFile();
        tempLocation = openFile.getAbsolutePath();
        fileLocationField.setText(tempLocation);
        }
    }//GEN-LAST:event_chooseButtonActionPerformed

    private void fileLocationFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileLocationFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileLocationFieldActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        if (!(fileLocationField.getText().equals("..."))) {
            try {
                // TODO add your handling code here:
                    
                    String documentString;
                
                    contactListBox.setEnabled(true);
                    contactListBox.removeAll();
                    messageTextBox.removeAll();

                    // Depreciated object. To be replaced with Hashmap?
                    Hashtable contactTable = new Hashtable(250);

                    // Get country code from areaCodeTextBox
                    countryCode = areaCodeTextBox.getText();

                    // Get time offset from offsetField
                    timeOffset = BigInteger.valueOf(Integer.parseInt(offsetField.getText()) * 3600000);
                    
                    File fXmlFile = openFile;
                     
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(fXmlFile);
                    
                    doc.getDocumentElement().normalize();

                    Element docEle = doc.getDocumentElement();

                    // Get a nodelist of elements in the XML file
                    NodeList nl = docEle.getElementsByTagName("sms");
                    if(nl != null && nl.getLength() > 0) {
                            numberTextBox.setText("" + nl.getLength());
                            for(int i = 0 ; i < nl.getLength();i++) {
                                    //Get the message element
                                    Element el = (Element)nl.item(i);

                                    // Get the message object
                                    message e = getText(el);


                                    // Begin code to add/update contact in Hashtable
                                    if (contactTable.containsKey(e.getMessageAddress()))
                                    {
                                            ((contact)contactTable.get(e.getMessageAddress())).addMessage(e);
                                    }
                                    else
                                    {
                                            contact tempContact = new contact(el.getAttribute("contact_name"), e.getMessageAddress());
                                            tempContact.addMessage(e);
                                            contactTable.put(e.getMessageAddress(), tempContact);
                                    }
                                    // End section
                            }
                    }
                    
                    Collection tempColl = contactTable.values();
                    contact[] contactArray = (contact[])tempColl.toArray(new contact[contactTable.size()]);
                    
                    
                    // Sort each contacts' messages
                    for (int c = 0; c < contactArray.length; c++)
                    {
                        Collections.sort(contactArray[c].messageList);
                                
                    }
                    
                    
                    
                    contactListBox.setListData(contactArray);
                    contactListBox.setEnabled(true);

            } catch (SAXException ex) {
                Logger.getLogger(smsBackupReaderGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(smsBackupReaderGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(smsBackupReaderGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            JOptionPane.showMessageDialog(rootPane, "Choose SMS backup file first!");
        }
    }//GEN-LAST:event_loadButtonActionPerformed

    private void contactListBoxValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_contactListBoxValueChanged
        // TODO add your handling code here:
        contact selectedContact = (contact)contactListBox.getSelectedValue();
        ArrayList<message> selectedMessages = selectedContact.getMessages();
        messageTextBox.setText("");
        for (int i = 0;i < selectedMessages.size();i++)
            messageTextBox.append(selectedMessages.get(i).toString() + "\n");
        
    }//GEN-LAST:event_contactListBoxValueChanged

    private void aboutMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuButtonActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, "SMS Backup Reader\nv0.6 - 2012-05-20\nBy Matt (devadvance)\nxtnetworks@users.sf.net");
    }//GEN-LAST:event_aboutMenuButtonActionPerformed

    private void areaCodeTextBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_areaCodeTextBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_areaCodeTextBoxActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        // TODO add your handling code here:
        if (!(fileLocationField.getText().equals("...")) && (contactListBox.isEnabled())) {
            try {

                BufferedWriter outputWriter = null;

                String tempLocation = "";
                int returnValue = saveChooser.showSaveDialog(smsBackupReaderGUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                saveFile = saveChooser.getSelectedFile();
                tempLocation = saveFile.getAbsolutePath();
                exportFileField.setText(tempLocation);

                outputWriter = new BufferedWriter(new FileWriter(tempLocation));

                ArrayList<message> selectedMessages;
                ListModel tempList = contactListBox.getModel();
                int numberOfContacts = tempList.getSize();
                contact selectedContact;

                for (int counter = 0; counter < numberOfContacts;counter++) {
                    selectedContact = (contact)tempList.getElementAt(counter);
                    selectedMessages = selectedContact.getMessages();

                    outputWriter.write(selectedContact.toString());
                    outputWriter.newLine();
                    outputWriter.newLine();

                    for (int i = 0;i < selectedMessages.size();i++) {
                        outputWriter.write(selectedMessages.get(i).toString());
                        outputWriter.newLine();
                    }

                    outputWriter.write("++++++++++++++++++++++++++++++++++++++++++++++++++");
                    outputWriter.newLine();
                }

                if (outputWriter != null) {
                    outputWriter.flush();
                    outputWriter.close();
                }

            }
            } catch (IOException ex) {
                Logger.getLogger(smsBackupReaderGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       else {
            JOptionPane.showMessageDialog(rootPane, "Load messages first!");
        }
    }//GEN-LAST:event_exportButtonActionPerformed

    private void exportFileFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportFileFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportFileFieldActionPerformed

    private void exportSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportSelectedButtonActionPerformed
        // TODO add your handling code here:
        if (!(fileLocationField.getText().equals("...")) && (contactListBox.isEnabled())) {
            BufferedWriter outputWriter = null;
            String tempLocation = "";
            int returnValue = saveChooser.showSaveDialog(smsBackupReaderGUI.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    saveFile = saveChooser.getSelectedFile();
                    tempLocation = saveFile.getAbsolutePath();
                    exportFileField.setText(tempLocation);


                    outputWriter = new BufferedWriter(new FileWriter(tempLocation));

                    contact selectedContact = (contact)contactListBox.getSelectedValue();
                    ArrayList<message> selectedMessages = selectedContact.getMessages();
                    outputWriter.write(selectedContact.toString());
                    outputWriter.newLine();
                    outputWriter.newLine();

                    for (int i = 0;i < selectedMessages.size();i++) {
                        outputWriter.write(selectedMessages.get(i).toString());
                        outputWriter.newLine();
                    }
                    
                    if (outputWriter != null) {
                        outputWriter.flush();
                        outputWriter.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(smsBackupReaderGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        else {
            JOptionPane.showMessageDialog(rootPane, "Load messages first!");
        }
    }//GEN-LAST:event_exportSelectedButtonActionPerformed

    private void helpMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuButtonActionPerformed
        // TODO add your handling code here:
        helpFrame.setVisible(true);
    }//GEN-LAST:event_helpMenuButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        helpFrame.setVisible(false);
}//GEN-LAST:event_jButton1ActionPerformed

    private void spanishRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spanishRadioActionPerformed
        // TODO add your handling code here:
        helpTextArea.setText(spanishHelp);
    }//GEN-LAST:event_spanishRadioActionPerformed

    private void helpTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_helpTextAreaFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_helpTextAreaFocusGained

    private void englishRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_englishRadioActionPerformed
        // TODO add your handling code here:
        helpTextArea.setText(englishHelp);
    }//GEN-LAST:event_englishRadioActionPerformed

    private void hindiRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hindiRadioActionPerformed
        // TODO add your handling code here:
        helpTextArea.setText(hindiHelp);
    }//GEN-LAST:event_hindiRadioActionPerformed

    private void germanRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_germanRadioActionPerformed
        // TODO add your handling code here:
        helpTextArea.setText(germanHelp);
    }//GEN-LAST:event_germanRadioActionPerformed

    private void exitMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuButtonActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_exitMenuButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new smsBackupReaderGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuButton;
    private javax.swing.JTextField areaCodeTextBox;
    private javax.swing.JButton chooseButton;
    private javax.swing.JList contactListBox;
    private javax.swing.JRadioButton englishRadio;
    private javax.swing.JMenu exitMenuButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JTextField exportFileField;
    private javax.swing.JButton exportSelectedButton;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JTextField fileLocationField;
    private javax.swing.JRadioButton germanRadio;
    private javax.swing.JFrame helpFrame;
    private javax.swing.JMenuItem helpMenuButton;
    private javax.swing.JTextArea helpTextArea;
    private javax.swing.JRadioButton hindiRadio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.ButtonGroup languageGroup;
    private javax.swing.JButton loadButton;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTextArea messageTextBox;
    private javax.swing.JTextField numberTextBox;
    private javax.swing.JTextField offsetField;
    private javax.swing.JFileChooser saveChooser;
    private javax.swing.JRadioButton spanishRadio;
    // End of variables declaration//GEN-END:variables
}
