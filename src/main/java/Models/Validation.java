package Models;

public class Validation extends Notifications{
    private Boolean Valid;
    private String Pseudo;
    public Validation( User user, String Pseudo, boolean Valid){
        super(user);
        this.Valid=Valid;
        this.Pseudo = Pseudo;
    }
    public boolean get_Valid(){
        return this.Valid;
    }
    public String get_Pseudo(){return this.Pseudo;}
}
