package Managers;
import Models.User;
import java.util.ArrayList;


public class ActiveUserManager {
    User U;
    ArrayList<User> listActiveUsers;
    public ActiveUserManager(User U){
        this.U = U;
        this.listActiveUsers = new ArrayList<User>();
    }
    public void addListActiveUser(User U){
        listActiveUsers.add(U);
    }

    public void removeListActiveUser(User U){
        listActiveUsers.remove(U);
    }

    ArrayList<User> getListActiveUser(){
        return this.listActiveUsers;
    }
}
