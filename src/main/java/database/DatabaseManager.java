package database;
import Models.Message;
import Models.User;

import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private Connection co;
    static DatabaseManager instance;

    private DatabaseManager() throws ConnectionError {
        this.connectDB();
    }

    public static DatabaseManager getInstance() throws ConnectionError {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void connectDB() throws ConnectionError {
        String databasename = "messages.sqlite";
        String url = "jdbc:sqlite:" + databasename;
        //the driver automatically creates a new database when the database does not already exist
        try {
            co = DriverManager.getConnection(url);
            DatabaseMetaData meta = co.getMetaData();
            ResultSet rs = meta.getCatalogs();
            while(rs.next()){
                String catalog = rs.getString(1); //retrieves the second column of the result set corresponding to the catalogs which stores the names of all the databases
                if(!catalog.equals(databasename)){ //check if the database exists already
                    System.out.println("Creating a new database...");
                }
            }
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A connection has been established for " + meta.getUserName());
        } catch (SQLException throwables1) {
            throwables1.printStackTrace();
            throw new ConnectionError(url);
        } /*finally {
            try {
                if(co != null){
                    co.close();
                }
            }
            catch(SQLException throwables2) {
                throwables2.printStackTrace();
                throw new ConnectionError(url);
            }*/
        //}
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
        String stringMessage = message.getMessage();
        Date date = (Date) message.getDate();
        String senderID = message.getSender().getHostname();
        String senderPseudo = message.getSender().getPseudo();
        String receiverID = message.getReceiver().getHostname();
        String receiverPseudo = message.getSender().getPseudo();
        Statement statement = co.createStatement();
        //verifier si la conv existe sinon la creer
        String sql = "INSERT INTO hostnameConv \n" +
                "VALUES(Id, date, senderID, senderPseudo, receiverID, receiverPseudo, stringMessage);";
        statement.executeUpdate(sql);
    }

    public Message getLastMessage(String hostnameConv) throws SQLException, UnknownHostException {
        Statement statement = co.createStatement();
        String sql = "SELECT LAST(Message) FROM hostnameConv;";
        ResultSet rs = statement.executeQuery(sql);
        Message mess = new Message(new User(rs.getString(2),rs.getString(3)),
                new User(rs.getString(4), rs.getString(5)), rs.getString(6));
        mess.setDate(rs.getDate(2));
        return mess;
    }

    public ArrayList<Message> getAllMessages(String hostnameConv) throws SQLException, UnknownHostException {
        ArrayList<Message> listMessages = null;
        Statement statement = co.createStatement();
        String sql = "SELECT * FROM hostnameConv;";
        ResultSet rs = statement.executeQuery(sql);
        /*String length = "COUNT * FROM hostnameConv";
        ResultSet rsLength = statement.executeQuery(sql);*/
        while(rs.next()) {
            Message mess = new Message(new User(rs.getString(2),rs.getString(3)),
                    new User(rs.getString(4), rs.getString(5)), rs.getString(6));
            mess.setDate(rs.getDate(2));
            listMessages.add(mess);
        }
        if(listMessages == null){
            System.out.println("You have no existing messages with the designated user"); //il faudrait que ca remonte au user qu'il y ai aucun message
        }
        return listMessages;
    }

    public Boolean checkExistConversation() throws SQLException {
        Statement statement = co.createStatement();
        String sql = "SELECT \n" +
                "  object_id \n" +
                "FROM sys.tables\n" +
                "WHERE name = 'hostnameConv';";
        ResultSet rs = statement.executeQuery(sql);
        //checks if the table is null
        if (rs.getString(0).equals("NULL")){
            return false;
        }
        else {
            return true;
        }
    }
}
