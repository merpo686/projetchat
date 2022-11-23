package Models;

public class Validation extends Notifications{
    Boolean Valid;
    public Validation( User user, boolean Valid){
        super(user);
        this.Valid=Valid;
    }
    public boolean get_Valid(){
        return this.Valid;
    }
}
