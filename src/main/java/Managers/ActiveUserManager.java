package Managers;
import Models.User;
import java.util.ArrayList;


public class ActiveUserManager {
    static ArrayList<User> listActiveUsers;
    public ActiveUserManager(){
        listActiveUsers = new ArrayList<User>();
    }
    public static void addListActiveUser(User U){
        listActiveUsers.add(U);
    }

    public static void removeListActiveUser(User U){
        listActiveUsers.remove(U);
    }

    public ArrayList<User> getListActiveUser(){
        return listActiveUsers;
    }

    public static boolean IsinActiveListUser(String Pseudo){
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
}
