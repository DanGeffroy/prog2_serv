package highLvl;

import java.net.*;
import java.util.concurrent.*;

/**
 * ServeurEchoHL est l'implementation de haut niveau du serveur
 * 
 * @author Damien/Dan
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
public class ServerEchoHL {

	public int maxIdleTime;
	private int port;
	private int nbMaxConnexion;

	/**
	 * ServeurEchoHL est le constructeur de la classe ServeurEchoHL
	 * 
	 * @param maxIdleTime
	 *            temps de connexion max sans activité
	 * @param port
	 *            port du serveur
	 * @param nbMaxConnexion
	 *            nombre maximum de connexion simultané
	 */
	public ServerEchoHL(int maxIdleTime, int port, int nbMaxConnexion) {
		this.nbMaxConnexion = nbMaxConnexion;
		this.maxIdleTime = maxIdleTime;
		this.port = port;
	}

	/**
	 * launch est la methode appelé par la classe Main pour lancer le serveur.
	 */
	public void launch() {
		ServerSocket serverSocket;
		ExecutorService execService;

		try {
			serverSocket = new ServerSocket(this.port);
			/*
			 * On fixe le nombre de connexion simultané directement dans le
			 * thread pool
			 */
			execService = Executors.newFixedThreadPool(this.nbMaxConnexion);
			while (true) {
				execService.execute(new ClientHL(serverSocket.accept(), this));
			}
		} catch (java.io.IOException e) {
			System.out.println("Erreur : " + e.toString());
		}
	}
}
