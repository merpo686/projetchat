package Managers;

import java.net.DatagramSocket;

public class ThreadRcvBC implements Runnable {
    Socket socket;
    DatagramPacket packet;

    public void ThreadRdvBC() {
        this.packet = new DatagramSocket();
        this.socket = null;
    }

    public class Server implements Runnable{
        public void run() {
            while (true) {
                //on receive avec un port qqonque donc j'ai pas mis de port dans le parametre receive
                packet.receive(socket);
                new Thread((Runnable) new Ack(socket, packet)).start();
            }
        }
    }

    public class Ack(){

        public Ack(Socket socket, DatagramPacket packet){
            this.socket = socket;
            this.packet = packet;
        }

        public void run(){

        }
    }
}
