package Managers;
import Models.User;

import java.net.UnknownHostException;
import java.util.ArrayList;


public class ActiveUserManager {
    static ActiveUserManager instance;

    ArrayList<User> listActiveUsers;
    private ActiveUserManager() {
        listActiveUsers = new ArrayList<User>();
    }

    public static ActiveUserManager getInstance() {
        if (instance == null) {
            instance = new ActiveUserManager();
        }
        return instance;
    }
    public void addListActiveUser(User U){
        listActiveUsers.add(U);
    }

    public void removeListActiveUser(User U){
        listActiveUsers.remove(U);
    }

    public ArrayList<User> getListActiveUser() throws UnknownHostException {
        ArrayList<User> list = new ArrayList<User>(this.listActiveUsers);
        list.remove(Self.getInstance().get_User());
        return list;
    }

    public boolean IsinActiveListUser(String Pseudo){
        try {
            for (User user: listActiveUsers){
                if (user.get_Pseudo().equals(Pseudo)){
                    return true;
                }
            }
        }
        catch (NullPointerException e){
            return false;
        }
        return false;
    }
    public String toString(){return listActiveUsers.toString();}
}
