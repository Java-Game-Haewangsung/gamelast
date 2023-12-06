package client.main;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GameRoom implements Serializable {

    private int id; //
    private ArrayList<GameUser> gameUsers = new ArrayList<>();
    private GameUser gameOwner; // 방장
    private String roomName;
    private int roomCode;
    private int joinNum = 0; // gameRoom에 참여한 유저 수
    private LocalDateTime startTime; //생성된 시간
//    private MainMap mainMap = new MainMap();

    /**
     * 게임룸 생성 및 게임유저 입장 로직
     */
    //아무도 없는 방 생성
    public GameRoom() {
        createRoomCode();
        this.startTime = LocalDateTime.now();
    }

    public GameRoom(int roomId, int roomCode) {
        this.id = roomId;
        this.roomCode = roomCode;
        this.startTime = LocalDateTime.now();
    }

    //유저가 방 만들 때
    public GameRoom(int roomId, GameUser user) {
        this.id = roomId;
        //this.joinNum += 1;
        createRoomCode();

        gameUsers.add(user);
        this.gameOwner = user;
        user.enterRoom(this);
        this.startTime = LocalDateTime.now();
    }

    //유저리스트가 방을 생성할 때
    public GameRoom(int roomId, ArrayList<GameUser> users) {
        this.id = roomId;
        createRoomCode();
        this.gameUsers = users;

        //유저를 게임룸으로 입장시킴
        for (GameUser user : users) {
            user.enterRoom(this);
            this.joinNum += 1;
        }

        this.gameOwner = users.get(0); //첫 번째 유저를 방장으로 설정
        this.startTime = LocalDateTime.now();
    }


    //5자리의 난수를 roomCode의 값으로 할당
    public void createRoomCode() {
        this.roomCode = (int)(Math.random() * 89999) + 10000;
    }

    // 유저를 게임룸으로 입장시킴
    public void enterUser(GameUser user) {
        //user.enterRoom(this);
        this.gameUsers.add(user);
    }

    // 유저를 게임룸으로 입장시킴
    public void enterUser(ArrayList<GameUser> users) {
        for (GameUser user : users) {
            user.enterRoom(this);
        }
        gameUsers.addAll(users);
    }

    /**
     * 게임 유저의 게임 룸 퇴장 로직
     */

    // 인자로 받은 유저 방에서 내보냄
    public void exitUser(GameUser user) {
        user.exitRoom();
        gameUsers.remove(user);

        // 모든 유저가 방은 나갔다면
        if (gameUsers.size() < 1) {
            RoomManager.removeRoom(this);
        }
    }

    /**
     * 해당 게임룸의 유저를 다 퇴장시키고 게임룸을 삭제
     */
    public void close() {
        for (GameUser gameUser : gameUsers) {
            gameUser.exitRoom();
        }
        this.gameUsers.clear();
        this.gameUsers = null;
    }

    // ==== 게임 로직 ====

    /**
     * 해당 byte 배열을 방의 모든 유저에게 전송
     * @param data 보낼 data
     */
    public void broadcast(byte[] data) {
        for (GameUser user : gameUsers) { //방에 속한 유저의 수만큼 반복
            //각 유저에세 데이터를 전송하는 메서드 호출
            // ex) user.SendData(data);

//         try {
//            user.sock.getOutputStream().write(data); // 이런식으로 바이트배열을 보낸다.
//         } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//         }

        }
    }

    public void setRoomName(String name) {
        this.roomName = name;
    }



    public int getId() {
        return id;
    }

    public ArrayList<GameUser> getGameUsers() {
        return gameUsers;
    }

    public GameUser getGameOwner() {
        return gameOwner;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void plusJoinNum(int i) {
        this.joinNum += i;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameRoom gameRoom = (GameRoom) o;

        return id == gameRoom.id;
    }

    @Override
    public int hashCode() {
        return id;
    }


}