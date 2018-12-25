import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client{
    private Socket socket;

    Client(String ipAddress, int port){
        while(true){
            try {
                this.socket = new Socket(ipAddress, port);
                break;
            } catch (IOException e) {
            }
        }
        System.out.println("Connected to: " + ipAddress + ":" + port);
    }

    // sends a command to a minion
    public void send(String message){
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Receives input from a minion
    public void receive(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String message = "";
            message = reader.readLine();
            // changes the message from | to \n
            message = message.replace("|", "\n");
            System.out.println(message);
        } catch (IOException|NullPointerException e) {
//            e.printStackTrace();
        }

    }

}

public class Gru {

    // menu to be printed for the user
    public static void menu(){
        String[] options = new String[]{
                "linux * - will run a linux command",
                "connect IP PORT - will connect to another minion",
                "die - kills server",
                "kill - will kill another minion"
        };

        for(String option : options){
            System.out.println(option);
        }
    }

    public static void main(String[] args) {
        // gets the ip address and port entered into the command prompt
        String ipAddress = args[0];
        int port = Integer.parseInt(args[1]);
        Client client = new Client(ipAddress, port);
        // gets user input
        Scanner scanner = new Scanner(System.in);
        boolean keepAlive = true;

        while(keepAlive){
            String input = scanner.nextLine();
            switch (input){
                case "help":
                    menu();
                    break;
                case "quit":
                    System.out.println("Closed connection");
                    keepAlive = false;
                    break;
                case "die":
                    System.out.println("Closed connection");
                    client.send(input);
                    keepAlive = false;
                    break;
                default:
                    client.send(input);
                    client.receive();
            }
        }
    }
}
