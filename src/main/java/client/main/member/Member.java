package client.main.member;

import java.io.Serializable;

public class Member implements Serializable {
    private String id;
    private int userId;
    private String password;
    private String nickName;
    private int winNum;
    private int loseNum;

    public Member(String id, String password, String nickName) {
        this.id = id;
        this.password = password;
        this.nickName = nickName;
    }

    public Member(int userId, String nickName,int winNum, int loseNum) {
        this.userId = userId;
        this.nickName = nickName;
        this.winNum = winNum;
        this.loseNum = loseNum;
    }
    public String getId() {
        return id;
    }

    public int getUserId() { return userId; }

    public String getPassword() {
        return password;
    }

    public String getNickName() {
        return nickName;
    }

    public int getWinNum() {
        return winNum;
    }

    public int getLoseNum() {
        return loseNum;
    }
}
