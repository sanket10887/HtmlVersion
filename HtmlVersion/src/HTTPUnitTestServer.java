import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.JsonArray;

public class HTTPUnitTestServer {

	
	Socket connectedClient = null;
	DataOutputStream outToClient = null;
	PrintWriter socketWriter;
	SystemTray tray;
	int port=5000;
	

	public HTTPUnitTestServer(JsonArray jsonStr) {

		try {
			
			ParseJSON parseJSONToHTML=new ParseJSON(jsonStr);
			parseJSONToHTML.createHTML();
			StreamServer streamServer=new StreamServer(port);
			new Thread(streamServer).start();
			tray = SystemTray.getSystemTray();
			Image img = Toolkit.getDefaultToolkit().getImage("web/images/test.png");
			
			PopupMenu popup = new PopupMenu();
			TrayIcon trayIcon = new TrayIcon(img, "Server Started", popup);
			
			try {
				tray.add(trayIcon);
			} catch (AWTException e1) {}
			
			MenuItem exitMenu = new MenuItem("Exit");
			exitMenu.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			
			
			popup.add(exitMenu);
			
			trayIcon.displayMessage("Server Started Successfully", "\tPort No: "+port, TrayIcon.MessageType.INFO);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void closeStreams() throws Exception {
		outToClient.close();
		connectedClient.close();
	}
	
}
