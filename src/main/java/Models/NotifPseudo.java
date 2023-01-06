package Models;

public class NotifPseudo extends Notifications{
    private String Pseudo;
    public NotifPseudo( User user, String Pseudo){
        super(user);
        this.Pseudo=Pseudo;
    }
    public String get_Pseudo(){return this.Pseudo;}
}
