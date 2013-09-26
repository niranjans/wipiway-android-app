package com.wipiway.wipiway_app;

public class RecentLog {
	

	private int iconResourceId;
	private String logText;
	private String timeAgo;
	
	public RecentLog(int iconResourceId, String logText, String timeAgo) {
		this.iconResourceId = iconResourceId;
		this.logText =  logText;
		this.timeAgo = timeAgo;

	}
	
	public int getIconResourceId() {
		return iconResourceId;
	}

	public void setIconResourceId(int iconResourceId) {
		this.iconResourceId = iconResourceId;
	}

	public String getLogText() {
		return logText;
	}

	public void setLogText(String logText) {
		this.logText = logText;
	}

	public String getTimeAgo() {
		return timeAgo;
	}

	public void setTimeAgo(String timeAgo) {
		this.timeAgo = timeAgo;
	}

	
	

}
