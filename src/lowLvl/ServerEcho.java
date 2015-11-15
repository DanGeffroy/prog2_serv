package lowLvl;

import java.net.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class ServerEcho {

	int nbConnectionActuel = 0;
	int maxIdleTime;
	private static ServerSocket serverSocket;

	public static void main(String args[]) {
		PrintStream printStream;
		Socket socket;

		int port = 0;
		int nbMaxConnection = 0;
		String implementation = null;

		ServerEcho serverEcho = new ServerEcho();

		JSONParser parser = new JSONParser();

		try {
			/*On recupere le fichier de configuration du serveur*/
			Object obj = parser.parse(new FileReader("config.txt"));
			JSONObject jsonObject = (JSONObject) obj;
			implementation = (String) jsonObject.get("Implementation");
			nbMaxConnection = Integer.parseInt((String) jsonObject.get("MaxThread"));
			serverEcho.maxIdleTime = Integer.parseInt((String) jsonObject.get("MaxIdleTime")) * 1000;
			port = Integer.parseInt((String) jsonObject.get("Port"));

		} catch (Exception e) {
			/*Si le fichier de configuration n'est pas present ou mal formé
			 * On set des valeur par defaut pour le serveur
			 * */
			implementation = "1";
			nbMaxConnection = 2;
			serverEcho.maxIdleTime = 10 * 1000;
			port = 9999;
		} finally {
			System.out.println("Implémentation: " + implementation);
			System.out.println("Nombre co maximum : " + nbMaxConnection);
			System.out.println("Temps maximum d'inactivité: " + serverEcho.maxIdleTime);
			System.out.println("Port d'écoute: " + port);
		}

		try {

			serverSocket = new ServerSocket(port);
			while (true) {
				if (serverEcho.nbConnectionActuel < nbMaxConnection) {
					new Client(serverSocket.accept(), serverEcho);
					serverEcho.nbConnectionActuel++;
					System.out.println("Nombre actuel de client connecté : " + serverEcho.nbConnectionActuel);
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
