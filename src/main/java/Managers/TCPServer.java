package Managers;

import Models.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    //constructor
    public TCPServer() throws IOException {
        //creation du server socket
        ServerSocket socket = new ServerSocket(0);
        Self.getInstance().set_Port(socket.getLocalPort());
        //le server tourne a l'infinie s'il n'y a pas d'erreur/exception
        while(true) {
            Socket link = null;
            try{
                link = socket.accept();
                System.out.println("A new connection identified at : " + link);
                User dest = ActiveUserManager.getInstance().get_User(link.getInetAddress().getHostName());
                TCPClientHandler thread = new TCPClientHandler(link,dest);
                thread.start();
                ThreadManager.getInstance().add_active_conversation(dest,thread);
            }
            catch(Exception e){
                assert link != null;
                link.close();
                e.printStackTrace();
            }

        }
    }
}
