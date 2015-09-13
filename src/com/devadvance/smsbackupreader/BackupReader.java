package com.devadvance.smsbackupreader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * BackupReader
 * This holds the state of the reader, as well as the loaded information.
 * All functionality is held within this class, so that the GUI/CLI is just a frontend, and the process is abstracted away.
 * @author Matt
 *
 */
public class BackupReader {

	File openFile; // Used for opening backup files
	File saveFile; // Used for saving to a text file
	private String countryCode; // Country code
	private BigInteger timeOffset; // Time offset for received messages
	private Contact[] contactArray; // Array of contacts (and in turn, their associated messages)
	private int numberOfSMS; // Number of SMS reader from backup
	private boolean loadSuccess; // State of the reading of the backup file

	/**
	 * Default constructor. Initializes the necessary fields.
	 */
	public BackupReader() {
		numberOfSMS = 0;
		contactArray = null;
		loadSuccess = false;
	}
	
	/**
	 * setLoadLocation sets the location of the backup file to read. Does not actually do any reading.
	 * @param file The File representing the location of the backup file.
	 * @return True if set correctly.
	 */
	public boolean setLoadLocation(File file) {
		openFile = file;
		return true;
	}
	
	/**
	 * elementToMessage turns an Element from the XML backup file into a Message object.
	 * @param element The Element to turn into a Message object.
	 * @return A Message object containing the date, address, and body from the Element.
	 */
	private Message elementToMessage(Element element){
		String addressString = element.getAttribute("address");
		addressString = removeExtraDigits(addressString);

		// Just in case, make sure its not empty
		if(addressString.isEmpty()) {
			addressString = "0";
		}
		
		BigInteger address = new BigInteger(addressString);

		BigInteger date = new BigInteger(element.getAttribute("date"));
		String body = element.getAttribute("body");
		int type = Integer.parseInt(element.getAttribute("type"));

		Message msg = new Message(address,date,body,type, timeOffset);

		return msg;
	}

	/**
	 * removeExtraDigits removes extra digits and characters from addresses associated with a message.
	 * @param inputAddress The address from the Element pulled from the XML backup file.
	 * @return A String containing the address without the extra digits or characters.
	 */
	private String removeExtraDigits(String inputAddress) {
		// If it is a draft message, the address is "null"
		if (inputAddress.equalsIgnoreCase("null"))
			return "0";

		inputAddress = inputAddress.replaceAll("\\D", "");

		// For short codes/other similar stuff
		if (inputAddress.length() <= 5) {
			return inputAddress;
		}
		else if (inputAddress.startsWith(countryCode)) {
			inputAddress = inputAddress.substring(countryCode.length());
		}
		
		return inputAddress;
	}

	/**
	 * loadSMS loads the SMS from the XML backup file. Requires that the openFile has been set already.
	 * @param _countryCode Country code to use for the messages.
	 * @param _timeOffset Offset for received messages, if desired.
	 * @return An integer indicating the success/failure. 0 - no error, 1 - invalid country code or offset, 2 - invalid backup file, 3 - other error, -1 - unknown error.
	 */
	public int loadSMS(String _countryCode, BigInteger _timeOffset) {
		int errored = -1;
		// If the parameters were not passed in correctly, return false
		if ((_countryCode == null) || (_timeOffset == null)) {
			return 1;
		}

		// HashMap to store contacts and their associated messages. Maps address to contact. Could use a HashSet ideally, but that can be done later.
		HashMap<BigInteger, Contact> contactMap = new HashMap<BigInteger, Contact>();

		countryCode = _countryCode;
		timeOffset = _timeOffset;
		try {
			InputStream is = new FileInputStream(openFile);  
			Reader reader = new InputStreamReader(is, "UTF-8");  
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(reader));
			
			doc.getDocumentElement().normalize();

			Element docEle = doc.getDocumentElement();

			// Get a nodelist of elements in the XML file
			NodeList nl = docEle.getElementsByTagName("sms");

			if(nl != null && nl.getLength() > 0) {
				// Set the number of SMS
				numberOfSMS = nl.getLength();

				Element element = null;
				Message msg = null;
				// For each message in the nodelist
				for (int i = 0; i < nl.getLength(); i++) {
					element = (Element) nl.item(i);
					msg = elementToMessage(element);

					// If the contactMap already contains this contact, just add the message
					if (contactMap.containsKey(msg.getMessageAddress())) {
						contactMap.get(msg.getMessageAddress()).addMessage(msg);
					}
					else { // Need to add the contact first
						Contact tempContact = new Contact(element.getAttribute("contact_name"), msg.getMessageAddress());
						tempContact.addMessage(msg);
						contactMap.put(msg.getMessageAddress(), tempContact);
					}
				}

				// Convert the contactMap to an array
				contactArray = contactMap.values().toArray(new Contact[contactMap.size()]);

				// Sort each contacts' messages
				for (int c = 0; c < contactArray.length; c++) {
					Collections.sort(contactArray[c].messageList);  
				}

				// Set boolean to true
				loadSuccess = true;
				errored = 0;
			}
			else { // No error but the file had no SMS
				loadSuccess = false;
			}
			
		} catch (SAXException e) {
			loadSuccess = false;
			errored = 2;
			
		} catch (Exception e) { // Error
			loadSuccess = false;
			errored = 3;
		}

		return errored;
	}
	
	/**
	 * exportContactMessages exports a specified contacts messages.
	 * @param saveFile File representing where to export the messages.
	 * @param contactIndex Index in the contactArray for which contacts messages to export.
	 * @return True if exported successfully, false otherwise.
	 */
	public boolean exportContactMessages(File saveFile, int contactIndex) {
		BufferedWriter outputWriter = null;

		try {
			outputWriter = new BufferedWriter(new FileWriter(saveFile));

			ArrayList<Message> selectedMessages = contactArray[contactIndex].getMessages();
			outputWriter.write(contactArray[contactIndex].toString());
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
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	/**
	 *  exportAllMessages exports all messages from every contact.
	 * @param saveFile File representing where to export the messages.
	 * @return True if exported successfully, false otherwise.
	 */
	public boolean exportAllMessages(File saveFile) {
		BufferedWriter outputWriter = null;
		ArrayList<Message> selectedMessages = null;

		try {
			outputWriter = new BufferedWriter(new FileWriter(saveFile));

			for (Contact selectedContact : contactArray) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	/**
	 * getContactArray gets the contact array once the XML backup file has been loaded.
	 * @return The array of contacts and their associated messages.
	 */
	public Contact[] getContactArray() {
		return contactArray;
	}
	
	/**
	 * getNumberOfSMS gets the number of SMS loaded.
	 * @return Number of SMS loaded.
	 */
	public int getNumberOfSMS() {
		return numberOfSMS;
	}
}
