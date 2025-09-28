import java.net.Socket;
import java.util.Scanner;
import java.io.*;

/**
 * This class connects to the server, sends a command option, and receives the corresponding
 * output from the server. It also measures and displays the turn-around time for each request.
 */
public class ClientP2{
	public static void main(String[] args) throws IOException{
		Scanner in = new Scanner(System.in);

		//Query user for server’s IP address
		System.out.println("Please enter the Network Address (IP) of the Server:");
		String serverIPAddress = in.next();

		//Query user for server port
		System.out.println("Please enter the Port Number for the server to Listen:");
		int portNumber = in.nextInt();
		boolean canConnect = false;

		// Create socket(address, port)
		try (Socket socket = new Socket(serverIPAddress, portNumber)) {
			canConnect = true;//try a connection and if its possible enable the menu and connection for the client.
		}catch(Exception e) {
			e.printStackTrace();
		}

		if(canConnect == true){
			//loop for sending commands to the server
			do {
				//present menu of commands to the user
				System.out.println("Please Enter Number Corresponding to the Desired Command");
				System.out.println("1 - Date and Time");
				System.out.println("2 - Uptime");
				System.out.println("3 - Memory Use ");
				System.out.println("4 - Netstat");
				System.out.println("5 - Current Users");
				System.out.println("6 - Running Processes");
				System.out.println("7 - Exit Program");

				// fetch command to run (command) from the user
				int command = in.nextInt();

				//Stop program execution
				if(command == 7){
					try (Socket socket = new Socket(serverIPAddress, portNumber)) {
						PrintWriter serverWriterExit = new PrintWriter(socket.getOutputStream(), true);// Object to send command to the Server
						serverWriterExit.println(command);
						serverWriterExit.flush();
						System.out.println("Connection Terminated");
						break;
					}catch(Exception e) {
						e.printStackTrace();
					}	
				}

				System.out.println("Number of client requests: 1, 5, 10, 15, 20, 25, 100");
				int numThreads = in.nextInt();//get the amount of threads to be run by the client

				long totalTurnAroundTime = 0;


				// Send command number to the Server
				for(int i = 0; i < numThreads; i++){
					try {
						long startTime = System.nanoTime();// Start time measurement of the thread execution
						ClientThread t = new ClientThread(serverIPAddress, portNumber, command);
						t.start(); // start thread
						t.join();
						long endTime = System.nanoTime(); // End time measurement of the thread
						long turnAroundTime = endTime - startTime; // Elapsed time in nanoseconds
						totalTurnAroundTime += turnAroundTime;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}// end for loop

				// Read the complete response from the server until "END_OF_OUTPUT" marker
				System.out.println("Average Turn Around Time: " + totalTurnAroundTime /(numThreads * 1000000L) + "ms"); // calculate and print average time
				// Calculate and display the total turn-around times
				System.out.println("Total turn around Time: " + (totalTurnAroundTime / 1000000) + " ms");// display time in milliseconds

			}while(true);//end loop
		}//end if
		else {
			System.out.println("ERROR: Provide a valid IP Address and a Port Number");
		}
		in.close();
	}//end main
}//end class

class ClientThread extends Thread {
	private String serverIPAddress;
	private int port;
	private int commandNum;

	public ClientThread(String serverIPAddress,Integer port, Integer commandNum){
		this.setServerIPAddress(serverIPAddress);
		this.setPort(port);
		this.commandNum = commandNum;
	}
	public void run() {
		try (Socket socket = new Socket(serverIPAddress, port)){
			PrintWriter tempWriter = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader reader = new BufferedReader (new InputStreamReader(socket.getInputStream()));// Object to read input from the Client
			tempWriter.println(commandNum);
			String line;
			while ((line = reader.readLine()) != null && !line.equals("END_OF_OUTPUT")) {// Receive data from the server
				System.out.println(line);
			}//end while
			tempWriter.flush();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// Object to send command to the Server
	}
	public String getServerIPAddress() {
		return serverIPAddress;
	}
	public void setServerIPAddress(String serverIPAddress2) {
		this.serverIPAddress = serverIPAddress2;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}