package Graphics;

import Models.ObserverReception;
import ActivityManagers.*;
import Models.Message;
import Models.ObserverDisconnection;
import Models.User;
import Conversations.ConversationsManager;
import Conversations.MessageAccessProblem;
import Threads.TCPClientHandler;
import Threads.ThreadManager;
import Threads.UDPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;

/**Chat Interface: send messages, append received ones, interact with the user, Highly smooth*/
public class ChatInterface extends Container implements ObserverReception, ObserverDisconnection {
    private static final Logger LOGGER = LogManager.getLogger(ChatInterface.class);
    JTextArea chatArea;
    JTextField inputArea;
    JFrame frame;
    User dest;
    ConversationsManager db;
    TCPClientHandler tcpClientHandler;
    UDPServer udpServer;

    private final ArrayList<ObserverReception> observers = new ArrayList<>();
    public void attach(ObserverReception observer){
        this.observers.add(observer);
    }
    public void notifyObserver(Message mess){
        for (ObserverReception observer: observers){
            observer.update(mess);
        }
    }

    //menu buttons
    Action changeDiscussionButton = new AbstractAction("CHANGE DISCUSSION") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            //close discussion
            frame.setResizable(true);
            new ChooseDiscussionInterface(frame);
        }
    };

    Action changePseudoButton = new AbstractAction("CHANGE PSEUDO") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            frame.setResizable(true);
            new ChoosePseudoInterface(frame);
        }
    };

    Action disconnectionButton = new AbstractAction("DISCONNECTION") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            ThreadManager.SendDisconnection();
            frame.dispose();
        }
    };
    /**Constructor, same as ChooseDiscussionInterface, the core of the interface is specified in initComponents
     * @param dest User to discuss with
     * @param frame - frame in activity
     * */
    public ChatInterface(JFrame frame, User dest){
        this.frame=frame;
        this.dest = dest;
        this.attach(ConversationsManager.getInstance());
        this.attach(ThreadManager.getInstance());
        LOGGER.info("starting conversation with user:"+dest);
        this.udpServer = ThreadManager.getInstance().getUdpServer();
        this.udpServer.attach(this);
        //here we make sure the TCPClientHandler thread associated to our conversation exists, then we observe it
        this.tcpClientHandler  = ThreadManager.getInstance().getActiveconversation(dest);
        if (tcpClientHandler==null){
            Socket socket= null;
            try {
                socket = new Socket(dest.getHostname(), Self.portTCP);
            } catch (IOException e) {
                LOGGER.error("Unable to create TCP socket. Hostname: "+ dest.getHostname()+" Port TCP: "+Self.portTCP);
                e.printStackTrace();
            }
            tcpClientHandler = new TCPClientHandler(socket,dest);
            tcpClientHandler.start();
            ThreadManager.getInstance().addActiveconversation(dest,tcpClientHandler);
        }
        this.tcpClientHandler.attach(this);
        this.db = ConversationsManager.getInstance();
        InterfaceManager IM=InterfaceManager.getInstance();
        IM.setState("ChatInterface");
        IM.setUser(dest);
        initComponents(); //specify interface components
        displayOldMessages(); //crystal clear
        frame.setContentPane(this);
        frame.setSize(new java.awt.Dimension(510, 470));
    }
/** Initialize the components of the interface */
    private void initComponents(){
        //declaration of variables
        inputArea = new JTextField();
        JScrollPane jScrollPane1 = new JScrollPane();
        chatArea = new JTextArea();
        JLabel discussionName = new JLabel();
        setLayout(null); //layout positioning via coordinates
        //inputArea specify
        inputArea.addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                   if (e.getKeyCode()== KeyEvent.VK_ENTER){
                       sendMessage(inputArea.getText());
                       inputArea.setText("");
                   }
               }
           }); //send message when enter pressed in input area

        //send button specify
        ImageIcon sendIcon = (new ImageIcon(Objects.requireNonNull(ChatInterface.class.getClassLoader().getResource("send.png"))));
        Image image = sendIcon.getImage();
        image = image.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        sendIcon = new ImageIcon(image);
        JButton send = new JButton();
        send.setContentAreaFilled(true);
        send.setIcon(sendIcon);
        send.setOpaque(false);
        send.setContentAreaFilled(false);
        send.setBorderPainted(false);
        send.addActionListener(evt -> {
            sendMessage(inputArea.getText());
            inputArea.setText("");
        });

        //chatArea specify
        chatArea.setColumns(20);
        chatArea.setRows(5);
        chatArea.setEditable(false);
        jScrollPane1.setViewportView(chatArea);

        //discussionName specify
        discussionName.setFont(new Font("Myriad Pro", Font.PLAIN, 30)); // NOI18N
        discussionName.setForeground(InterfaceManager.foregroundColor);
        discussionName.setText(dest.getPseudo());

        //add
        add(inputArea);
        add(send);
        add(jScrollPane1);
        add(discussionName);

        //positionning
        send.setBounds(450, 370, 40, 40);
        jScrollPane1.setBounds(10, 80, 490, 280);
        discussionName.setHorizontalAlignment(SwingConstants.CENTER);
        discussionName.setBounds(0, 20, 480, 40);
        inputArea.setBounds(10, 370, 410, 40);
        setSize(new Dimension(510, 480));

        //menu bar
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Menu");
        file.add(changeDiscussionButton);
        file.add(changePseudoButton);
        file.add(disconnectionButton);
        bar.add(file);
        frame.setJMenuBar(bar);
        frame.setResizable(false);
    }

    /**Update method which appends new received messsages when TCPClient says he received some
     * @param mess Message the tcpclienthanlder received
     * */
    @Override
    public void update(Message mess){
        chatArea.append("\n("+ mess.getSender().getPseudo()+") - "+mess.getMessage());
    }

    /** Shows a disconnected interface if the user we were chatting with disconnected
     * */
    @Override
    public void update(){
        //if we are chatting with the user right now it shows a user-disconnected interface
        JOptionPane.showMessageDialog(frame, "The user you were chatting with just disconnected. " +
                        "As messages won't get through, you will get back to the choose discussion interface in 3 secs.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setVisible(false);
        //close discussion
        frame.setResizable(true);
        new ChooseDiscussionInterface(frame);
    }

    /**Send messages to the destination user
     * @param message to send
     * */
    private void sendMessage(String message) {
        Self selfInstance = Self.getInstance();
        Message mess= new Message(new User(selfInstance.getHostname(),selfInstance.getPseudo()), dest,message);
        chatArea.append("\nME("+ Self.getInstance().getPseudo()+") - "+message);
        notifyObserver(mess);
    }
    /**Display old messages at the opening of the chat interface*/
    private void displayOldMessages() {
        try {
            ArrayList<Message> conv = db.getAllMessages(dest.getHostname());
            for (Message message: conv){
                if ((message.getSender().getHostname()).equals(Self.getInstance().getHostname()))
                {
                    chatArea.append("\nME("+ Self.getInstance().getPseudo()+") - "+message);
                }
                else {
                    chatArea.append("\n("+message.getSender()+") - "+message.getMessage());
                }
            }
        }catch(MessageAccessProblem e){
            LOGGER.error("Error loading existing messages from the Database. Or no messages for this conversation.");
        }
    }
}
