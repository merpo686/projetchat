package database;
import Models.Message;
import Models.User;
import org.sqlite.SQLiteConfig;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.nio.file.*;

public class DatabaseManager {
    private Connection co;
    private final String databaseName = "messages.sqlite";
    static DatabaseManager instance;

    private DatabaseManager() throws ConnectionError {
        this.connectDB(databaseName);
    }

    /**the get instance() for the database manager, as we want to be able to access it at any time from any function*/
    public static DatabaseManager getInstance() throws ConnectionError {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**connects to the database*/
    public void connectDB(String databaseName) throws ConnectionError {
        String url = "jdbc:sqlite:" + databaseName;
        //the driver automatically creates a new database when the database does not already exist
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            co = DriverManager.getConnection(url, config.toProperties());
            DatabaseMetaData meta = co.getMetaData();
            ResultSet rs = meta.getCatalogs();
            while(rs.next()){
                String catalog = rs.getString(1); //retrieves the first column of the result set corresponding to the catalogs which stores the names of all the databases
                if(!catalog.equals(databaseName)){ //check if the database exists already
                    System.out.println("Creating a new database...");
                }
            }
            System.out.println("A connection has been established");
            System.out.println("The driver name is " + meta.getDriverName());
        } catch (SQLException throwables1) {
            throwables1.printStackTrace();
            throw new ConnectionError(url);
        }
    }

    /**creates a table for the conversations to be stored*/
    public void createTableConversations() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Conversations (\n" +
                "Hostname STRING PRIMARY KEY NOT NULL);";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.executeUpdate();
        ps.clearParameters();
    }

    /**creates a table for the messages to be stored in a specific conversation identified by hostname, which corresponds to the hostname of the receiver*/
    public void createTableMessages(String hostname) throws SQLException {
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        if(!checkExistConversation(hostname)){
            addConversation(hostname);
        }
        String sql = "CREATE TABLE IF NOT EXISTS Messages (\n" +
                "IdMessage INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "Date STRING NOT NULL, \n" +
                "SenderID STRING NOT NULL, \n" +
                "SenderPseudo STRING NOT NULL, \n" +
                "ReceiverID STRING NOT NULL, \n" +
                "ReceiverPseudo STRING NOT NULL, \n" +
                "StringMessage STRING NOT NULL, \n" +
                "FOREIGN KEY(ReceiverID) REFERENCES Conversations(Hostname));";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**adds a conversation identified by the hostname of the receiver in the conversations table*/
    public void addConversation(String hostname) throws SQLException {
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        PreparedStatement ps = co.prepareStatement("INSERT INTO Conversations (Hostname) VALUES(?)");
        ps.setString(1, hostname);
        ps.executeUpdate();
        ps.clearParameters();
    }

    /**adds a message in the messages table*/
    public void addMessage(Message message) throws SQLException { //A VOIR SI ON PEUT PAS REDUIR LE NB DE PARAMETRES A 1 ET ENLEVER LE HOSTNAME CAR IL EST NORMALEMENT EGAL AU MESSAGE RECEIVER
        String stringMessage = message.getMessage();
        LocalDateTime date = message.getDate();
        String senderID = message.getSender().getHostname();
        String senderPseudo = message.getSender().getPseudo();
        String hostname = message.getReceiver().getHostname(); //hostname == receiverID
        String receiverPseudo = message.getReceiver().getPseudo();
        //check if the tables and corresponding conversation exist
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        if(!checkExistConversation(hostname)){
            addConversation(hostname);
        }
        if(!checkExistTableMessages()){
            createTableMessages(hostname);
        }

        PreparedStatement ps = co.prepareStatement("INSERT INTO Messages (IdMessage, Date, SenderID, SenderPseudo, ReceiverID, ReceiverPseudo, StringMessage) VALUES(?, ?, ?, ?, ?, ?, ?);");
        ps.setString(2, String.valueOf(date));
        ps.setString(3, senderID);
        ps.setString(4, senderPseudo);
        ps.setString(5, hostname);
        ps.setString(6, receiverPseudo);
        ps.setString(7, stringMessage);
        ps.executeUpdate();
        ps.clearParameters();
    }

    /**
     * gets the last message sent by the sender in a specific conversation identified by hostname, which corresponds to the hostname of the receiver
     * @param hostname
     * */
    public Message getLastMessage(String hostname) throws SQLException, UnknownHostException {
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        if(!checkExistConversation(hostname)){
            addConversation(hostname);
        }
        if(!checkExistTableMessages()){
            createTableMessages(hostname);
        }
        String sql = "SELECT * FROM Messages WHERE (ReceiverID = ?) ORDER BY ReceiverID DESC LIMIT 1;";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.setString(1, hostname);
        ResultSet rs = ps.executeQuery();
        ps.clearParameters();
        if(rs.next()){
            Message mess = new Message(new User(rs.getString(3),rs.getString(4)),
                    new User(rs.getString(5), rs.getString(6)), rs.getString(7));
            String stringDate = rs.getString(2);
            mess.setDate(LocalDateTime.parse(stringDate));
            return mess;
        }
        return null;
    }

    /**gets all messages sent by the sender in a specific conversation identified by hostname, which corresponds to the hostname of the receiver*/
    public ArrayList<Message> getAllMessages(String hostname) throws  MessageAccessProblem {
        try{
            if(!checkExistTableConversations()){
                createTableConversations();
            }
            if(!checkExistConversation(hostname)){
                addConversation(hostname);
            }
            if(!checkExistTableMessages()){
                createTableMessages(hostname);
            }
            ArrayList<Message> listMessages = new ArrayList<>();
            String sql = "SELECT * FROM Messages WHERE (ReceiverID = ?);";
            PreparedStatement ps = co.prepareStatement(sql);
            ps.setString(1, hostname);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                User sender = new User(rs.getString(3),rs.getString(4));
                User receiver = new User(rs.getString(5), rs.getString(6));
                Message mess = new Message(sender, receiver, rs.getString(7));
                String stringDate = rs.getString(2);
                mess.setDate(LocalDateTime.parse(stringDate));
                listMessages.add(mess);
            }
            return listMessages;
        } catch(Exception e){
            e.printStackTrace();
            throw new MessageAccessProblem(hostname);
        }
    }

