package client.main;

import client.main.member.Member;

import java.awt.*;
import java.net.Socket;

public class GameUser {
    private int id;
    private Member member;
    private String nickName;
    private Socket sock;
    private GameRoom room;
    private int enteredCode;
    Image img;
    Toolkit tk = Toolkit.getDefaultToolkit();
    int posX;
    int posY;
    int width;
    int height;
    private int coin;
    private int sun;


    public GameUser(Member member) {
        this.member = member;
        this.nickName = member.getNickName();
    }

    /**
     * 방에 입장시킴
     * @param room  입장할 방
     */
    public void enterRoom(GameRoom room) {
        this.room = room;
    }

    /**
     * 방에서 퇴장
     */
    public void exitRoom() {
        this.room = null;
    }

    public GameRoom getRoom() {
        return room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Socket getSock() {
        return sock;
    }

    public void setSock(Socket sock) {
        this.sock = sock;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public int getEnteredCode() {
        return enteredCode;
    }

    public void setEnteredCode(int enteredCode) {
        this.enteredCode = enteredCode;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getSun() {
        return sun;
    }

    public void setSun(int sun) {
        this.sun = sun;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    /*
                        equals와 hashCode를 override 해줘야, 동일유저를 비교할 수 있다
                        비교할 때 -> gameUser 간 equals 비교, list에서 find 등
                     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameUser gameUser = (GameUser) o;

        return id == gameUser.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
