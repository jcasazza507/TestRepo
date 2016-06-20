package com;

public class FTPResponse {
	private int code;
	private String message;
	
	public FTPResponse(int c, String m)
	{
		setCode(c);
		setMessage(m);
	}

	public FTPResponse()
	{

	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	} 
}
