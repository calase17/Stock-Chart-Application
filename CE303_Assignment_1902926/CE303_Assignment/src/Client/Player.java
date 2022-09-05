package Client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player implements AutoCloseable{
    private final int port = 8888;
    private final Scanner reader;
    private final PrintWriter writer;

    public Player() throws Exception {

        Socket socket = new Socket("localhost", port );

        reader = new Scanner(socket.getInputStream());

        writer = new PrintWriter(socket.getOutputStream(), true);

        Thread t1 = new Thread(new Input(reader));
        t1.start();

    }



    public void passBall(String player){
        writer.println("id " + player);
    }


    @Override
    public void close() throws Exception {
        reader.close();
        writer.close();
    }
}
