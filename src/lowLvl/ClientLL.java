package lowLvl;

import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

class ClientLL implements Runnable
{
  private Thread thread;
  private Socket socket;
  private PrintStream printStream;
  private BufferedReader bufferedReader;
  private static ServerEchoLL serverEcho;

  ClientLL(Socket s, ServerEchoLL serverE)
  {
	serverEcho =serverE;
    socket=s;
    try
    {
      printStream = new PrintStream(socket.getOutputStream());
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    catch (IOException e){ }
    
    thread = new Thread(this);
    thread.start();
  }

  public void run()
  {
    String message;
    
	Timer t;
	TimerTask tt;
	
    try{
    	printStream.println("Connection au serveur, ce serveur vous renvoi tout ce que vous ecrivez");
    	printStream.println("Ecrivez deconnection pour vous deconnectez du serveur");
      while(true)
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
		serverEcho.deconnection();
	} catch (IOException e) {}
  }
  
}
