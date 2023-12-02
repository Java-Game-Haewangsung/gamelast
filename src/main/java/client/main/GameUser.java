package client.main;

import client.main.mainmap.Item;
import client.main.member.Member;

import java.net.Socket;
import java.util.ArrayList;

public class GameUser {
    private int id;
    private Member member;
    private String nickName;
    private Socket sock;
    private GameRoom room;

    private ArrayList<Item> userItems;

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
