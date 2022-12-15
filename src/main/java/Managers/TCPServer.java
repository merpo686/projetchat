package Managers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private ServerSocket socket;
    //constructor
    public TCPServer(int port) throws IOException {
        //creation du server socket
        this.socket = new ServerSocket(port);
        //le server tourne a l'infinie s'il n'y a pas d'erreur/exception
        while(true) {
            Socket link = null;
            try{
                link = socket.accept();
                System.out.println("A new connection identified at : " + link);

                new Thread(new TCPClientHandler(link)).start();
            }
            catch(Exception e){
                link.close();
                e.printStackTrace();
            }

        }
    }
}
