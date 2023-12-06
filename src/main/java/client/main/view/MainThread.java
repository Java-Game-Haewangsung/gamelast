package client.main.view;

import client.main.GameUser;
import client.main.RoomManager;
import client.main.member.Member;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class MainThread extends JFrame {
    private boolean isMiniGameExecuted = false;
<<<<<<< HEAD

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

=======

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

//        if (mainMapView.getCheckTurn() == 2) {
//            // 미니게임 랜덤 실행
//            miniRan = new Random();
//            int ranNum = miniRan.nextInt(2);
//            Thread miniGameThread = null;
//
//            if (ranNum == 0) {
//                miniGameThread = new Thread(new MiniGame1(users.get(0))); // 미니게임1 실행
//            } else if (ranNum == 1) {
//                miniGameThread = new Thread(new MiniGame2(users.get(0))); // 미니게임2 실행
//            } else if (ranNum == 2) {
////            miniGameThread = new Thread((Runnable) new Mini3Main(users.get(0))); // 미니게임3 실행
//            }
//
//            miniGameThread.start(); // 미니게임 스레드 시작
//            System.out.println("미니게임 스레드 시작");
//            // 메인 스레드는 미니게임 스레드의 종료를 기다림
//            try {
//                miniGameThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        int miniscore = 0;
//        // 점수에 따라 코인 처리
//        if (!(miniGameThread.isAlive())) {
//            if (miniscore > 5) g1.setCoin(g1.getCoin() + 4);
//            System.out.println("코인: " + g1.getCoin());
//        }
    }

    public static void main(String[] args) {
        new MainThread();
    }

>>>>>>> 852e1f8a2334240e78f52837c6795ad0884b8539
}


