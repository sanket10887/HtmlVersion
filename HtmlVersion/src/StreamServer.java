

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public final class StreamServer implements Runnable {

	private static StreamServer instance = null;
	private ServerSocket listenSocket = null;
	private ExecutorService pool = null;
	private boolean running = false;
	public StreamServer(int port) {
		instance = this;
		try {
			System.out.println("Port:"+port);
			listenSocket = new ServerSocket(port);
			pool = Executors.newFixedThreadPool(15);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void run() {
		running = true;

		// Process HTTP service requests in an infinite loop
		try {
			while (running) {
				HttpRequest request = new HttpRequest();
				request.setSocket(listenSocket.accept());
				pool.execute(request);
			}
		} catch (Exception e) {
		} finally {
			shutdown();
		}
	}

	/**
	 * shutdown the stream server and stop streaming of all camera.
	 */
	public void shutdown() {
		running = false;
	}

	public static StreamServer getInstance() {
		return instance;
	}
	
	public boolean isRunning() {
		return running;
	}
}