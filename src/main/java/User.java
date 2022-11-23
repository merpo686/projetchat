import Managers.UserManager;

public class User {
    public static void main(String[]args){
        try {
            UserManager Um= new UserManager();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
