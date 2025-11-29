# Iterative Socket Server

This repository contains a simple Java client/server pair for experimenting with remote command execution over TCP sockets. It includes an iterative server/client (one request at a time) and a multi-threaded variant for exercising concurrency.

## Project structure

- `Server.java` – single-connection server that executes a Linux command based on the client option and streams the output back.
- `Client.java` – interactive client that sends a single command at a time and prints the server response along with turn-around time.
- `ServerP2.java` – multi-threaded server that spawns a `ServerThread` for each incoming connection.
- `ServerThread.java` – worker thread that runs the requested command and returns the results to the client.
- `ClientP2.java` – client that can issue many concurrent requests (threads) to measure average round-trip time.

## Prerequisites

- Java 8+ (JDK) installed and available on the `PATH`.
- A Unix-like environment where the commands below (`uptime`, `free`, `netstat`, `who`, `ps`) are available.

## Building

Compile all classes from the repository root:

```bash
javac *.java
```

This produces the `.class` files needed to run the server and clients.

## Running the iterative server/client

1. **Start the server**

   ```bash
   java Server
   ```

   Enter the port to listen on when prompted.

2. **Run the client** (in a separate terminal)

   ```bash
   java Client
   ```

   Provide the server IP address and the same port number. Choose one of the menu options:

   | Option | Command run on server |
   | --- | --- |
   | 1 | Current date/time |
   | 2 | `uptime` |
   | 3 | `free -m` |
   | 4 | `netstat -an` |
   | 5 | `who` |
   | 6 | `ps -e` |
   | 7 | Disconnect |

   The client prints the server output until it sees the `END_OF_OUTPUT` marker and displays the turn-around time.

## Running the multi-threaded variant

1. **Start the server**

   ```bash
   java ServerP2
   ```

   Enter a port when prompted. Each connection is handled by a new `ServerThread`.

2. **Run the multi-threaded client**

   ```bash
   java ClientP2
   ```

   After selecting a command, you will be asked for the number of client requests (threads) to send concurrently (e.g., 5, 10, 20). The client reports the average and total turn-around time for the batch of requests.

## Notes

- Both clients terminate by choosing option `7`.
- Error details from executed commands are also forwarded to the client output.
- Commands are executed on the server host; run only in trusted environments.
