package server.main;

import client.dto.readyRoomDto;
import client.main.GameRoom;
import client.main.GameUser;
import client.main.RoomManager;
import client.main.member.Member;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class SocketServer {

    RoomManager serverRM = new RoomManager();
    //메인게임마다 참여하는 클라이언트들의 소켓 집합. 서버 상에 하나만 존재. 즉 모든 클라이언트 소켓 관리.
    Vector<Vector<Socket>> socketList = new Vector<>();

    public static void main(String[] args) throws IOException {
        SocketServer socketServer = new SocketServer();
        socketServer.start();

    }

    public void start() throws IOException {
        ServerSocket server = null;
        Socket socket = null;
        try {
            // 서버 소켓 준비
            int port = 18501;
            server = new ServerSocket();
            server.bind(new InetSocketAddress("localhost", port));

            // 클라이언트의 연결 요청을 상시 대기.
            while(true) {
                System.out.println("--- 클라이언트 접속 대기중 ---");
                socket = server.accept();
                System.out.println(socket.getInetAddress() + "로부터의 연결 요청이 들어옴");

                // client가 접속할 때마다 새로운 스레드 생성
                ReceiveThread receiveThread = new ReceiveThread(socket, serverRM, socketList);
                receiveThread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                    System.out.println("[서버 종료]");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("[서버 소켓통신 에러");
                }
            }
        }
    }

    public void sendData(byte[] bytes, Socket socket) {
        try {
            OutputStream os = socket.getOutputStream();
            os.write(bytes);
            os.flush();
        } catch(Exception e1) {
            e1.printStackTrace();
        }
    }
}

// 게임방 들어가는 요청 처리
// client가 접속할 때마다 새로운 스레드 생성
class ReceiveThread extends Thread {
    Vector<Vector<Socket>> entireSocketList = new Vector<>();
    Vector<Socket> tempSocketList; //임시로 사용할 소켓리스트.
    RoomManager serverRM;
    GameUser gameUser;
    static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
    private final Object lock = new Object();

    Socket socket = null;
    InputStream in;
    OutputStream out;
    String reqName;
    private ArrayList<byte[]> bytecodeArrays = new ArrayList<>();

    public ReceiveThread (Socket socket, RoomManager rm, Vector<Vector<Socket>> socketList) {

        Collections.synchronizedList(entireSocketList); // 교통정리를 해준다.( clientList를 네트워크 처리해주는것 )
        this.socket = socket;
        this.serverRM = rm;
        this.entireSocketList = socketList;

        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
            //list.add(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        beforeGameProcess();
        //Thread.sleep(20);


    }

