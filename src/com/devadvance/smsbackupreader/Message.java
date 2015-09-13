package com.devadvance.smsbackupreader;

import java.math.BigInteger;
import java.util.Date;

public class Message implements Comparable<Message> {
	private String messageText;
	private int messageType;
	private BigInteger messageDate;
	private BigInteger messageAddress;
	private Date messageDateFormat;

	public Message() {
		messageType = -1;
		messageDate = BigInteger.valueOf(-1);
		messageText = "";
		messageAddress = BigInteger.valueOf(-1);
		messageDateFormat = new Date();
	}

	/**
	 * Constructor to create a message with specified parameters.
	 * @param address address of the message.
	 * @param date date of the message.
	 * @param body text of the message;
	 * @param type Type of message, 1 is received, 0 is sent.
	 * @param offset Offset if it is a received message.
	 */
	public Message(BigInteger address,BigInteger date,String body,int type, BigInteger offset) {
		messageType = type;

		messageDate = date;
		// If it is a received message, add the offset
		if (messageType == 1)
			messageDate = messageDate.add(offset);
		messageText = body;
		messageAddress = address;
		messageDateFormat = new Date(messageDate.longValue());
	}

	@Override
	public int compareTo(Message msg2) { 

		return (this.getMessageDate().compareTo(msg2.getMessageDate())); 
	}

	public void setMessageText(String input) {
		messageText = input;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageType(int input) {
		messageType = input;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageDate(BigInteger input) {
		messageDate = input;
		messageDateFormat = new Date(messageDate.longValue());
	}

	public BigInteger getMessageDate() {
		return messageDate;
	}

	public void setMessageAddress(BigInteger input) {
		messageAddress = input;
	}

	public BigInteger getMessageAddress() {
		return messageAddress;
	}

	@Override
	public String toString() {
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
