import highLvl.ServerEchoHL;

import java.io.FileReader;

import lowLvl.ServerEchoLL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

	public static void main(String[] args) {
		String implementation = null;
		int nbMaxConnexion = 0;
		int maxIdleTime = 0;
		int port = 0;

		JSONParser parser = new JSONParser();

		try {
			/* On recupere le fichier de configuration du serveur */
			Object obj = parser.parse(new FileReader("config.txt"));
			JSONObject jsonObject = (JSONObject) obj;

			implementation = (String) jsonObject.get("Implementation");
			nbMaxConnexion = Integer.parseInt((String) jsonObject
					.get("MaxThread"));
			maxIdleTime = Integer.parseInt((String) jsonObject
					.get("MaxIdleTime")) * 1000;
			port = Integer.parseInt((String) jsonObject.get("Port"));

		} catch (Exception e) {
			/*
			 * Si le fichier de configuration n'est pas present ou mal form� On
			 * set des valeur par defaut pour le serveur
			 */
			implementation = "lowlvl";
			nbMaxConnexion = 2;
			maxIdleTime = 10 * 1000;
			port = 9999;
		} finally {
			System.out.println("Implementation : " + implementation);
			System.out.println("Nombre connexion maximum : " + nbMaxConnexion);
			System.out.println("Temps maximum d'inactivite: " + maxIdleTime);
			System.out.println("Port d'ecoute: " + port);
		}

		if (implementation.equals("lowlvl")) {
			System.out.println("Lancement du serveur de bas niveau");
			ServerEchoLL servLow = new ServerEchoLL(maxIdleTime, port, nbMaxConnexion);
			servLow.launch();
		} else if (implementation.equals("highlvl")) {
			System.out.println("Lancement du serveur de haut niveau");
			ServerEchoHL servHigt = new ServerEchoHL(maxIdleTime, port, nbMaxConnexion);
			servHigt.launch();
		} else {
			System.out.println("Implementation " + implementation
					+ " est inconnue");
		}

	}

}