    public synchronized void beforeGameProcess() {
        try {
            // 다른 유저들이 생성된 방에 접속 요청
            byte[] lengthBuffer = new byte[4];
            while (in.read(lengthBuffer) != -1) {
                // 각 배열의 길이 읽기
                int length = ByteBuffer.wrap(lengthBuffer).getInt();
                // 길이에 맞게 byte 배열 읽기
                byte[] data = new byte[length];
                int readSize = in.read(data);

                //받아온 값이 0보다 클때
                if (readSize > 0 && readSize < 15) {
                    reqName = new String(data, StandardCharsets.UTF_8);
                }

                else if (readSize > 15) {
                    switch (reqName) {
                        case "CREATE_ROOM" : {
                            //받아온 byte를 Object로 변환
                            Member requestMember = toObject(data, Member.class);
                            // 확인을 위해 출력
                            System.out.println(requestMember.getNickName());
                            gameUser = new GameUser(requestMember);

                            GameRoom createdRoom = serverRM.createRoom(gameUser);
                            System.out.println("방장이 방을 생성 완료 방 코드는 " + createdRoom.getRoomCode());
                            System.out.println("총인원" + createdRoom.getJoinNum());
                            gameUser.setId(1);
                            Vector<Socket> s = new Vector<>();
                            s.add(socket);
                            entireSocketList.add(s); // gameManager의 gameRooms의 순서대로 해당 게임방의 클라이언트들 소켓을 저장하도록 함.
                            int idx = entireSocketList.size() - 1;

                            // 생성된 방의 코드를 요청을 보낸 클라이언트 (방장) 에게 다시 전송
                            ArrayList<Member> mList = new ArrayList<>();

                            mList.add(gameUser.getMember());
                            readyRoomDto dto = new readyRoomDto(gameUser.getRoom().getJoinNum(), gameUser.getRoom().getRoomCode(), mList);
                            dto.setRoomId(idx);

                            byte[] userData = toByteArray(dto);
                            out.write(userData);
                            out.flush();

                            break;

                        }
                        case "JOIN_ROOM" : {
                            //받아온 byte를 Object로 변환
                            Member requestMember = toObject(data, Member.class);
                            // 확인을 위해 출력
                            gameUser = new GameUser(requestMember);


                            if (serverRM.enterRoomByCode(gameUser, requestMember.getJoinCode())) {
                                System.out.println(requestMember.getJoinCode());
                                System.out.println(requestMember.getNickName()+"유저의 게임방 접속에 성공하였습니다.");
                                int uid = gameUser.getRoom().getJoinNum();
                                gameUser.setId(uid);
                                System.out.println("총인원" + uid );

                                // 사용자가 접속한 게임방의 index로 대응되는 클라이언트 소켓들 리스트 위치 찾아 소켓 저장
                                int idx = serverRM.getGameRooms().indexOf(gameUser.getRoom());
                                entireSocketList.get(idx).add(socket);

                                int joinN = gameUser.getRoom().getJoinNum();

                                ArrayList<Member> mList = new ArrayList<>();

                                for(int i = 0; i < joinN; i++) {
                                    GameUser u = gameUser.getRoom().getGameUsers().get(i);
                                    mList.add(u.getMember());
                                }

                                readyRoomDto dto = new readyRoomDto(gameUser.getRoom().getJoinNum(), gameUser.getRoom().getRoomCode(), mList);
                                dto.setRoomId(idx);
                                byte[] userData = toByteArray(dto);
                                System.out.println(userData);

                                //서버로 내보내기 위한 출력 스트림 뚫음
                                OutputStream os = socket.getOutputStream();
                                //출력 스트림에 데이터 쓰기
                                os.write(userData);
                                //서버로 송신
                                os.flush();

                            }
                            else {
                                System.out.println("게임방에 실패하였습니다.");
                            }
                            break;
                        }

                        case "IS_IT_READY" : {
                            readyRoomDto dto = toObject(data, readyRoomDto.class);

                            GameRoom r = serverRM.getRoomByCode(dto.getRoomCode());
                            System.out.println("준비? 방코드" + r.getRoomCode());

                            ArrayList<Member> mList = new ArrayList<>();

                            for(int i = 0; i < r.getJoinNum(); i++) {
                                GameUser u = r.getGameUsers().get(i);
                                mList.add(u.getMember());
                            }

                            readyRoomDto response = new readyRoomDto(r.getJoinNum(), r.getRoomCode(), mList);


                        }
                    }

                }
                else {
                    System.out.println("스트림읽기 오류");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //broadcast(room.getRoomCode(), "접속 성공");
        }
        System.out.println("끝");
    }

    public boolean isRoomFull(GameRoom room) {
        if (room.getJoinNum() >= 3) {
            return true;
        }
        else return false;
    }


    private void broadcast(int roomCode, String msg) {
        for (PrintWriter out: list) {
            out.println(msg);
            out.flush();
        }
    }

    // 역직렬화, 클라이언트로부터 받은 byte array를 객체로.
    public synchronized static <T> T toObject(byte[] bytes, Class<T> type) {
        T obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = type.cast(ois.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public static int byteArrayToInt(byte[] byteArray) {
        // ByteBuffer를 사용하여 바이트 배열을 정수로 변환
        return ByteBuffer.wrap(byteArray).getInt();
    }

    // int를 byte 배열로 변환하는 메서드
    private static byte[] intToByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    // 소켓을 통해 Server에 보내기 위해 Object를 byte로 변환
    public static byte[] toByteArray (Object obj)
    {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            bytes = bos.toByteArray();
        }
        catch (IOException ex) {
            //TODO: Handle the exception
            System.out.println("toByteArray 오류");
            ex.printStackTrace();
        }
        return bytes;
    }



}