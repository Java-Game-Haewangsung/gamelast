package client.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomManager implements Serializable {
    private int id;
    private static HashMap<Integer, GameRoom> roomInfo = new HashMap<>();
    private static ArrayList<GameRoom> gameRooms = new ArrayList<>();
    private static ArrayList<Integer> roomCodes = new ArrayList<>();
    private static AtomicInteger automicInteger = new AtomicInteger();

    public RoomManager() {
    }

    /**
     * 빈 방을 생성
     * @return GameRoom
     */
    public static GameRoom createRoom() { // 빈 방을 새로 생성
        int roomId = automicInteger.incrementAndGet(); // room id 채번
        GameRoom room = new GameRoom(roomId, (int)(Math.random() * 89999) + 10000);
        gameRooms.add(room);
        roomCodes.add(room.getRoomCode());
        roomInfo.put(room.getRoomCode(), room);
        System.out.println("빈 방이 생성되었습니다!");
        return room;
    }

    /**
     * 유저가 방을 생성
     * @param owner 방장
     * @return GameRoom
     */
    public static GameRoom createRoom(GameUser owner) {
        int roomId = automicInteger.incrementAndGet();

        GameRoom room = new GameRoom(roomId, owner);
        gameRooms.add(room);
        roomCodes.add(room.getRoomCode());
        roomInfo.put(room.getRoomCode(), room);

        System.out.println("유저가 방을 만들었습니다! 해당 유저는 방장이 됩니다.");

        return room;
    }

    /**
     * 유저 리스트로 방을 생성
     * @param users 입장시킬 유저 리스트
     * @return GameRoom
     */
    public static GameRoom createRoom(ArrayList<GameUser> users) {
        int roomId = automicInteger.incrementAndGet();

        GameRoom room = new GameRoom(roomId, users);
        gameRooms.add(room);
        roomCodes.add(room.getRoomCode());
        roomInfo.put(room.getRoomCode(), room);

        System.out.println("유저 리스트로 방을 생성했습니다!");

        return room;
    }

    public static GameRoom getRoom(GameRoom gameRoom){

        int idx = gameRooms.indexOf(gameRoom);

        if(idx > 0){
            return gameRooms.get(idx);
        }
        else{
            return null;
        }
    }

    /**
     * 유저가 roomCode로 해당 (이미 생성된)gameRoom에 접속
     * @param user, roomCode
     */
    public static boolean enterRoomByCode(GameUser user, int roomCode) {
        if (roomInfo.containsKey(roomCode)) {
            GameRoom r = roomInfo.get(roomCode);

            if (r.getJoinNum() < 4) {
                user.enterRoom(r);
                return true;
            }
            else {
                System.out.println(r.getJoinNum() + "인원 다 찼음");
                return false; // 방에 인원이 다 찼으면 입장 불가
            }
        }
        System.out.println("해당 코드인 게임방이 존재하지 않음");
        return false; // 해당 코드인 게임방이 존재하지 않으면 false 반환
    }

    public static GameRoom getRoomByCode(int roomCode) {
        if (roomInfo.containsKey(roomCode)) {
            GameRoom r = roomInfo.get(roomCode);

            return r;
        }
        return null;
    }

    /**
     * 인자로 받은 게임방을 제거
     * @param room 제거할 방
     */
    public static void removeRoom(GameRoom room) {
        room.close();
        gameRooms.remove(room);
        System.out.println("해당 게임방이 제거되었습니다!");
    }

    /**
     * 현재 존재하는 게임방들의 개수 반환
     * @return
     */
    public static int roomCount() {
        return gameRooms.size();
    }


    public static HashMap<Integer, GameRoom> getRoomInfo() {
        return roomInfo;
    }

    public static void setRoomInfo(HashMap<Integer, GameRoom> roomInfo) {
        RoomManager.roomInfo = roomInfo;
    }

    public static ArrayList<GameRoom> getGameRooms() {
        return gameRooms;
    }

    public static void setGameRooms(ArrayList<GameRoom> gameRooms) {
        RoomManager.gameRooms = gameRooms;
    }

    public static ArrayList<Integer> getRoomCodes() {
        return roomCodes;
    }

    public static void setRoomCodes(ArrayList<Integer> roomCodes) {
        RoomManager.roomCodes = roomCodes;
    }
}