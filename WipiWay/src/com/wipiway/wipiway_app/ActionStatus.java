package com.wipiway.wipiway_app;

public class ActionStatus {
	
	private long id;
	private String phoneNumber;
	private long last_message_received;
	private int mode;
	private int stage;
	private int action;
	private String argument;
	
	public ActionStatus(int id, String phoneNumber,long last_message_received, int mode, int stage, int action, String argument){
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.last_message_received = last_message_received;
		this.mode = mode;
		this.stage = stage;
		this.action = action;
		this.argument = argument;
		
	}
	
	public ActionStatus() {

		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public long getLast_message_received() {
		return last_message_received;
	}
	public void setLast_message_received(long last_message_received) {
		this.last_message_received = last_message_received;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public String getArgument() {
		return argument;
	}
	public void setArgument(String argument) {
		this.argument = argument;
	}

	
	

}
