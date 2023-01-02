package Managers;

import Models.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient {
    private Socket socket;
    public TCPClient(int numPort) throws IOException {
        //we establish the connection to our IP address and the specified port
        this.socket = new Socket(InetAddress.getLocalHost(), numPort);

        //we recover the input and output streams
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        try (Scanner newScanner = new Scanner(System.in)) {
            //first the user enters his pseudo, and then enters into the while loop to exchange messages
            System.out.println(inputStream.readUTF());
            String sendPseudo = newScanner.nextLine();
            outputStream.writeUTF(sendPseudo);
            if(sendPseudo.equals("Exit")){
                System.out.println("Closing...");
                socket.close();
                System.out.println("Closed");
                System.exit(0);
            }
            int i=1;
            //the client exchanges messages with the client handler
            while(true){
                String receivedData = inputStream.readUTF();
                System.out.println(receivedData);
                if(!((i%2)==0)){ //we implement a modulo method to not have to send anything mandatorily when the server prints an overview of the message sent
                    String toSend = newScanner.nextLine();
                    outputStream.writeUTF(toSend);
                    if(toSend.equals("Exit")){
                        System.out.println("Closing...");
                        socket.close();
                        System.out.println("Closed");
                        break;
                    }
                }
                i++;
            }
        }
    }
}
