import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.Date;
import java.util.Scanner;

/**
 * This class listens for incoming client connections and executes Linux commands
 * based on the commands received from the clients. It sends the output of the commands
 * back to the clients.
 */

public class ServerP2 {
	
	private static boolean connection = true;
	public static int counter = 0;
	
	public static void main(String[] args) throws IOException{
    	Scanner in = new Scanner(System.in);
    	System.out.println("Enter the port number to listen:");//get the port number
    	int port = in.nextInt();
    	
    	try(ServerSocket serverSocket = new ServerSocket(port))//Start listening if the port number is valid
    	{
    		System.out.println("Listening on port: " + port);
    		
    		while(connection) {
    			Socket socket = serverSocket.accept();//accept the connection made by various clients
    			System.out.println(counter + ") Client connected to this server with port number: " + port);
    			ServerThread serverThread = new ServerThread(socket);//generate a thread to answer the client 
    			serverThread.start();
    			counter++; //count the number of instructions
    		}//end while
    		serverSocket.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}//end try catch
    	in.close();
    }//end main
	
	public static void endConnection() {
		connection = false;
	}
}//end class