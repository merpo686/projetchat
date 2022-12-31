package Managers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    //constructor
    public TCPServer(int port) throws IOException {
        //creation du server socket
        ServerSocket socket = new ServerSocket(port);
        //le server tourne a l'infinie s'il n'y a pas d'erreur/exception
        while(true) {
            Socket link = null;
            try{
                link = socket.accept();
                System.out.println("A new connection identified at : " + link);

                new Thread(new TCPClientHandler(link)).start();
            }
            catch(Exception e){
                assert link != null;
                link.close();
                e.printStackTrace();
            }

        }
    }
}
