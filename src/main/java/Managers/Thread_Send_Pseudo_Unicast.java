package Managers;
import java.io.IOException;
import java.net.*;

public class Thread_Send_Pseudo_Unicast implements Runnable {
    private final DatagramSocket socket;
    int numSocket;
    private final String hostname;

    public Thread_Send_Pseudo_Unicast(String hostname) throws SocketException {
        this.socket= new DatagramSocket();
        this.numSocket=Self.portUDP;
        this.hostname=hostname;
    }

    public void run() {
        String data;
        try {
            data = Self.getInstance().get_Pseudo();
            byte[] pseudoData = data.getBytes();
            DatagramPacket sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName(String.valueOf(hostname)), numSocket);
            socket.send(sendNotif);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
