package se.mah.ac9511.towerd;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by David on 2015-10-24.
 */


public class User implements Parcelable {
    private String name;
    private int xPos;
    private int yPos;
    private int livesLeft;
    private int score;
    private int kills;

    public int getLivesLeft() {
        return livesLeft;
    }

    public void setLivesLeft(int livesLeft) {
        this.livesLeft = livesLeft;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    int playerId;
    private double xRel=0;
    private double yRel=0;




    Status priority = Status.IDLE;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    int money;

    public User(String name, int xPos, int yPos, Status build,int money, int livesLeft,int score,int kills) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.money=money;
        this.livesLeft=livesLeft;
        this.score=score;
        this.kills=kills;

    }
    public void setPriority(Status p) {
        this.priority = p;
    }

    public Status getPriority() {
        return priority;
    }

    public double getxRel() {
        return xRel;
    }


    public void setxRel(double xRel) {
        this.xRel = xRel;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Log.d("setName", "user: "+name);
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public double getyRel() {
        return yRel;
    }

    public void setyRel(double yRel) {
        this.yRel = yRel;
    }


    protected User(Parcel in) {
        name = in.readString();
        xPos = in.readInt();
        yPos = in.readInt();
        xRel = in.readDouble();
        yRel = in.readDouble();
        score=in.readInt();
        livesLeft=in.readInt();
        kills=in.readInt();
        priority = (Status) in.readValue(Status.class.getClassLoader());
        money = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(xPos);
        dest.writeInt(yPos);
        dest.writeDouble(xRel);
        dest.writeDouble(yRel);
        dest.writeInt(score);
        dest.writeInt(livesLeft);
        dest.writeInt(kills);
        dest.writeValue(priority);
        dest.writeInt(money);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
