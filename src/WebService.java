import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.POST;

import org.apache.commons.io.IOUtils;

public class WebService {
	
	private FTPClientHandler ftpCH;
	
	public WebService() 
	{ 
		ftpCH = new FTPClientHandler();
	}
	
	@POST
	public int putFile(String filename, String fileContents, String server, int port, String username, String password)
	{ 
		int replyCode;
		if(ftpCH.CONNECT(server, port, username, password)) {
			//System.out.println("Connecting worked!!");
			replyCode = ftpCH.getFTP().getReplyCode();
			try {
				InputStream is = IOUtils.toInputStream(fileContents, "UTF-8");
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
	
	@POST
	public FileResult getFile(String filename, String server, int port, String username, String password)
	{ 
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