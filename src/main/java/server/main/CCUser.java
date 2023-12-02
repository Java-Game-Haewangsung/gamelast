package server.main;

import client.main.GameRoom;
import client.main.RoomManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// 서버에 접속한 유저와의 메세지 송수신을 관리하는 클래스
// 스레드를 상속받아 연결 요청이 들어왔을 때도 독립적으로 동작할 수 있도록 한다.
public class CCUser extends Thread{

    // 소켓 서버에서 생성한 RoomManager 객체. 서버 상에 하나 존재.
    RoomManager rm;
    Socket socket;

    // Database db = new Database

    /* 메시지 송수신을 위한 필드 */
    OutputStream os;
    DataOutputStream dos;
    InputStream is;
    DataInputStream dis;

    String msg; // 수신메시지를 저장할 필드
    String nickname; //클라이언트의 닉네임을 저장할 필드

    GameRoom myRoom; // 입장한 방 객체를 저장

    /* 각 메시지를 구분하기 위한 태그 */
    final String loginTag = "LOGIN";	//로그인
    final String joinTag = "JOIN";		//회원가입
    final String overTag = "OVER";		//중복확인
    final String viewTag = "VIEW";		//회원정보조회
    final String changeTag = "CHANGE";	//회원정보변경
    final String rankTag = "RANK";		//전적조회(전체회원)
    final String croomTag = "CROOM";	//방생성
    final String eroomTag = "EROOM";	//방입장
    final String cuserTag = "CUSER";	//접속유저
    final String searchTag = "SEARCH";	//전적조회(한명)
    final String pexitTag = "PEXIT";	//프로그램종료
    final String rexitTag = "REXIT";	//방퇴장
    final String omokTag = "OMOK";		//오목
    final String winTag = "WIN";		//승리
    final String loseTag = "LOSE";		//패배
    final String recordTag = "RECORD";	//전적업데이트

    CCUser(Socket s, RoomManager rm) {
        this.socket = s;
        this.rm = rm;
    }


    public void run() {
        try {
            System.out.println();
        }
    }
}
