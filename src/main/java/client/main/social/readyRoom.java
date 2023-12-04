package client.main.social;

import client.main.GameRoom;
import client.main.view.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class readyRoom extends JFrame {
    boolean flag = false;
    public readyRoom(GameRoom room) {
        // 프레임 설정
        setTitle("로그인 화면");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 배경 이미지 설정
        JLabel backgroundLabel = new JLabel(new ImageIcon("SOURCE/readybg.png"));
        backgroundLabel.setLayout(null);
        setContentPane(backgroundLabel);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBounds(200,550,400,100);
        JLabel inviteCodeLabel = new JLabel("초대코드 : ");
        panel.setBackground(Color.BLACK);
        inviteCodeLabel.setForeground(Color.WHITE);
        inviteCodeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        JButton startBtn = new JButton("게임 시작");
        add(panel);
        panel.add(inviteCodeLabel);
        panel.add(startBtn);

        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 3명일때
                flag = true;
            }
        });

        setVisible(true);
    }

    public void startGame() {
        if(flag==true){
            new Main();
        }
    }

    public static void main(String[] args) {

    }


}
