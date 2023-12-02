package client.main.social;

import client.main.member.Member;

import javax.swing.*;
import java.awt.*;


public class MainScreen extends JFrame {

    private login login;
    private JPanel main;

    // login 객체의 맴버변수 member(Member의 객체)로부터 로그인한 정보 받아옴
    public MainScreen(login login) {

        // 프레임 설정
        this.login = login;
        setTitle("메인화면");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 배경 이미지 설정
        JLabel backgroundLabel = new JLabel(new ImageIcon("SOURCE/bg0.png"));
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        add(backgroundLabel);

        // 패널과 레이아웃 설정
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 로그인한 사용자 정보 받아오기
        displayMain(login.getMember(),panel);

        backgroundLabel.add(panel);
        Dimension size = panel.getPreferredSize();
        panel.setBounds((getWidth() - size.width)/ 2, (getHeight() - size.height) / 2, size.width, size.height);

        setVisible(true);
    }

    private void displayMain(Member member, JPanel panel){
        // 레이아웃 설정
        JPanel mainPanel = new JPanel(new GridLayout(5,1));
        mainPanel.setPreferredSize(new Dimension(400,400));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(mainPanel);

        //타이틀
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.BLACK);
        JLabel title = new JLabel("Solar System");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        mainPanel.add(titlePanel);

        //프로필
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        profilePanel.setBackground(Color.BLACK);
        System.out.println(member.getNickName());
        String mainString = member.getNickName() + "님,환영합니다!";
        System.out.println(mainString);
        JLabel nameLabel = new JLabel();
        nameLabel.setText(mainString);
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        mainPanel.add(profilePanel);
        profilePanel.add(nameLabel);

        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startPanel.setBackground(Color.BLACK);
        JButton createRbtn = new JButton("방 만들기");
        createRbtn.setBackground(Color.BLUE);
        JButton joinRbtn = new JButton("방 참여하기");
        joinRbtn.setBackground(Color.BLUE);
        mainPanel.add(startPanel);
        startPanel.add(createRbtn);
        startPanel.add(joinRbtn);

    }



    public static void main(String[] args) {
        new MainScreen(new login());
    }
}