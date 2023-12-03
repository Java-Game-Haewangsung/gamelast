package server.main;

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

public class SocketServer {

    RoomManager serverRM = new RoomManager();

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
                ReceiveThread receiveThread = new ReceiveThread(socket, serverRM);
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

    RoomManager serverRM;
    static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());
    private final Object lock = new Object();

    Socket socket = null;
//    BufferedReader in = null;
//    PrintWriter out = null;
    InputStream in;
    OutputStream out;
    String reqName;

    public ReceiveThread (Socket socket, RoomManager rm) {
        this.socket = socket;
        this.serverRM = rm;
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
        // given. 회원가입한 회원. 테스트용
        //Main main = new Main();

        try {
            System.out.println("try 문 안");
            // 해당 유저가 게임 방을 생성
            // broadcast(room.getRoomCode(), "방" + room.getId() + "에 접속하였습니다.");

            // client 정보(Member 객체)를 수신
//            Member requestMember = null;
//            GameUser gameUser = null;

            // 다른 유저들이 생성된 방에 접속 요청
            synchronized (lock) {

                //BufferedReader initialReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("아");
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
                                GameUser gameUser = new GameUser(requestMember);

                                GameRoom createdRoom = serverRM.createRoom(gameUser);
                                System.out.println("방장이 방을 생성 완료 방 코드는 " + createdRoom.getRoomCode());

                            }
                            case "JOIN_ROOM" : {
                                //받아온 byte를 Object로 변환
                                Member requestMember = toObject(data, Member.class);
                                // 확인을 위해 출력
                                System.out.println(requestMember.getNickName());
                                GameUser gameUser = new GameUser(requestMember);

                                System.out.println("아");
                                System.out.println(requestMember.getNickName());

                                if (serverRM.enterRoomByCode(gameUser, requestMember.getJoinCode())) {
                                    System.out.println("게임방 접속에 실패하였습니다.");
                                }
                                else {
                                    System.out.println("게임방에 접속하였습니다.");
                                }
                            }
                        }

                    }
                    else {
                        System.out.println("스트림읽기 오류");
                    }
                }


//                byte[] recvBuffer = new byte[100000];
//                InputStream is = socket.getInputStream();
//                int readSize = is.read(recvBuffer);
//                Member requestMember = toObject(recvBuffer, Member.class);


                //======


//                while (true) {
//                    requestMember = receive(socket);
//                    if (requestMember == null) {
//                        // 클라이언트가 연결을 끊었을 때
//                        break;
//                    }
//                    gameUser = new GameUser(requestMember);
//                    //int code = Integer.parseInt(inputMsg);
//                    if (serverRM.enterRoomByCode(gameUser, room.getId())) {
//                        System.out.println("게임방 접속에 실패하였습니다.");
//                    }
//                    else {
//                        System.out.println("게임방에 접속하였습니다.");
//                    }
//                }

                lock.notify();
            }

        } catch (IOException e) {
//            System.out.println("게임 방 접속 실패");
            e.printStackTrace();
        } finally {
            //broadcast(room.getRoomCode(), "접속 성공");
        }
        System.out.println("끝");

    }

    public static synchronized Member receive(Socket socket) throws IOException {
        System.out.println("리시브");
        //수신 버퍼의 최대 사이즈 지정
        int maxBufferSize = 1024;
        //버퍼 생성
        byte[] recvBuffer = new byte[maxBufferSize];

        System.out.println("왜그래11");
        InputStream is = socket.getInputStream();
        System.out.println("왜그래1");
        //버퍼(recvBuffer) 인자로 넣어서 받음. 반환 값은 받아온 size
        int nReadSize = is.read(recvBuffer);


        System.out.println("왜그래");
        System.out.println(nReadSize);
        Member requestMember = null;
        //받아온 값이 0보다 클때
        if (nReadSize > 0) {
            //받아온 byte를 Object로 변환
            requestMember = toObject(recvBuffer, Member.class);
            // 확인을 위해 출력
            System.out.println(requestMember.getNickName());
        }

        return requestMember;
    }

    private void broadcast(int roomCode, String msg) {
        for (PrintWriter out: list) {
            out.println(msg);
            out.flush();
        }
    }

    // 역직렬화, 클라이언트로부터 받은 byte array를 Member객체로.
    public static <T> T toObject(byte[] bytes, Class<T> type) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return type.cast(obj);
    }


}
