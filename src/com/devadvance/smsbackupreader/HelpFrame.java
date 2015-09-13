package com.devadvance.smsbackupreader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class HelpFrame extends JFrame {

	private static final long serialVersionUID = 408142384609470813L;
	private JPanel contentPane;
	private ButtonGroup buttonGroup;
	private JTextArea helpTextArea;

	private final String englishHelp = "English:\nHow to use this program:\n\n1) Enter your country code. This will make it so the SMS conversations are correct.\n2) Choose a XML backup file to load. This needs to be a file from SMS Backup & Restore by Ritesh.\n3) After you choose a file, click the Load! button. This will display the messages.\n\nYou can view different conversations by click on different contacts.\n\nTo export a conversation, you can either click Export All, or choose a conversation and click Export Selected.\n";
	private final String hindiHelp = "हिंदी:\nकैसे इस कार्यक्रम का उपयोग करने के लिए\n1) अपने देश कोड दर्ज करें. यह बना तो एसएमएस वार्तालापों सही हैं.\n2) एक XML बैकअप फ़ाइल लोड करने के लिए चुनें. यह SMS Backup and Restore से एक फ़ाइल की जरूरत है.\n3) आप किसी फ़ाइल का चयन करने के बाद, Load! पर क्लिक करें. यह संदेश प्रदर्शित करेगा.\n\nआप विभिन्न संपर्कों पर क्लिक करके विभिन्न वार्तालापों को देख सकते हैं.\n\nनिर्यात एक बातचीत करने के लिए, आप या तो क्लिक करें सभी निर्यात कर सकते हैं या एक वार्तालाप चुनें और चयनित निर्यात क्लिक करें .\n";
	private final String spanishHelp = "Español:\nCómo utilizar este programa:\n\n1) Ponga tu código de país. Esto lo hará para presentar a Ud. las conversaciones SMS en la orden correcta.\n2) Selecciona un archivo XML para cargar. Esto tiene que ser un archivo de SMS Backup & Restore por Ritesh.\n3) Después de seleccionar un archivo, oprima Load!. Esto mostrará los mensajes.\n\nPara ver las conversaciones diferentes, haga un clic en los diferentes contactos.\n\nPara exportar una conversación, pueda hacer clic en Export All, o eliger una conversación y haga un clic en Export Selected.\n";
	private final String germanHelp = "Deutsch:\nWie dieses Programm zu verwenden:\n\n1) Geben Sie Ihre Vorwahl. Dadurch wird es so den SMS Gespräche korrekt sind.\n2) Wählen Sie ein XML-Backup-Datei zu laden. Dies muss eine Datei von SMS Backup & Restore von Ritesh werden.\n3) Nachdem Sie eine Datei auswählen, klicken Sie auf Load! .Dies zeigt die Nachrichten.\n\nSie können verschiedene Gespräche per Klick auf verschiedene Kontakte anzuzeigen.\n\nSo exportieren Sie ein Gespräch, können Sie entweder auf Export All, oder wählen Sie ein Gespräch und klicken Sie auf Export Selected.\n";

	/**
	 * Create the frame.
	 */
	public HelpFrame() {
		setTitle("Help - SMS Backup Reader");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 620, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		buttonGroup = new ButtonGroup();

		JRadioButton englishRadio = new JRadioButton("English");
		englishRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				helpTextArea.setText(englishHelp);
			}
		});
		englishRadio.setSelected(true);
		buttonGroup.add(englishRadio);

		JRadioButton spanishRadio = new JRadioButton("Español");
		spanishRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpTextArea.setText(spanishHelp);
			}
		});
		buttonGroup.add(spanishRadio);

		JRadioButton hindiRadio = new JRadioButton("हिंदी");
		hindiRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpTextArea.setText(hindiHelp);
			}
		});
		buttonGroup.add(hindiRadio);

		JRadioButton deutschRadio = new JRadioButton("Deutsch");
		deutschRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpTextArea.setText(germanHelp);
			}
		});
		buttonGroup.add(deutschRadio);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(englishRadio)
								.addComponent(spanishRadio)
								.addComponent(hindiRadio)
								.addComponent(deutschRadio))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
								.addContainerGap())
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(englishRadio)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(spanishRadio)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(hindiRadio)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(deutschRadio)))
										.addContainerGap())
				);

		helpTextArea = new JTextArea();
		helpTextArea.setWrapStyleWord(true);
		helpTextArea.setLineWrap(true);
		helpTextArea.setText("English:\r\nHow to use this program:\r\n\r\n1) Enter your country code. This will make it so the SMS conversations are correct.\r\n2) Choose a XML backup file to load. This needs to be a file from SMS Backup & Restore by Ritesh.\r\n3) After you choose a file, click the Load! button. This will display the messages.\r\n\r\nYou can view different conversations by click on different contacts.\r\n\r\nTo export a conversation, you can either click Export All, or choose a conversation and click Export Selected.");
		helpTextArea.setEditable(false);
		scrollPane.setViewportView(helpTextArea);
		contentPane.setLayout(gl_contentPane);
	}
}
