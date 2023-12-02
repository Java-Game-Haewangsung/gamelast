package client.main.social;

import client.main.member.Member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainScreen extends JFrame {

    private login login;
    private JPanel main;
    private JTextField codeInput;

    // 이미지 크기 조절 메서드
    private Image resizeImage(Image originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    Toolkit tk = Toolkit.getDefaultToolkit();
    Image background_ = tk.getImage("SOURCE/mainbg.png"); // 배경화면;
    Image background = background_.getScaledInstance(800, 800, Image.SCALE_SMOOTH);

    // login 객체의 맴버변수 member(Member의 객체)로부터 로그인한 정보 받아옴
    public MainScreen(login login) {

        // 프레임 설정
        this.login = login;
        setTitle("메인화면");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 배경 이미지 설정
        JLabel backgroundLabel = new JLabel(new ImageIcon(resizeImage(background,800,800)));

        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        add(backgroundLabel);

        // 패널과 레이아웃 설정
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 로그인한 사용자 정보 받아오기
        displayMain(login.getMember(),panel);

        backgroundLabel.add(panel);
        Dimension size = panel.getPreferredSize();
        panel.setBounds((getWidth() - size.width)/ 2, 350, 400, 150);

        setVisible(true);
    }

    private void displayMain(Member member, JPanel panel) {
        // 레이아웃 설정
        JPanel mainPanel = new JPanel(new GridLayout(3, 1));  // Change the number of rows to 3
        mainPanel.setPreferredSize(new Dimension(600, 200));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(mainPanel);

        // 중앙에서 100 픽셀 밑에 표시되도록 설정
        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startPanel.setBackground(Color.BLACK);
        mainPanel.add(startPanel);

        JButton createRbtn = new JButton("방 만들기");
        createRbtn.setBackground(Color.BLUE);
        JButton joinRbtn = new JButton("방 참여하기");
        joinRbtn.setBackground(Color.BLUE);

        JPanel invitePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        invitePanel.setBackground(Color.BLACK);
        JLabel inviteLabel = new JLabel("초대코드");
        inviteLabel.setForeground(Color.WHITE);
        codeInput = new JTextField(15);
        mainPanel.add(invitePanel);
        startPanel.add(createRbtn);
        startPanel.add(joinRbtn);
        invitePanel.add(inviteLabel);
        invitePanel.add(codeInput);

        String inviteCode = codeInput.getText();
        joinRbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inviteCode!=null){
                    int code = Integer.parseInt(inviteCode);

                }
                else{
                    JOptionPane.showMessageDialog(null,"초대코드를 입력해주세요.");
                }
            }
        });

    }

}