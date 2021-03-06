package com;
import java.io.IOException;
import org.apache.commons.net.ftp.*;
/**
 * 
 * @author evan marian
 *
 * FTPSClientHandler creates an FTPSClient and implements CONNECT()
 * and DISCONNECT() methods that handle connecting, logging in, 
 * disconnecting and logging out of the server as well as any
 * exceptions thrown by these processes.
 */
public class FTPSClientHandler {
	
	private FTPSClient ftps;
	
	public FTPSClientHandler() {
		ftps = new FTPSClient(false);
	}
	
	public FTPSClient getFTPS() {
		return ftps;
	}
	
	public boolean CONNECT(String server, int port, String username, String password) {
		int reply;	
		try {
			this.ftps.connect(server, port);
			this.ftps.login(username, password);
			System.out.println("Connected to " + server + ".");
			System.out.print(ftps.getReplyString());
			//After connection attempt, check reply code to verify success
			reply = this.ftps.getReplyCode();
			
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.err.println("FTP server refused connection.");
				return false;
			}
			else return true;
		}
		catch (java.net.ConnectException ce) {
			System.err.println("Failed to connect to FTPS server.");
		}
		catch (java.net.UnknownHostException uhe) {
			System.err.println("Could not resolve server name.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean DISCONNECT() {
		if (this.ftps.isConnected()) {
			System.out.println("Logging out...");
			try {
				this.ftps.logout();
				System.out.println("Logout successful.");
			}
			catch(IOException ioeLO) {
				System.err.println("An error occurred while logging out");
			}
			System.out.println("Disconnecting...");
			try {
				this.ftps.disconnect();
			}
			catch (IOException ioeD) {
				System.err.println("An error occurred while disconnecting.");
				return false;
			}
			System.out.println("Disconnect successful.");
		}
		else
			System.out.println("Attempting to disconnect...\nAlready disconnected.");
		return true;
	}
}