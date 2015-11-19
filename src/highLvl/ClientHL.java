package highLvl;

import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

import java.io.*;

/**
 * ClientHL est la classe representant le client de l'implementation de haut
 * niveau.
 * 
 * @author Damien/Dan
 * @param socket
 *            c'est le socket qui relit le serveur au client (telnet)
 * @param printStream
 *            est le flux de sortit
 * @param bufferedReader
 *            est le flux d'entré
 * @param serverEcho
 *            est le serveur qui a créé le ClientHL
 */
public class ClientHL implements Runnable {
	private Socket socket;
	private PrintStream printStream;
	private BufferedReader bufferedReader;
	private static ServerEchoHL serverEcho;

	/**
	 * la methode ClienHL est le constructeur de la classe ClientHL
	 * 
	 * @param s
	 *            est le socket par le quel le client va dialoguer avec le
	 *            serveur
	 * @param serverE
	 *            est le serveur qui a créé le Client
	 */
	public ClientHL(Socket s, ServerEchoHL serverE) {
		serverEcho = serverE;
		socket = s;
	}

	/**
	 * La methode run est la methode executé l'orsqu'un client ce connect au
	 * serveur
	 */
	public synchronized void run() {
		try {
			String message;
			Timer t;
			TimerTask tt;

			printStream = new PrintStream(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			printStream.println("Connection au serveur, ce serveur vous renvoi tout ce que vous ecrivez");
			printStream.println("Ecrivez deconnection pour vous deconnectez du serveur");

			while (true) {
				t = new Timer();
				tt = new TimerTask() {
					@Override
					public void run() {
						printStream.println("Time out : vous allez etre deconnecte");
						deconnexion(socket);
					}
				};
				t.schedule(tt, serverEcho.maxIdleTime);
				message = bufferedReader.readLine();
				t.cancel();
				if (message.equals("deconnection")) {
					deconnexion(socket);
				}
				printStream.println(message);
			}
		} catch (Exception e) {
			System.out.println("Client deconnecté pour cause d'inactivité");
		}
	}

	/**
	 * La methode deconnexion est juste chargé de deconnecter le socket
	 * 
	 * @param s
	 *            est le socket à deconnecter
	 */
	public static void deconnexion(Socket s) {
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}