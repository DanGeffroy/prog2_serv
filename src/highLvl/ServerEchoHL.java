package highLvl;

import java.net.*;
import java.util.Collection;
import java.util.concurrent.*;

public class ServerEchoHL {

	private ServerSocket s;
	private Socket client;
	private ClientHL c;
	private ExecutorService pool;
	
	public ServerEchoHL (){
		//create the socket
		try {
				s = new ServerSocket(5555);
				pool = Executors.newFixedThreadPool(3);	
		}
		catch (java.io.IOException e){
				System.out.println(e);
				System.exit(1);
		}
		
		// OK, now listen for connections
		System.out.println("Server is listening.");
		
		try{
			while (true){
				client = s.accept();
				//create a separate thread to service the request
				c = new ClientHL(client);
				//new Thread(c).start();
				pool.execute(c);
			}
		}
		catch (java.io.IOException e){
			System.out.println(e);
		}
}
	
}
