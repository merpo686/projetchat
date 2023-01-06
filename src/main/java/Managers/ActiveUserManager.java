package Managers;
import Models.User;

import java.net.UnknownHostException;
import java.util.ArrayList;


public class ActiveUserManager {
    static ActiveUserManager instance;

    ArrayList<User> listActiveUsers;
    private ActiveUserManager() {
        listActiveUsers = new ArrayList<>();
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
    public void changeListActiveUser(User U){
        if (IsinActiveListUser(U.get_Hostname())){
            removeListActiveUser(U.get_Hostname());
        }
        addListActiveUser(U);
    }

    public void removeListActiveUser(String hostname){
        listActiveUsers.removeIf(u -> u.equals(hostname));
    }

    public ArrayList<User> getListActiveUser() {
        return listActiveUsers;
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

    public User get_User(String hostname){
        try {
            for (User user: listActiveUsers){
                if (user.equals(hostname)){
                    return user;
                }
            }
        }
        catch (NullPointerException e){
            return null;
        }
        return null;
    }
    public String toString(){return listActiveUsers.toString();}
}
