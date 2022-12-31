package Graphics;

import Managers.NetworkManager;
import Managers.Self;
import Models.User;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;

import javax.swing.*;

//need a function to get old text and display them ok
//need a function to display received texts ok
//need to be linked to database and networkmanager ok
//update sendmessage to send messages ok

public class ChatInterface extends Container {
    JTextArea chatArea;
    JTextField inputArea;
    JFrame frame;
    User user;

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

    public ChatInterface(JFrame frame, User user) {
        this.frame=frame;
        this.user = user;
        InterfaceManager IM=InterfaceManager.getInstance();
        IM.set_state("ChatInterface");
        IM.set_user(user);
        initComponents();
        //open threads and pass function printreceivedMessage as a handler of received messages to print them
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
                       } catch (UnknownHostException unknownHostException) {
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
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    sendMessage(inputArea.getText());
                    inputArea.setText("");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
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
        discussion_name.setText(user.get_Pseudo());

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

    //handler for received messages
    public void printreceivedMessage(String message) {
        chatArea.append(message);
    }

    //send messages, needs the tcp thread to be opened and to have a function send callable
    private void sendMessage(String message) throws UnknownHostException {
        /*try NetworkManager.Send_Message_TCP(message,user); //calls the right send
        catch host disconnected / envoi impossible
        ->print error message hote injoignable
         */
        chatArea.append("\nME("+ Self.getInstance().get_Pseudo()+") - "+message);
    }
    private void displayOldmessages() {
        //DataManager.getInstance();
        //Conversation conv = DataManager.get_conversation(User u);
        /*for (Message message: conv.messages){
            chatArea.append("\n("+message.sender+") - "+message.data);
        }*/
    }

}
