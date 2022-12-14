package Managers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    static Boolean active=true;
    private ServerSocket socket;
    //constructor
    public TCPServer(int port) throws IOException {
        //creation du server socket
        this.socket = new ServerSocket(port);
        Socket link = socket.accept();
        System.out.println("A new connection identified at : " + link);

        new Thread(new TCPClientHandler(link)).start();
    }
}
