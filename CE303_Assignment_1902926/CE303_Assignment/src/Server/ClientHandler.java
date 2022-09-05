package Server;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private Ball ball;
    private PrintWriter writer;
    private Scanner scanner;

    public ClientHandler(Socket socket, Ball ball){
        this.socket = socket;
        this.ball = ball;

    }

    @Override
    public void run() {
        int playerId = 0;
        try{
                scanner = new Scanner(socket.getInputStream());
                writer = new PrintWriter(socket.getOutputStream(),true);

                playerId = ball.assignId();
                System.out.println("New Player has joined with ID " + playerId);
                for (Map.Entry<Integer, PrintWriter> wr : ServerMain.writers.entrySet()){
                    wr.getValue().println("New Player has joined with ID " + playerId);
                }
                ServerMain.writers.put(playerId, writer);
                writer.println("You are ID " + playerId);
                List<PlayerSession> listOfPlayers = ball.getListOfPlayers();
                for (PlayerSession playerSession : listOfPlayers){
                    System.out.println("Player with ID " + playerSession.getId() + " is currently in the game.");
                    writer.println("Player with ID " + playerSession.getId() + " is currently in the game.");
                }

                if (ball.hasBall(playerId)){
                    System.out.println("Player with ID " + playerId + " has the ball." );
                }
                while (true) {

                    List<PlayerSession> playerSessionList = ball.getListOfPlayers();
                    if (playerSessionList.size() == 1){
                        ball.giveBall(playerId, playerId);
                    }

                    for (PlayerSession playerSession1 : playerSessionList){
                        if (playerSession1.getHasBall()){
                            ServerMain.writers.get(playerSession1.getId()).println("Who would you like to pass the ball to. ");
                        }
                    }

                    String line = scanner.nextLine();
                    String[] args = line.split(" ");
                    switch (args[0].toLowerCase()) {
                        case "id":
                            if (ball.hasBall(playerId)){
                                int playerToPassTo;
                                try {
                                    playerToPassTo = Integer.parseInt(args[1]);
                                    ball.giveBall(playerId, playerToPassTo);
                                }
                                catch (Exception e){
                                    writer.println("player you are trying to pass to doesn't exist");
                                    ball.giveBall(playerId,playerId);
                                    continue;
                                }


                            System.out.println("Player with ID " + playerId + " has passed the ball to " + playerToPassTo);
                            for (Map.Entry<Integer, PrintWriter> wr : ServerMain.writers.entrySet()){
                                wr.getValue().println("Player with ID " + playerId + " has given the ball to Player with ID " + playerToPassTo);
                            }

                            }
                            else{
                                writer.println("You do not have the ball. ");
                                break;
                            }
                            break;
                        default:
                            throw new Exception("Unknown command: " + line);
                    }

                }

            }
            catch (Exception e){
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        finally {
            boolean hasBall1 = false;
            if (ball.hasBall(playerId)){
                hasBall1 = true;
            }
            ball.removePlayer(playerId);
            // iterating through printwriters to send broadcast messages to clients from https://github.com/AlexFlorides/Virtual_Ball/blob/master/Server(Java)/src/Server.java
            for (Map.Entry<Integer, PrintWriter> wr : ServerMain.writers.entrySet()){
                wr.getValue().println("Player with ID " + playerId + " has left the game." );
            }
            System.out.println("Player " + playerId + " has left the game.");
            ServerMain.writers.remove(playerId);
            List<PlayerSession> listOfPlayers = ball.getListOfPlayers();
            if(listOfPlayers != null){
                for (PlayerSession playerSession : listOfPlayers){
                    System.out.println("Player with ID " + playerSession.getId() + " is currently in the game.");
                }
                if (hasBall1){
                    Random random = new Random();
                    int randIndex = random.nextInt(listOfPlayers.size());
                    int randId = listOfPlayers.get(randIndex).getId();
                    ball.giveBall(0, randId);
                    for (PlayerSession playerSession : listOfPlayers){
                        if (playerSession.getHasBall()){
                            System.out.println("Player with ID " + playerSession.getId() + " now has the ball.");
                            for (Map.Entry<Integer, PrintWriter> wr : ServerMain.writers.entrySet()){
                                wr.getValue().println("Player with ID " + playerSession.getId() + " now has the ball.");
                            }

                        }
                    }
                }
            }
        }
    }

}
