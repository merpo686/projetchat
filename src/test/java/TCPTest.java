import ActivityManagers.Self;
import Graphics.Interface;
import Models.Message;
import Models.Observers;
import Models.User;
import Threads.TCPClientHandler;
import Threads.TCPServer;
import Threads.ThreadManager;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class TCPTest {
    @Test
    public void TCPConnectionTest() {
        Self self = Self.getInstance();
        ThreadManager.getInstance();
        User user =new User(self.getHostname(),self.getPseudo());
        try{
            Socket socket = new Socket(self.getHostname(),12342);
            TCPClientHandler tcpClientHandlerTest = new TCPClientHandler(socket, user);
            tcpClientHandlerTest.start();
        } catch (IOException e) {
            System.out.println("Unable to create TCP socket. Hostname: "+ self.getHostname()+" Port TCP: "+Self.portTCP);
            e.printStackTrace();
        }
        assert (ThreadManager.getInstance().getActiveconversation(user))!=null;
    }
}
