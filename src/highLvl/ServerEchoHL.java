package highLvl;


import java.net.*;
import java.util.concurrent.*;

public class ServerEchoHL {

	public int maxIdleTime;
	private int port;
	private int nbMaxConnexion;
		
	public ServerEchoHL (int maxIdleTime, int port,  int nbMaxConnexion){
		this.nbMaxConnexion = nbMaxConnexion;
		this.maxIdleTime = maxIdleTime;
		this.port = port;
	}
	
	public void launch() {
		ServerSocket serverSocket;
		ExecutorService execService;
		
		try {
			serverSocket = new ServerSocket(this.port);
			execService = Executors.newFixedThreadPool(this.nbMaxConnexion);
			while (true){
				execService.execute(new ClientHL(serverSocket.accept(), this));
			}
		} catch (java.io.IOException e){
			System.out.println("Erreur : " + e.toString());
		}
	}
}
