package client.main.socket;

import client.dto.readyRoomDto;
import client.main.GameRoom;
import client.main.GameUser;
import client.main.member.Member;
import client.main.social.MainScreen;
import client.main.view.MainMapView;
import client.main.view.MainThread;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ClientThread extends Thread {

    private Socket ctSocket;
    private InputStream in;
    private OutputStream out;
    private StringBuffer buffer;
    private Thread thisThread;
    private MainScreen joinClient;
    private Member joinMember;
    private String reqName;
    private ArrayList<byte[]> bytecodeArrays = new ArrayList<>();
    private ArrayList<GameUser> players = new ArrayList<>();
    private GameUser gameUser; // 호스트 플레이어
    private GameRoom gameRoom;
    private MainThread mt;
    private int myRoomJoinN; // 클라이언트가 참여한 방의 현재 인원
    private boolean EXIT = false;
    private MainMapView mainMapView;
    private MainThread mainThread;
    private boolean isStarted = false;
    private readyRoomDto readyRoomDto;

    public ClientThread(Member m, String req) {
        try {
            ctSocket = new Socket("localhost", 18501);
            in = ctSocket.getInputStream();
            out = ctSocket.getOutputStream();
            buffer = new StringBuffer(4096);
            thisThread = this;
            joinMember = m;
            reqName = req;

        } catch (IOException e) {
            System.out.println("클라이언트 통신 오류");
        }
    }

    public void run() {

        try {
            beforeGameProcess();
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(!isStarted) {
            getReady();
        }

    }

    // 게임 중. 사용자가 주사위 굴릴 때
    public void diceRolling() {
    }

    public synchronized void beforeGameProcess() {
        try {
            /**
             * 보내기
             */
            //클라이언트에서 보내려는 요청 이름에 따라 소켓에 담을 데이터 다르게 처리
            switch (reqName) {
                // 클라이언트가 입력한 코드로 게임방 참여. 서버에서 생성된 gameRoom 객체와 gameUser 객체 받아옴
                case "JOIN_ROOM": {
                    //생성한 Member 객체를 byte array로 변환
                    byte[] memberData = toByteArray(joinMember);
                    System.out.println(memberData);
                    byte[] reqData = reqName.getBytes(StandardCharsets.UTF_8);

                    bytecodeArrays.add(reqData);
                    bytecodeArrays.add(memberData);

                    for (byte[] code : bytecodeArrays) {
                        // 각 배열의 길이 전송
                        out.write(ByteBuffer.allocate(4).putInt(code.length).array());
                        // 실제 배열 전송
                        out.write(code);
                    }

                    out.flush();

                    /**
                     * 받기
                     */
                    byte[] data = new byte[10000];
                    int readSize = in.read(data);
                    readyRoomDto = toObject(data, readyRoomDto.class);

                    System.out.println("현재까지 방 참여 인원: " + readyRoomDto.getpNum());

                    this.reqName = "IS_IT_READY";
                    if (readyRoomDto.getpNum() >= 3) {
                        System.out.println("준비 요청");
                        //this.reqName = "START_MAIN_GAME";
//                        for(int i = 0; i < 2; i++) {
//                            GameUser g = new GameUser(readyRoomDto.getPlayMembers().get(i));
//                            players.add(g);
//                        }
//                        this.mainThread = new MainThread(players);
                    }
                    //else ()

//                    // 데이터를 읽어올 버퍼 생성
//                    byte[] buffer = new byte[4];
//                    // 데이터를 읽어옴
//                    int bytesRead = in.read(buffer);
//                    // 읽어온 데이터를 int로 변환
//                    if (bytesRead == 4) {
//                        int receivedInt = byteArrayToInt(buffer);
//                        System.out.println("현재까지 방 참여 인원: " + gameUser.getRoom().getJoinNum());
//                    }
                    break;
                }

                // 게임방 생성 요청, 서버 소켓으로 Member 객체 보내고, ! 서버에서 만든 GameUser 객체와 GameRoom 객체 받아옴

                case "CREATE_ROOM": {
                    //생성한 Member 객체를 byte array로 변환
                    byte[] memberData = toByteArray(joinMember);
                    //System.out.println(memberData);
                    byte[] reqData = reqName.getBytes(StandardCharsets.UTF_8);
                    bytecodeArrays.add(reqData);
                    bytecodeArrays.add(memberData);
                    for (byte[] code : bytecodeArrays) {
                        // 각 배열의 길이 전송
                        out.write(ByteBuffer.allocate(4).putInt(code.length).array());
                        // 실제 배열 전송
                        out.write(code);
                    }
                    out.flush();
                    // 받기
                    //수신 버퍼의 최대 사이즈 지정

                    byte[] data = new byte[10000];
                    int readSize = in.read(data);
                    readyRoomDto = toObject(data, readyRoomDto.class);
                    System.out.println(readyRoomDto.getPlayMembers());

                    break;
                }

                // 대기실에서 모인 인원 3명인지 확인 후 3명이면 게임 시작
                case "IS_READY" : {
                    reqName = "START_MAIN_GAME";


                }

            }

            /**
             * 받기
             */



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getReady() {
        try {
            //보내기
            if (this.reqName == "IS_IT_READY") {
                //보내기
                byte[] reqData = reqName.getBytes(StandardCharsets.UTF_8);
                byte[] idxData = toByteArray(readyRoomDto);
                bytecodeArrays.add(reqData);
                bytecodeArrays.add(idxData);
                for (byte[] code : bytecodeArrays) {
                    // 각 배열의 길이 전송
                    out.write(ByteBuffer.allocate(4).putInt(code.length).array());
                    // 실제 배열 전송
                    out.write(code);
                }
                out.flush();

                //받기
                byte[] data = new byte[10000];
                int readSize = in.read(data);
                readyRoomDto dto = toObject(data, readyRoomDto.class);


                if (dto.getpNum() >= 3) {
                    System.out.println("게임 시작");
                    this.reqName = "START_MAIN_GAME";
                    for(int i = 0; i < 2; i++) {
                        GameUser g = new GameUser(dto.getPlayMembers().get(i));
                        players.add(g);
                    }
                    this.isStarted = true;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            bytes = bos.toByteArray();
        } catch (IOException ex) {
            //TODO: Handle the exception
            System.out.println("toByteArray 오류");
        }
        return bytes;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    private static byte[] intToByteArray(int value) {
        byte[] result = new byte[4];
        result[0] = (byte) (value >> 24);
        result[1] = (byte) (value >> 16);
        result[2] = (byte) (value >> 8);
        result[3] = (byte) (value);
        return result;
    }

    public static void receive(Socket socket) throws IOException {

        //수신 버퍼 생성
        byte[] lengthBuffer = new byte[4];
        //서버로부터 받기 위한 입력 스트림 뚫음
        InputStream is = socket.getInputStream();

        while (is.read(lengthBuffer) != -1) {
            // 각 배열의 길이 읽기
            int length = ByteBuffer.wrap(lengthBuffer).getInt();
            // 길이에 맞게 byte 배열 읽기
            byte[] receiveData = new byte[length];
            int readSize = is.read(receiveData);

            //받아온 값이 0보다 클때
            if (readSize > 0 && readSize < 15) {
                //reqName = new String(data, StandardCharsets.UTF_8);
            } else {
                System.out.println("스트림읽기 오류");
            }

        }
    }


    // byte 배열을 int로 변환하는 메서드
    private static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
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
}