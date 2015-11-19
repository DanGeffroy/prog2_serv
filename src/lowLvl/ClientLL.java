package lowLvl;

import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

/**
 * ClientHLL est la classe representant le client de l'implementation de bas
 * niveau.
 * 
 * @author Damien/Dan
 * @param thread
 *            est le thread de la classe ClientLL
 * @param socket
 *            est le socket qui relit le serveur au client (telnet)
 * @param printStream
 *            est le flux de sortit
 * @param bufferedReader
 *            est le flux d'entré
 * @param serverEcho
 *            est le serveur qui a créé le ClientHL
 */
class ClientLL implements Runnable {
	private Thread thread;
	private Socket socket;
	private PrintStream printStream;
	private BufferedReader bufferedReader;
	private static ServerEchoLL serverEcho;

	/**
	 * la methode ClienLL est le constructeur de la classe ClientLL
	 * 
	 * @param s
	 *            est le socket par le quel le client va dialoguer avec le
	 *            serveur
	 * @param serverE
	 *            est le serveur qui a créé le Client
	 */
	ClientLL(Socket s, ServerEchoLL serverE) {
		serverEcho = serverE;
		socket = s;
		try {
			printStream = new PrintStream(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		thread = new Thread(this);
		thread.start();
	}

	/**
	 * La methode run est la methode executé l'orsqu'un client ce connect au
	 * serveur
	 */
	public void run() {
		String message;

		Timer t;
		TimerTask tt;

		try {
			printStream.println("Connection au serveur, ce serveur vous renvoi tout ce que vous ecrivez");
			printStream.println("Ecrivez deconnection pour vous deconnectez du serveur");
			while (true) {
				t = new Timer();
				tt = new TimerTask() {
					@Override
					public void run() {
						printStream.println("Time out : vous allez etre deconnecte");
						deconnection(socket);
					}
				};
				t.schedule(tt, serverEcho.maxIdleTime);
				message = bufferedReader.readLine();
				t.cancel();
				if (message.equals("deconnection")) {
					deconnection(socket);
				}
				printStream.println(message);
			}
		} catch (Exception e) {
			System.out.println("Client deconnecté pour cause d'inactivité");
		}

	}

	public static void deconnection(Socket s) {
		try {
			s.close();
			serverEcho.deconnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
