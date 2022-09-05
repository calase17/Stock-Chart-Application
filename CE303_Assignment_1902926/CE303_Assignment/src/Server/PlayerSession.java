package Server;

public class PlayerSession {
    private final int  id;
    private boolean hasBall;

    public PlayerSession(int id, boolean hasBall){
        this.id = id;
        this.hasBall = hasBall;
    }

    public int getId(){
        return id;
    }

    public void setHasBall(boolean ball){
        this.hasBall = ball;
    }

    public boolean getHasBall(){
        return this.hasBall;
    }

}
