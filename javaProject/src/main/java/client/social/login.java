package client.social;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame {
    private JTextField usernameTextField;
    private JPasswordField passwordField;

    public login() {
        // 프레임 설정
        setTitle("로그인 화면");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 배경 이미지 설정 (가정)
        JLabel backgroundLabel = new JLabel(new ImageIcon("assets/minigame2/bg0.png"));
        backgroundLabel.setLayout(null);
        setContentPane(backgroundLabel);

        // 패널과 레이아웃 설정
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 로그인 폼 생성
        createLoginForm(panel);

        // 패널을 배경 이미지의 중앙에 추가
        backgroundLabel.add(panel);
        Dimension size = panel.getPreferredSize();
        panel.setBounds((getWidth() - size.width) / 2, (getHeight() - size.height) / 2, size.width, size.height);

        // 프레임 표시
        setVisible(true);
    }

    private void createLoginForm(JPanel panel) {
        // 레이아웃 설정
        JPanel formPanel = new JPanel(new GridLayout(6, 1));
        formPanel.setPreferredSize(new Dimension(300, 200));
        formPanel.setBackground(Color.BLACK);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(formPanel);

        // 로그인 타이틀
        JPanel loginTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginTitlePanel.setBackground(Color.BLACK);
        JLabel loginTitle = new JLabel("로그인");
        loginTitle.setForeground(Color.WHITE);
        loginTitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        loginTitlePanel.add(loginTitle);
        formPanel.add(loginTitlePanel);

        // 닉네임 입력 필드
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.setBackground(Color.BLACK);
        namePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel nameLabel = new JLabel("아이디");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        usernameTextField = new JTextField(10);
        namePanel.add(nameLabel);
        namePanel.add(usernameTextField);
        formPanel.add(namePanel);

        // 비밀번호 입력
        JPanel loginpsdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginpsdPanel.setBackground(Color.BLACK);
        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        passwordField = new JPasswordField(10);
        loginpsdPanel.add(passwordLabel);
        loginpsdPanel.add(passwordField);
        formPanel.add(loginpsdPanel);

        // 로그인 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.BLACK);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JButton loginButton = new JButton("로그인");
        loginButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        btnPanel.add(loginButton);
        formPanel.add(btnPanel);

        //회원가입으로 이동 버튼
        JPanel moveToSignupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        moveToSignupPanel.setBackground(Color.BLACK);
        moveToSignupPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JButton moveToSignupButton = new JButton("회원가입하기");
        moveToSignupButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        moveToSignupPanel.add(moveToSignupButton);
        formPanel.add(moveToSignupPanel);

        moveToSignupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current login window
                new signup(); // Open the signup window
            }
        });
        formPanel.add(moveToSignupButton);

        // 로그인 버튼의 액션 리스너
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());

                // 로그인 처리
                if (isValidLogin(username, password)) {
                    JOptionPane.showMessageDialog(null, "로그인 성공!");
                    // 여기에 로그인 후의 작업 추가 (예: 메인 화면으로 이동)
                } else {
                    JOptionPane.showMessageDialog(null, "로그인 실패. 아이디 또는 비밀번호를 확인하세요.");
                }
            }
        });
    }

    private boolean isValidLogin(String username, String password) {
        // 데이터베이스 연결 정보
        String url = "jdbc:mysql://34.64.188.49:3306/solarSystem?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "java";
        String dbPassword = "solarsystem";

        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결
            Connection connection = DriverManager.getConnection(url, user, dbPassword);

            // SQL 쿼리 작성
            String sql = "SELECT * FROM user WHERE userName = ? AND userPasswd = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // 쿼리 실행
            ResultSet resultSet = preparedStatement.executeQuery();

            // 결과 확인
            boolean isValid = resultSet.next();

            // 연결 종료
            preparedStatement.close();
            connection.close();

            return isValid;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        // 이 부분에서 Swing 컴포넌트를 생성하는 코드를 작성
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new login();
            }
        });
    }
}
