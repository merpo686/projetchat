package Managers;

import Models.*;
import database.ConnectionError;
import database.DatabaseManager;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class TCPClientHandler extends Thread {
    private final Socket numPort;
    private final User dest;
    DataInputStream inputStream;

    public TCPClientHandler(Socket link,User dest){
        this.setDaemon(true);
        this.numPort = link;
        this.dest=dest;
    }

    public Socket getNumPort() {
        return numPort;
    }

    @Override
    public void interrupt() {
        super.interrupt();
        try {
            inputStream.close(); // Fermeture du flux si l'interruption n'a pas fonctionné.
        } catch (IOException e) {}
    }

    public void run(){
        while(true){
            try {
                //we recover the input stream
                inputStream = new DataInputStream(numPort.getInputStream());
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
                    Message mess = new Message(dest,new User(Self.getInstance().getHostname(),Self.getInstance().get_Pseudo()), received);
                    DatabaseManager.getInstance().addMessage(mess,dest.get_Hostname());
                }
            } catch (InterruptedIOException e) { // Si l'interruption a été gérée correctement.
                Thread.currentThread().interrupt();
                System.out.println("Interrompu via InterruptedIOException");
            } catch (IOException e) {
                if (!isInterrupted()) { // Si l'exception ne vient pas d'une interruption.
                    e.printStackTrace();
                } else { // <italique>Thread</italique> interrompu mais <italique>InterruptedIOException</italique> n'était pas gérée pour ce type de flux.
                    System.out.println("Interrompu");
                }
            } catch (SQLException | ConnectionError throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
