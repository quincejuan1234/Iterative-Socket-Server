import java.net.Socket;
import java.util.Scanner;
import java.net.*;
import java.io.*;

/**
 * This class connects to the server, sends a command option, and receives the corresponding
 * output from the server. It also measures and displays the turn-around time for each request.
 */
public class Client {
    public static void main(String[] args) throws IOException{
        Scanner in = new Scanner(System.in);

        //Query user for server’s IP address
        System.out.println("Please enter the Network Address (IP) of the Server:");
        String serverIPAddress = in.next();

        //Query user for server port
        System.out.println("Please enter the Port Number for the server to Listen:");
        int portNumber = in.nextInt();

        // Create socket(address, port)
        try (Socket socket = new Socket(serverIPAddress, portNumber)) {
            // Create output and input streams to share information in the connection
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream());// Object to send command to the Server
            BufferedReader reader = new BufferedReader (new InputStreamReader(socket.getInputStream()));// Object to read input from the Client

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
                    serverWriter.println(command);
                    serverWriter.flush();
                    break;
                }

                //query for how many times to run command(number)
                System.out.println("Please Enter the Number of Requests");
                System.out.println("Options: 1, 5, 10, 15, 20, 25");
                int numThreads = in.nextInt();// get number of requests(threads) from the user

                long totalTurnAroundTime = 0;

                //make some threads
                //run threads
                //for(int i = 0; i < numThreads; i++) {
                long startTime = System.nanoTime(); // Start time measurement

                // Send command number to the Server
                serverWriter.println(command);
                serverWriter.flush();

                // Read the complete response from the server until "END_OF_OUTPUT" marker
                String line;
                while ((line = reader.readLine()) != null && !line.equals("END_OF_OUTPUT")) {// Receive data from the server
                    System.out.println(line);
                }//end while

                long endTime = System.nanoTime(); // End time measurement
                long turnAroundTime = endTime - startTime; // Elapsed time in nanoseconds
                totalTurnAroundTime += turnAroundTime;
                System.out.println("Turn around time: " + (turnAroundTime / 10000000) + " ms");// display time in milliseconds
                    //System.out.println("Turn-around Time for request " + (i + 1) + ": " + turnAroundTime + " nanoseconds");
                //}

                // Calculate and display the total turn-around times
                System.out.println("Total turn around Time: " + (totalTurnAroundTime / 1000000) + " ms");// display time in milliseconds
                //calculate average time
                //System.out.println("Average Turn-around Time: " + ((totalTurnAroundTime / numThreads) / 1000000) + " nanoseconds");// display time in milliseconds


            }while(true);//end loop
        }//end try
        catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        }
        catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }//end main
    /***/
    //public ??? Thread_handler(){}
}//end class
