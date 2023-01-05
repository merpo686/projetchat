package Graphics;

import Managers.NetworkManager;
import Managers.Self;
import Models.Message;
import Models.User;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;

import javax.swing.*;

//server is running full time
// when sending message either the tcp thread exists: it finds it in an array of sockets and uses it to send
// either it doesn't: it connects to the server then sends
// server opens new thread for each new conversation (new dest).
// the reception thread add received messages to the datamanager. to know if there is new messages,
// we start a thread which keep track of last received message and check datamanager if there is new one, to append them

public class ChatInterface extends Container {
    JTextArea chatArea;
    JTextField inputArea;
    JFrame frame;
    User dest;
    Message lastmessage;
    //menu buttons
    Action change_discussion_button = new AbstractAction("CHANGE DISCUSSION") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            //close discussion
            frame.setResizable(true);
            try {
                new ChooseDiscussionInterface(frame);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    };

    Action change_pseudo_button = new AbstractAction("CHANGE PSEUDO") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            frame.setResizable(true);
            new ChoosePseudoInterface(frame);
        }
    };

    Action deconnexion_button = new AbstractAction("DECONNEXION") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            try {
                NetworkManager.Send_Disconnection();
            } catch (SocketException | UnknownHostException e) {
                e.printStackTrace();
            }
            frame.dispose();
        }
    };

    public ChatInterface(JFrame frame, User dest) {
        this.frame=frame;
        this.dest = dest;
        this.lastmessage=null;
        InterfaceManager IM=InterfaceManager.getInstance();
        IM.set_state("ChatInterface");
        IM.set_user(dest);
        initComponents();
        //displayOldmessages();
        new Thread(new printreceivedMessage()).start();
        frame.setContentPane(this);
        frame.setSize(new java.awt.Dimension(510, 470));
    }

    private void initComponents() {
        //declaration of variables
        inputArea = new JTextField();

        JScrollPane jScrollPane1 = new JScrollPane();
        chatArea = new JTextArea();
        JLabel discussion_name = new JLabel();
        setLayout(null); //layout positioning via coordinates
        //inputArea specify
        inputArea.addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                   if (e.getKeyCode()== KeyEvent.VK_ENTER){
                       try {
                           sendMessage(inputArea.getText());
                       } catch (IOException unknownHostException) {
                           unknownHostException.printStackTrace();
                       }
                       inputArea.setText("");
                   }
               }
           }); //send message when enter pressed in input area

        //send button specify
        ImageIcon send_icon = (new ImageIcon(Objects.requireNonNull(ChatInterface.class.getClassLoader().getResource("send.png"))));
        Image image = send_icon.getImage();
        image = image.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        send_icon = new ImageIcon(image);
        JButton send = new JButton();
        send.setContentAreaFilled(true);
        send.setIcon(send_icon);
        send.setOpaque(false);
        send.setContentAreaFilled(false);
        send.setBorderPainted(false);
        send.addActionListener(evt -> {
            try {
                sendMessage(inputArea.getText());
                inputArea.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //chatArea specify
        chatArea.setColumns(20);
        chatArea.setRows(5);
        chatArea.setEditable(false);
        jScrollPane1.setViewportView(chatArea);

        //discussion_name specify
        discussion_name.setFont(new Font("Myriad Pro", Font.PLAIN, 30)); // NOI18N
        discussion_name.setForeground(InterfaceManager.foreground_color);
        discussion_name.setText(dest.get_Pseudo());

        //add
        add(inputArea);
        add(send);
        add(jScrollPane1);
        add(discussion_name);

        //positionning
        send.setBounds(450, 370, 40, 40);
        jScrollPane1.setBounds(10, 80, 490, 280);
        discussion_name.setHorizontalAlignment(SwingConstants.CENTER);
        discussion_name.setBounds(0, 20, 480, 40);
        inputArea.setBounds(10, 370, 410, 40);
        setSize(new Dimension(510, 480));

        //menu bar
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Menu");
        file.add(change_discussion_button);
        file.add(change_pseudo_button);
        file.add(deconnexion_button);
        bar.add(file);
        frame.setJMenuBar(bar);
        frame.setResizable(false);
    }

    //thread for received messages
    public class printreceivedMessage extends Thread{
        public void run(){
            String message;
            while(true) {
                /*Message mess = DatabaseManager.getInstance().getConversation(dest).getlastMessage();
                if (lastmessage!=mess) {
                    lastmessage=mess;
                    chatArea.append(mess.get_message());
                }*/
            }
        }
    }

    //send messages, needs the tcp thread to be opened and to have a function send callable
    private void sendMessage(String message) throws IOException {
        Message mess= new Message(Self.getInstance().get_User(), dest,message);
        NetworkManager.Send_Message_TCP(mess); //calls send: find the conversation's tcp thread or creates it
        chatArea.append("\nME("+ Self.getInstance().get_Pseudo()+") - "+message);
    }
    private void displayOldmessages() {
        //DataManager.getInstance();
        //Conversation conv = DataManager.get_conversation(User u);
        /*for (Message message: conv.getListMessages){
            chatArea.append("\n("+message.sender+") - "+message.data);
            lastmessage= message;
        }*/
    }

}
