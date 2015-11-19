package highLvl;

import java.net.*;
import java.util.Timer;
import java.util.TimerTask;


import java.io.*;

public class ClientHL  implements Runnable{
	private Socket socket;
	private PrintStream printStream; 
    private BufferedReader bufferedReader; 
    private static ServerEchoHL serverEcho;

    public ClientHL(Socket s, ServerEchoHL serverE){
    	serverEcho = serverE;
		socket = s;
	}
	
	public synchronized void run() {
		try{
			String message;
			Timer t;
			TimerTask tt;
			
			printStream = new PrintStream(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
	    	printStream.println("Connection au serveur, ce serveur vous renvoi tout ce que vous ecrivez");
	    	printStream.println("Ecrivez deconnection pour vous deconnectez du serveur");
	      
	    	while (true)
	    	{
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
		         if(message.equals("deconnection")){
		         	deconnection(socket);
		         }
		         printStream.println(message);
	    	}
	    }
	    catch (Exception e){
	    }
	}
	
	public static void deconnection(Socket s){
		  try {
			s.close();
		} catch (IOException e) {}
	  }
}