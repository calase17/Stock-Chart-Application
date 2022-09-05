package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerMain {

    public static int port = 8888;
    public static final Ball ball = new Ball();
    // idea for using map to store the client PrintWriters taken from https://github.com/AlexFlorides/Virtual_Ball/blob/master/Server(Java)/src/Server.java
    public static final Map<Integer, PrintWriter> writers = new TreeMap<>();


    private static void StartServer(){
        ServerSocket serverSocket = null;
        try{
            System.out.println("Waiting for players to join... ");
            serverSocket = new ServerSocket(port);

            while (true){
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, ball);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        StartServer();
    }
}
