package edu.cooper.wordsfornerds;

public class HelloMessage {

	private String name;
	private String password;

	public HelloMessage() {
	}

	public HelloMessage(String name) {
		this.name = name;
	}

	public HelloMessage(String name,String password) {
		this.name = name; this.password = password;
	}

	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name = name;
	}
}
