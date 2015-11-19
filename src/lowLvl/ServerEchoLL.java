package lowLvl;

import java.net.*;

import java.io.*;

/**
 * ServeurEchoLL est la classe representant l'implementation de haut niveau du
 * serveur echo
 * 
 * @author Damien/Dan
 * @param nbConnectionActuel
 *            represent le nombre de connexion actuel sur le serveur
 * @param maxIdleTime
 *            il s'agit du temps avant la deconnexion par le serveur d'un
 *            utilisteur inactif, ce parametre est recupéré du fichier de
 *            configuration
 * @param port
 *            c'est le port du serveur, ce parametre est recupéré du fichier de
 *            configuration
 * @param nbMaxConnexion
 *            comme sont nom l'indique c'est le nombre maximum de connexion
 *            simultané au serveur ce parametre est recupérer du fichier de
 *            configuration
 */
public class ServerEchoLL {

	private int nbConnectionActuel;
	public int maxIdleTime;
	private int port;
	private int nbMaxConnexion;

	/**
	 * ServeurEchoLL est le constructeur de la classe ServeurEchoLL
	 * 
	 * @param maxIdleTime
	 *            temps de connexion max sans activité
	 * @param port
	 *            port du serveur
	 * @param nbMaxConnexion
	 *            nombre maximum de connexion simultané
	 */
	public ServerEchoLL(int maxIdleTime, int port, int nbMaxConnexion) {
		this.nbConnectionActuel = 0;
		this.nbMaxConnexion = nbMaxConnexion;
		this.maxIdleTime = maxIdleTime;
		this.port = port;
	}

	/**
	 * launch est la methode appelé par la classe Main pour lancer le serveur.
	 */
	public void launch() {
		PrintStream printStream;
		Socket socket;
		ServerSocket serverSocket;

		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				/* On gere le nombre de connexion maximum "à la main" */
				if (this.nbConnectionActuel < this.nbMaxConnexion) {
					new ClientLL(serverSocket.accept(), this);
					this.nbConnectionActuel++;
					System.out.println("Nombre actuel de client connectï¿½ : " + this.nbConnectionActuel);
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
