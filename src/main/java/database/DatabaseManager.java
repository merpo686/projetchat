package database;
import Models.Conversation;
import Models.Message;
import Models.User;
import org.sqlite.*;

import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private final String DataBaseName;
    private Connection co;

    public DatabaseManager(String DataBaseName) {
        this.DataBaseName = DataBaseName;
    }

    public void connectDB() throws ConnectionError {
        String url = "jdbc:sqlite:C:/sqlite/db/" + DataBaseName;
        //the driver automatically creates a new database when the database does not already exist
        try {
            co = DriverManager.getConnection(url);
            DatabaseMetaData meta = co.getMetaData();
            ResultSet rs = meta.getCatalogs();
            while(rs.next()){
                String catalog = rs.getString(1); //retrieves the second column of the result set corresponding to the catalogs which stores the names of all the databases
                if(!catalog.equals(DataBaseName)){ //check if the database exists already
                    System.out.println("Creating a new database...");
                }
            }
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A connection has been established for " + meta.getUserName());
        } catch (SQLException throwables1) {
            throwables1.printStackTrace();
            throw new ConnectionError(url);
        } finally {
            try {
                if(co != null){
                    co.close();
                }
            }
            catch(SQLException throwables2) {
                throwables2.printStackTrace();
                throw new ConnectionError(url);
            }
        }
    }

    public void createTableConversation() throws SQLException {

    }

    public void addConversation(String hostnameConv) throws SQLException {
        Statement statement = co.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS hostnameConv (\n" +
                "Id AS Integer NOT NULL AUTO INCREMENT\n" +
                "Date AS Date NOT NULL \n" +
                "Sender AS String NOT NULL \n" +
                "Receiver AS String NOT NULL \n" +
                "StringMessage AS String NOT NULL \n" +
                "PRIMARY KEY(Id))                   ;"; //a tester la requete hein

        statement.executeUpdate(sql);
    }

    public void addMessage(Message message, String hostnameConv) throws SQLException {
        String stringMessage = message.get_message();
        Date date = (Date) message.get_date();
        String sender = message.get_sender().get_Hostname();
        String receiver = message.get_receiver().get_Hostname();
        Statement statement = co.createStatement();
        String sql = "INSERT INTO hostnameConv \n" +
                "VALUES(Id, date, sender, receiver, stringMessage);";
        statement.executeUpdate(sql);
    }

    public String getLastMessage(String hostnameConv) throws SQLException {
        Statement statement = co.createStatement();
        String sql = "SELECT LAST(Message) FROM hostnameConv";
        ResultSet rs = statement.executeQuery(sql);
        String lastMessage = rs.getString(4); //we retreive the 4th column of the corresponding row, which represents the last message
        if(lastMessage == null){
            System.out.println("You have no existing messages with the designated user"); //il faudrait que ca remonte au user qu'il y ai aucun message
        }
        return lastMessage;
    }

    public ArrayList<Message> getAllMessages(String hostnameConv) throws SQLException, UnknownHostException {
        ArrayList<Message> listMessages = null;
        Statement statement = co.createStatement();
        String sql = "SELECT * FROM hostnameConv";
        ResultSet rs = statement.executeQuery(sql);
        /*String length = "COUNT * FROM hostnameConv";
        ResultSet rsLength = statement.executeQuery(sql);*/
        while(rs.next()) {
            Message mess = new Message(new User(rs.getString(0)), new User(rs.getString(1)), rs.getString(3));
            mess.set_date(rs.getDate(2));
            listMessages.add(mess);
        }
        if(listMessages == null){
            System.out.println("You have no existing messages with the designated user"); //il faudrait que ca remonte au user qu'il y ai aucun message
        }
        return listMessages;
    }
}
