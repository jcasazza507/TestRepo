public class Driver {
	public static void main(String[] args) {
		WebService wb = new WebService();
		System.out.println("Reply Code: " + wb.putFile("test", "fsdhjgljkf", "cazmedia.com", 21, "Shinedown@cazmedia.com", "Grand4orks@2016"));
		FileResult fr = wb.getFile("test", "cazmedia.com", 21, "Shinedown@cazmedia.com", "Grand4orks@2016");
		System.out.println("File: " + fr.getFilename());
		System.out.println("Contents: " + fr.getFilecontents());
	}
}