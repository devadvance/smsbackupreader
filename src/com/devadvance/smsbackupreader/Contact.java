package com.devadvance.smsbackupreader;

import java.math.BigInteger;
import java.util.ArrayList;

public class Contact {

	private String name;
	private BigInteger phoneNumber;
	ArrayList<Message> messageList;

	public Contact() {
		name = "";
		phoneNumber = new BigInteger("");
		messageList = new ArrayList<Message>();
	}

	public Contact(String inputName, BigInteger inputNumber) {
		name = inputName;
		phoneNumber = inputNumber;
		messageList = new ArrayList<Message>();
	}

	public String getName() {
		return name;
	}

	public BigInteger getNumber() {
		return phoneNumber;
	}

	public ArrayList<Message> getMessages() {
		return messageList;
	}

	public void addMessage(Message msgToAdd) {
		messageList.add(msgToAdd);
	}

	@Override
	public String toString() {
		String tempString;
		tempString = name;
		tempString += " - " + phoneNumber.toString();
		return tempString;
	}
}
