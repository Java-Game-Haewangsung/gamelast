
package client.dto;

import client.main.member.Member;

import java.io.Serializable;
import java.util.ArrayList;

public class readyRoomDto implements Serializable {
    int pNum;
    int roomCode;
    ArrayList<Member> playMembers = new ArrayList<>();
    int roomId;

    public readyRoomDto(int pNum, int roomCode, ArrayList<Member> playMembers) {
        this.pNum = pNum;
        this.roomCode = roomCode;
        this.playMembers = playMembers;
    }

    public int getpNum() {
        return pNum;
    }

    public void setpNum(int pNum) {
        this.pNum = pNum;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }

    public ArrayList<Member> getPlayMembers() {
        return playMembers;
    }

    public void setPlayMembers(ArrayList<Member> playMembers) {
        this.playMembers = playMembers;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}