package Graphics;

import Managers.NetworkManager;
import Managers.Self;
import Models.Message;
import Models.User;
import database.ConnectionError;
import database.DatabaseManager;
import database.MessageAccessProblem;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;

/**Chat Interface: send messages, append received ones, interact with the user, Highly smooth*/
public class ChatInterface extends Container {
    JTextArea chatArea;
    JTextField inputArea;
    JFrame frame;
    User dest;
    Message lastMessage;
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

    Action disconnectionButton = new AbstractAction("DECONNEXION") {
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
        this.lastMessage =null;
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
    private void initComponents() {
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

    //thread for received messages
    public class printReceivedMessage extends Thread{
        public printReceivedMessage(){
            this.setDaemon(true);
        }
        public void run(){
            while(true) {
                Message mess = null;
                try {
                    mess = DatabaseManager.getInstance().getLastMessage(dest.getHostname());
                } catch (ConnectionError | SQLException | UnknownHostException connectionError) {
                    connectionError.printStackTrace();
                }
                if (!lastMessage.equals(mess)) {
                    lastMessage =mess;
                    chatArea.append(mess.getMessage());
                }
            }
        }
    }
    //send messages, needs the tcp thread to be opened and to have a function send callable
    private void sendMessage(String message) {
        Self selfInstance = Self.getInstance();
        Message mess= new Message(new User(selfInstance.getHostname(),selfInstance.getPseudo()), dest,message);
        NetworkManager.SendMessageTCP(mess); //calls send: find the conversation's tcp thread or creates it
        chatArea.append("\nME("+ Self.getInstance().getPseudo()+") - "+message);
    }
    private void displayOldMessages() {
        try {
            if(DatabaseManager.getInstance().checkExistConversation(DatabaseManager.getInstance().getDBName())){
                try {
                    ArrayList<Message> conv = DatabaseManager.getInstance().getAllMessages(dest.getHostname());
                    for (Message message: conv){
                        chatArea.append("\n("+message.getSender()+") - "+message.getMessage());
                        lastMessage = message;
                    }
                }catch(MessageAccessProblem e){
                    chatArea.append("(ERROR) We are waiting for your messages. It will be coming shortly.");
                }
            }
            else{
                DatabaseManager.getInstance().addConversation(dest.getHostname());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ConnectionError connectionError) {
            connectionError.printStackTrace();
        }
    }
}
