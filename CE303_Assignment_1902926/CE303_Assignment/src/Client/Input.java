package Client;

// code taken from https://stackoverflow.com/questions/22301892/print-to-console-while-waiting-for-input-in-java
import java.util.Scanner;

public class Input extends Thread{
    Scanner scanner;

    public Input(Scanner scanner){
        this.scanner =  scanner;
    }

    @Override
    public void run() {
        while (scanner.hasNextLine()){
            System.out.println(scanner.nextLine());
        }
    }
}
