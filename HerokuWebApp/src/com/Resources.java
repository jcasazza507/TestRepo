/**
 *
 * @author evan marian
 *
 **/

package com;

import java.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import org.apache.commons.io.IOUtils;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("resources")
public class Resources {
	
	@POST
	@Path("putFile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public FTPResponse putFile(@HeaderParam("authToken") String token, Upload upload)
	{   
		if (!token.equals(System.getenv("auth_token")))
		{
			return new FTPResponse(401, "Authentication error");
		}
		String decodedString = "";
		FTPSClientHandler ftpsCH = new FTPSClientHandler();
		FTPResponse response = new FTPResponse();
		if (upload == null)
		{
			System.err.println("Passed improper arguments.");
			return response;
		}
		
		//Decode filecontents
		try
		{
			byte[] decoded = Base64.getDecoder().decode(upload.getFilecontents());
			decodedString = new String(decoded);
		}
		catch(IllegalArgumentException iae)
		{
			System.err.println("File contents are not encoded properly.");
			return response;
		}
		
		if(ftpsCH.CONNECT(upload.getServer(), upload.getPort(), upload.getUsername(), upload.getPassword())) {
			response.setCode(ftpsCH.getFTPS().getReplyCode());
			response.setMessage(ftpsCH.getFTPS().getReplyString());
			try {
				InputStream is = IOUtils.toInputStream(decodedString, "UTF-8");
				ftpsCH.getFTPS().enterLocalPassiveMode();
				ftpsCH.getFTPS().storeFile(upload.getFilename(), is);
				response.setCode(ftpsCH.getFTPS().getReplyCode());
				response.setMessage(ftpsCH.getFTPS().getReplyString());
				is.close();
			}
			catch(IOException ioe) {
				System.err.println("An error occurred while uploading file " + upload.getFilename() + ".");
			}
		}
		else {
			response.setCode(ftpsCH.getFTPS().getReplyCode());
			response.setMessage(ftpsCH.getFTPS().getReplyString());
		}
		ftpsCH.DISCONNECT();
		return response;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getFile/{filename}/{server}/{port}/{username}/{password}")
	public FileResult getFile(@HeaderParam("authToken") String token, @PathParam("filename") String filename, @PathParam("server") String server, @PathParam("port") int port, @PathParam("username") String username, @PathParam("password") String password)
	{ 
		if (!token.equals(System.getenv("auth_token")))
		{
			System.out.println("401 - An error occured during authentication");
			return new FileResult(filename, null);
		}
		FTPSClientHandler ftpsCH = new FTPSClientHandler();
		FileResult fr = new FileResult(filename, null);
		try {
			if(ftpsCH.CONNECT(server, port, username, password)) {
				ftpsCH.getFTPS().enterLocalPassiveMode();
				InputStream is = ftpsCH.getFTPS().retrieveFileStream(filename);
				if (is == null) {
					System.err.println("File " + filename + " could not be found.");
				}
				else {
					fr.setFilecontents(IOUtils.toString(is, "UTF-8"));
					is.close();
				}
			}
		}
		catch(IOException ioe) {
			System.err.println("An error occurred while retrieving file " + filename);
		}
		ftpsCH.DISCONNECT();
		return fr;
	}	
}