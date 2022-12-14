package Managers;

import Models.*;

import java.io.*;
import java.net.Socket;

public class TCPClientHandler extends Thread {
    private Socket numPort;
    private User userDest;
    public TCPClientHandler(Socket link){
        this.numPort = link;
    }

    public void run(){

        while(true){

            try {
                //on recupere les flux de lecture et d'envoie du socket
                DataInputStream inputStream = new DataInputStream(numPort.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(numPort.getOutputStream());

                outputStream.writeUTF("Please enter the name of the user whom you want to talk to");
                //getting user
                String pseudoDest = inputStream.readUTF();
                userDest = new User(pseudoDest, numPort.getPort());

                outputStream.writeUTF("Here is the conversation history");
                outputStream.writeUTF(Conversation.getInstance(userDest).getHistory());

                outputStream.writeUTF("Please enter a message to send, or enter 'exit' if you wish to disconnect");
                //getting message
                String receivedMsg = inputStream.readUTF();
                if(receivedMsg.equals("Exit")){
                    System.out.println("Closing... ");
                    numPort.close();
                    System.out.println("Closed.");
                    break;
                }

                //storing message sent in list of messages
                Conversation.getInstance(userDest).addMessage(new Message(userDest, receivedMsg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
