package client.main.socket;

import client.main.member.Member;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {

    public static void main(String[] args) {
        MultiClient multiClient = new MultiClient();
        multiClient.start();
    }

    public void start() {
        Socket socket = null;
        BufferedReader in = null;
        try {
            socket = new Socket("localhost" , 18501);
            System.out.println("서버와 연결되었습니다.");

            Member testM1 = new Member("kkk", "111", "방접속유저1");
            Thread sendThread = new SendThread(socket, testM1);
            sendThread.start();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (in != null) {
                //MainMapView mainMapView =
                String inputMsg = in.readLine();
                if(("방 접속 완료").equals(inputMsg)) break;
                System.out.println("From:" + inputMsg);

            }
//            byte[] data = toByteArray(testM1);
//            System.out.println(data);




            //서버에 회원 객체 보내기
            //send(socket);



        } catch (IOException e) {
            System.out.println("서버 접속 끊김");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[서버 연결종료]");
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
        }
        return bytes;
    }
}

class SendThread extends Thread {
    Socket socket = null;
    Member m;

    Scanner scanner = new Scanner(System.in);

    public SendThread(Socket socket, Member m) {
        this.socket = socket;
        this.m = m;
    }

    @Override
    public void run() {
        try {

            //생성한 Member 객체를 byte array로 변환
            byte[] data = toByteArray(m);
            System.out.println(data);
            // client, 회원 정보를 서버에 전송
           // PrintStream out = new PrintStream(socket.getOutputStream());
            //out.println(data);

            //서버로 내보내기 위한 출력 스트림 뚫음
            OutputStream os = socket.getOutputStream();
            //출력 스트림에 데이터 쓰기
            os.write(data);
            //서버로 송신
            os.flush();

            // 계속 서버와 통신
//            while (true) {
//                String outputMsg = scanner.nextLine();
//                os.write(outputMsg);
//                out.flush();
//                if("quit".equals(outputMsg)) break;
//
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        }
        return bytes;
    }
}
