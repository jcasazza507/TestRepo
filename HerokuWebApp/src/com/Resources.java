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

@Path("resources")
public class Resources {
	
	@POST
	@Path("putFile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON) //Maybe not?? I just don't think an int primitive will work with JSON
	public FTPResponse putFile(Upload upload)
	{ 
		FTPClientHandler ftpCH = new FTPClientHandler();
		FTPResponse response;
		if(ftpCH.CONNECT(upload.getServer(), upload.getPort(), upload.getUsername(), upload.getPassword())) {
			//System.out.println("Connecting worked!!");
			response.setCode() = ftpCH.getFTP().getReplyCode();
			try {
				InputStream is = IOUtils.toInputStream(upload.getFilecontents(), "UTF-8");
				ftpCH.getFTP().enterLocalPassiveMode();
				ftpCH.getFTP().storeFile(upload.getFilename(), is);
				response.setCode() = ftpCH.getFTP().getReplyCode();
				is.close();
			}
			catch(IOException ioe) {
				System.out.println("An error occurred while uploading file " + filename + ".");
			}
		}
		else {
			response.setCode() = ftpCH.getFTP().getReplyCode();
		}
		ftpCH.DISCONNECT();
		return response;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getFile/{filename}/{server}/{port}/{username}/{password}")
	public FileResult getFile(@PathParam("{filename}") String filename, @PathParam("{server}") String server, @PathParam("{port}") int port, @PathParam("{username}") String username, @PathParam("{password}") String password)
	{ 
		System.out.println("Server: " + server);
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