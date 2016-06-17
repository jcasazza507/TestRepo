package com;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import org.apache.commons.io.IOUtils;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("web service")
public class WebService {
	
	@POST
	@Path("putFile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN) //Maybe not?? I just don't think an int primitive will work with JSON
	public int putFile(String filename, String filecontents, String server, int port, String username, String password)
	{ 
		FTPClientHandler ftpCH = new FTPClientHandler();
		int replyCode;
		if(ftpCH.CONNECT(server, port, username, password)) {
			//System.out.println("Connecting worked!!");
			replyCode = ftpCH.getFTP().getReplyCode();
			try {
				InputStream is = IOUtils.toInputStream(filecontents, "UTF-8");
				ftpCH.getFTP().enterLocalPassiveMode();
				ftpCH.getFTP().storeFile(filename, is);
				replyCode = ftpCH.getFTP().getReplyCode();
				is.close();
			}
			catch(IOException ioe) {
				System.out.println("An error occurred while uploading file " + filename + ".");
			}
		}
		else {
			replyCode = ftpCH.getFTP().getReplyCode();
		}
		ftpCH.DISCONNECT();
		return replyCode;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getFile/{filename}/{server}/{port}/{username}/{password}")
	public FileResult getFile(@PathParam("{filename}") String filename, @PathParam("{server}") String server, @PathParam("{port}") int port, @PathParam("{username}") String username, @PathParam("{password}") String password)
	{ 
		FTPClientHandler ftpCH = new FTPClientHandler();
		FileResult fr = new FileResult(filename, null);
		try {
			if(ftpCH.CONNECT(server, port, username, password)) {
				ftpCH.getFTP().enterLocalPassiveMode();
				InputStream is = ftpCH.getFTP().retrieveFileStream(filename);
				if (is == null) {
					System.out.println("File " + filename + " could not be found.");
				}
				else {
					fr.setFilecontents(IOUtils.toString(is, "UTF-8"));
					is.close();
				}
			}
		}
		catch(IOException ioe) {
			System.out.println("An error occurred while retrieving file " + filename);
		}
		ftpCH.DISCONNECT();
		return fr;
	}	
}