    /**checks if the entire conversations table exists*/
    public Boolean checkExistTableConversations() throws SQLException {
        int count=0;
        Statement statement = co.createStatement();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='Conversations'";
        ResultSet rs = statement.executeQuery(sql);
        while(rs.next()){
            count++;
        }
        if(count==1){
            return true;
        }else{
            return false;
        }
    }

    /**checks if a specific conversation exists inside the conversations table*/
    public Boolean checkExistConversation(String hostname) throws SQLException {
        int count=0;
        String sql = "SELECT EXISTS(SELECT Hostname FROM Conversations WHERE Conversations.Hostname = ?);";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.setString(1, hostname);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            count++;
        }
        if(count==1){
            return true;
        }else{
            return false;
        }
    }

    /**checks if the entire messages table exists*/
    public Boolean checkExistTableMessages() throws SQLException {
        int count=0;
        Statement statement = co.createStatement();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='Messages'";
        ResultSet rs = statement.executeQuery(sql);

        while(rs.next()){
            count++;
        }
        if(count==1){
            return true;
        }else{
            return false;
        }
    }

    /**clears the database specified*/
    public void clearDB(String databaseName) {
        Path path = Path.of(databaseName);
        System.out.println(path.toAbsolutePath());
        try {
            Files.delete(path);
        } catch(NoSuchFileException e) {
            System.out.println("Delete: No such file directory " + path);
        } catch(DirectoryNotEmptyException e){
            System.out.println("Directory is not empty" + path);
        } catch(IOException e) {
            System.out.println("You don't have permission" + path);
        }
        System.out.println("File deleted successfully: " + path);
    }
}
