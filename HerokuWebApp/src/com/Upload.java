package com;

public class Upload {
	
	private String filename;
	private String filecontents;
	private String server;
	private int port;
	private String username;
	private String password;
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilecontents() {
		return filecontents;
	}
	public void setFilecontents(String filecontents) {
		this.filecontents = filecontents;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
