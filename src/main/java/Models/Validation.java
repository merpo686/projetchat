package Models;

public class Validation extends Notifications{
    Boolean Valid;
    public Validation( User user, String Pseudo, boolean Valid){
        super(user, Pseudo);
        this.Valid=Valid;
    }
    public boolean get_Valid(){
        return this.Valid;
    }
}
