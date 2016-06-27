package com;
/**
 * 
 * @author evan marian
 * 
 * FileResult is a simple class that holds two pieces of data: a String containing the name of a file 
 * (filename) and a String containing the file's contents. 
 * Has a no-arg and full-arg constructors as well as simple gets and sets.
 */

public class FileResult {
	
	private String filename;
	private String filecontents;
	
	public FileResult() {
		setFilename(null);
		setFilecontents(null);
	}
	
	public FileResult(String fn, String fc) {
		setFilename(fn);
		setFilecontents(fc);
	}

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
}
