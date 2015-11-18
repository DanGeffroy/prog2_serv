package lowLvl;

import java.net.*;

import java.io.*;


public class ServerEchoLL {

	private int nbConnectionActuel;	
	public int maxIdleTime;
	private int port;
	private int nbMaxConnexion;
	private static ServerSocket serverSocket;
	
	
	public ServerEchoLL(int maxIdleTime, int port,  int nbMaxConnexion) {
		this.nbConnectionActuel = 0;
		this.nbMaxConnexion = nbMaxConnexion;
		this.maxIdleTime = maxIdleTime;
		this.port = port;
		this.nbMaxConnexion = nbMaxConnexion;
	}

	public void launch() {
		PrintStream printStream;
		Socket socket;

		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				if (this.nbConnectionActuel < this.nbMaxConnexion) {
					new ClientLL(serverSocket.accept(), this);
					this.nbConnectionActuel++;
					System.out.println("Nombre actuel de client connect� : " + this.nbConnectionActuel);
				} else {
					socket = serverSocket.accept();
					printStream = new PrintStream(socket.getOutputStream());
					printStream.println("Desole le nombre maximal de connection est atteint");
					socket.close();
				}
			}
		} catch (Exception e) {
			System.out.println("Erreur : " + e.toString());
		}
	}

	public void deconnection() {
		this.nbConnectionActuel--;
		System.out.println("Nombre actuel de client connecte : " + this.nbConnectionActuel);
	}

}
