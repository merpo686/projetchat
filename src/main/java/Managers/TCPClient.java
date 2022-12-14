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

        //the client and the client handler exchange data
        while(true){
            System.out.println(inputStream.readUTF());
            String toSend = new Scanner(System.in).nextLine();
        }


    }
}
