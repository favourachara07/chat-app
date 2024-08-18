package client;
// The ChatClient class establishes a connection to the chat server at the specified address and port.
// It reads messages from the console and sends them to the server.
// It also listens for messages from the server and prints them to the console.
// The client runs in a loop until the user types "exit".
// Add a Constructor Parameter: Modify the constructor to accept a Consumer<String> parameter. This consumer will be called with incoming messages from the server.
// Handle Incoming Messages: Inside the startClient method, read messages from the server in a loop. For each message received, call the consumer passed in the constructor.
// The ChatClientGUI class creates an instance of ChatClient in its constructor. It passes a lambda function that appends received messages to the messageArea.
// The ChatClient class uses the passed consumer to handle incoming messages, effectively linking the networking code with the GUI.

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private Consumer<String> onMessageReceived;

  public ChatClient(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException {
      this.socket = new Socket(serverAddress, serverPort);
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.onMessageReceived = onMessageReceived;
  }

  public void sendMessage(String msg) {
      out.println(msg);
  }

  public void startClient() {
      new Thread(() -> {
          try {
              String line;
              while ((line = in.readLine()) != null) {
                  onMessageReceived.accept(line);
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }).start();
  }
}