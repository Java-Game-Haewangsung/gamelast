package client.main.view;

import client.main.GameUser;
import client.main.RoomManager;
import client.main.member.Member;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main extends JFrame {
    // 이미지 버퍼
    Image buffImg;
    Graphics buffG;
    MainMapView mainMapView;

//    public Main(MainMapView mainMapView) {
//        this.mainMapView = mainMapView;
//        main();
//    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Your Frame Title");
        // 창을 닫을 때 프로그램 종료 설정
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 프레임을 보이도록 설정
        frame.setVisible(true);
        //테스트용 객체 생성
        Member m1 = new Member("a", "a", "a");
        Member m2 = new Member("b", "a", "b");
        Member m3 = new Member("c", "a", "c");
        Member m4 = new Member("d", "a", "d");
        GameUser g1 = new GameUser(m1);
        GameUser g2 = new GameUser(m2);
        GameUser g3 = new GameUser(m3);
        GameUser g4 = new GameUser(m4);
        ArrayList<GameUser> users = new ArrayList<>();
        users.add(g1); users.add(g2); users.add(g3); users.add(g4);
        MainMapView mainMapView = new MainMapView(users, RoomManager.createRoom());
        new Thread(mainMapView).start();

    }
}


