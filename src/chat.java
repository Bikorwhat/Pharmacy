import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter; 
import java.net.Socket;
public class chat { 
	private Socket socket;
private BufferedReader serverInput; 
private PrintWriter serverOutput;
private BufferedReader clientInput; 
private PrintWriter clientOutput;

public chat(String serverAddress, int port) {
	try {

// Connect to the server 
		socket = new Socket(serverAddress, port);
System.out.println("Connect to the server: " + socket.getInetAddress() +  + socket.getPort());
// Initialize input and output streams
serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
serverOutput = new PrintWriter(socket.getOutputStream(), true);
clientInput = new BufferedReader(new InputStreamReader(System.in)); 
clientOutput = new PrintWriter(socket.getOutputStream(), true);
// Start reading messages from the server and the client
Thread serverReadThread = new Thread(this::readServerMessages);
Thread clientReadThread = new Thread(this::readClientMessages); 
serverReadThread.start();
clientReadThread.start();
// Start sending messages from the client 
sendClientMessages();
} catch (IOException e) {
e.printStackTrace();
} finally { try {
// Close the socket when done 
	if (socket != null) socket.close();
} catch (IOException e) {
e.printStackTrace();
}}}
private void readServerMessages() { try {
String message; 
while ((message = serverInput.readLine()) != null) {
System.out.println("Server:"+  message);
}
}
catch (IOException e) {
e.printStackTrace();}}
private void readClientMessages() { try {
String message; 
while ((message = clientInput.readLine()) != null) {
	clientOutput.println(message);
}
} catch (IOException e) {
e.printStackTrace();
}}
private void sendClientMessages() { try {
String message;
while ((message = clientInput.readLine()) != null) {
	clientOutput.println(message);
}
} catch (IOException e) {
e.printStackTrace();
}
}
public static void main(String[] args) {
chat client = new chat("localhost", 1521);
}

}