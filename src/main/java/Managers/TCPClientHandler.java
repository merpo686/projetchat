package Managers;

import Models.*;

import java.io.*;
import java.net.Socket;

public class TCPClientHandler extends Thread {
    private final Socket numPort;
    private final User dest;


    public TCPClientHandler(Socket link,User dest){
        this.setDaemon(true);
        this.numPort = link;
        this.dest=dest;
    }

    public Socket getNumPort() {
        return numPort;
    }

    public void run(){
        while(true){
            try {
                //we recover the input stream
                DataInputStream inputStream = new DataInputStream(numPort.getInputStream());
                //getting message
                String received = inputStream.readUTF();
                //end of thread
                if(received.equals("Exit")){
                    System.out.println("Closing... ");
                    numPort.close();
                    System.out.println("Closed.");
                    break;
                }
                //message
                else{
                    Message mess = new Message(dest,Self.getInstance().get_User(), received);
                    //DatabaseManager.getInstance.addMessage(mess,dest.get_hostname);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
