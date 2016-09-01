
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

final class HttpRequest implements Runnable {

	private static final String CONTENT_TYPE = "Content-type: ";
	private static final String CRLF = "\r\n";
	private static final byte[] HTTP_STATUS_200 = ("HTTP/1.0 200 OK" + CRLF).getBytes();
	private static final byte[] HTTP_STATUS_404 = ("HTTP/1.0 404 Not Found" + CRLF).getBytes();

	Socket socket;
	FileInputStream fis = null;
	OutputStream os;
	InputStream is;

	boolean fileExists = true;

	public HttpRequest() throws Exception {
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			processRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processRequest() throws Exception {
		is = this.socket.getInputStream();
		os = socket.getOutputStream();

		String requestLine = "";

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		requestLine = br.readLine();
		System.out.println("rquestline :---------> " + requestLine.trim());

		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken(); 
		String fileName = tokens.nextToken();
		if (fileName.startsWith("/"))
			fileName = fileName.substring(1, fileName.length());
		if (fileName.length() == 0)
			fileName = "index.html";

		try {
			fis = new FileInputStream("web\\" + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fileExists = false;
		}

		byte statusLine[] = null;
		String contentTypeLine = null;
		String entityBody = null;

		if (fileExists) {
			statusLine = HTTP_STATUS_200;
			contentTypeLine = CONTENT_TYPE + contentType(fileName) + CRLF;
			os.write(statusLine);
			os.write(contentTypeLine.getBytes());
			os.write(CRLF.getBytes());
			try {
				sendBytes(fis, os);
				fis.close();
			} catch (Exception e) {
				System.out.println("file upload error : " + e);
			}

		} else {
			statusLine = HTTP_STATUS_404;
			contentTypeLine = "NONE";
			entityBody = "\n\n Not Found";
			os.write(statusLine);
			os.write(contentTypeLine.getBytes());
			os.write(CRLF.getBytes());
			os.write(entityBody.getBytes());
		}
		os.close();
		socket.close();

	}

	private static String contentType(String fileName) {
		if (fileName.endsWith(".htm") || fileName.endsWith(".html"))
			return "text/html";
		else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".strm"))
			return "image/jpeg";
		else if (fileName.endsWith(".png"))
			return "image/png";
		else if (fileName.endsWith(".gif"))
			return "image/gif";
		else if (fileName.endsWith(".txt"))
			return "text/plain";
		else if (fileName.endsWith(".js"))
			return "text/javascript";
		else if (fileName.endsWith(".css"))
			return "text/css";
		else
			return "application/octet-stream";
	}

	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
		// Construct a 1K buffer to hold bytes on their way to the socket.
		byte[] buffer = new byte[1024];
		int bytes = 0;

		// Copy requested file into the socket's output stream.
		while ((bytes = fis.read(buffer)) != -1)
			os.write(buffer, 0, bytes);
	}

}
