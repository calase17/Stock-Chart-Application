package Client;


import java.util.Scanner;


public class ClientMain {

    public static void main(String[] args) throws Exception {
        Player player = new Player();
        while (true){
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("exit")){
                System.exit(0);
            }
            else{
                player.passBall(command);
            }
        }
    }


}

