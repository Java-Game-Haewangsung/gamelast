package client.main.socket;

import client.main.member.Member;
import client.main.social.MainScreen;
import client.main.view.MainThread;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ClientThread extends Thread{

    private Socket ctSocket;
    private InputStream in;
    private OutputStream out;
    private StringBuffer buffer;
    private Thread thisThread;
    private MainThread mainThreadMapStart;
    private MainScreen joinClient;
    private Member joinMember;
    private String reqName;
    private ArrayList<byte[]> bytecodeArrays = new ArrayList<>();

    public ClientThread(Member m, String req) {
        try {
            ctSocket = new Socket("localhost" , 18501);
            in = ctSocket.getInputStream();
            out = ctSocket.getOutputStream();
            buffer = new StringBuffer(4096);
            thisThread = this;
            joinMember = m;
            reqName = req;

        } catch(IOException e) {
            System.out.println("클라이언트 통신 오류");
        }
    }

    public void run() {
        try {
            switch (reqName) {
                case "JOIN_ROOM" : {
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
                }

                case "CREATE_ROOM": {
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
                }

            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        }
        return bytes;
    }

    private static byte[] intToByteArray(int value) {
        byte[] result = new byte[4];
        result[0] = (byte) (value >> 24);
        result[1] = (byte) (value >> 16);
        result[2] = (byte) (value >> 8);
        result[3] = (byte) (value);
        return result;
    }
}
