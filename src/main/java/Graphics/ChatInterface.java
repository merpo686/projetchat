package Graphics;

import Managers.NetworkManager;
import Managers.Self;
import Models.Message;
import Models.User;
import database.ConnectionError;
import database.DatabaseManager;
import database.MessageAccessProblem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;

/**Chat Interface: send messages, append received ones, interact with the user, Highly smooth*/
public class ChatInterface extends Container {
    private static final Logger LOGGER = LogManager.getLogger(ChatInterface.class);
    JTextArea chatArea;
    JTextField inputArea;
    JFrame frame;
    User dest;
    Message lastMessage;
    DatabaseManager db;
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
            NetworkManager.SendDisconnection();
            frame.dispose();
        }
    };
    /**Constructor, same as ChooseDiscussionInterface, the core of the interface is specified in initComponents*/
    public ChatInterface(JFrame frame, User dest){
        this.frame=frame;
        this.dest = dest;
        LOGGER.info("starting conv with user:"+dest);
        this.lastMessage =null;
        try {
            this.db = DatabaseManager.getInstance();
        } catch (ConnectionError connectionError) {
            LOGGER.error("Error connecting to the database.");
            connectionError.printStackTrace();
        }
        InterfaceManager IM=InterfaceManager.getInstance();
        IM.setState("ChatInterface");
        IM.setUser(dest);
        initComponents(); //specify interface components
        displayOldMessages(); //crystal clear
        new Thread(new printReceivedMessage()).start(); //start a thread which will check constantly if there is new messages to print
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

    /**Thread which appends new received messsages constantly */
    public class printReceivedMessage extends Thread{
        public printReceivedMessage(){
            this.setDaemon(true);
        }
        public void run(){
            while(true) {
                Message mess = null;
                try {
                    mess = db.getLastMessage(dest.getHostname());
                } catch ( SQLException | UnknownHostException connectionError) {
                    connectionError.printStackTrace();
                }
                if (mess!=null && !lastMessage.equals(mess)) {
                    lastMessage =mess;
                    chatArea.append(mess.getMessage());
                }
            }
        }
    }
    /**Send messages to the destination user */
    private void sendMessage(String message) {
        Self selfInstance = Self.getInstance();
        Message mess= new Message(new User(selfInstance.getHostname(),selfInstance.getPseudo()), dest,message);
        NetworkManager.SendMessageTCP(mess); //calls send: find the conversation's tcp thread or creates it
        try {
            db.addMessage(mess);
        } catch (SQLException | ConnectionError throwable) {
            LOGGER.error("Error inserting the message in the Database.");
            throwable.printStackTrace();
        }
        chatArea.append("\nME("+ Self.getInstance().getPseudo()+") - "+message);
    }
    /**Display old messages at the opening of the chat interface*/
    private void displayOldMessages() {
        try {
            ArrayList<Message> conv = db.getAllMessages(dest.getHostname());
            for (Message message: conv){
                chatArea.append("\n("+message.getSender()+") - "+message.getMessage());
                lastMessage = message;
            }
        }catch(MessageAccessProblem e){
            LOGGER.error("Error loading existing messages from the Database. Or no messages for this conversation.");
        }
    }
}
