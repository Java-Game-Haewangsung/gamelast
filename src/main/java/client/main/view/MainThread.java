package client.main.view;

import client.main.GameUser;
import client.main.RoomManager;
import client.main.member.Member;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class MainThread extends JFrame {
    private boolean isMiniGameExecuted = false;

    public MainThread() {
        Member m1, m2, m3; // 유저 객체
        GameUser g1, g2, g3; // 인게임 플레이어 객체
        ArrayList<GameUser> users; // 플레이어들 저장 리스트
        MainMapView mainMapView;
        Random miniRan; // 미니게임 랜덤실행용


        // (테스트용) 멤버, 플레이어 객체 생성
        m1 = new Member("a", "a", "팽도리");
        m2 = new Member("b", "a", "이상해씨");
        m3 = new Member("c", "a", "파이리");
        g1 = new GameUser(m1);
        g2 = new GameUser(m2);
        g3 = new GameUser(m3);

        users = new ArrayList<>();
        users.add(g1);
        users.add(g2);
        users.add(g3);
        mainMapView = new MainMapView(users, RoomManager.createRoom());

        // 타이틀 설정
        setTitle("Solar System");
        // 프레임 크기 및 속성 설정
        setSize(800, 800);
        // MainMapView를 프레임에 추가
        getContentPane().add(mainMapView);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new Thread(mainMapView).start();
    }

    public static void main(String[] args) {
        new MainThread();
    }

}


