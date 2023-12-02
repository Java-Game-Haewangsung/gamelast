package client.main.social;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class signup extends JFrame {
    private JTextField usernameTextField;
    private JPasswordField passwordField;

    public signup() {
        // 프레임 설정
        setTitle("회원가입 화면");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 배경 이미지 설정 (가정)
        JLabel backgroundLabel = new JLabel(new ImageIcon("assets/minigame2/bg0.png"));
        backgroundLabel.setLayout(null);
        setContentPane(backgroundLabel);

        // 패널과 레이아웃 설정
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 회원가입 폼 생성
        createSignUpForm(panel);

        // 패널을 배경 이미지의 중앙에 추가
        backgroundLabel.add(panel);
        Dimension size = panel.getPreferredSize();
        panel.setBounds((getWidth() - size.width) / 2, (getHeight() - size.height) / 2, size.width, size.height);

        // 프레임 표시
        setVisible(true);
    }

    private void createSignUpForm(JPanel panel) {
        // 레이아웃 설정
        JPanel formPanel = new JPanel(new GridLayout(4,1));  // 3행 2열의 그리드 레이아웃으로 변경
        formPanel.setPreferredSize(new Dimension(300, 300));
        formPanel.setBackground(Color.BLACK);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(formPanel);

        // 회원가입 타이틀
        JPanel signUpTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signUpTitlePanel.setBackground(Color.BLACK);
        JLabel signUpTitle = new JLabel("회원가입");
        signUpTitle.setForeground(Color.WHITE);
        signUpTitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        signUpTitlePanel.add(signUpTitle);
        formPanel.add(signUpTitlePanel);

        // 회원 닉네임 입력 필드 및 중복확인 버튼
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.setBackground(Color.BLACK);;
        usernamePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel usernameLabel = new JLabel("회원 닉네임");
        usernameLabel.setForeground(Color.WHITE);  // 텍스트 색상을 흰색으로 변경
        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));  // 폰트 설정
        usernameTextField = new JTextField(10);
        JButton checkDuplicateButton = new JButton("중복확인");
        checkDuplicateButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));  // 폰트 크기를 12로 변경
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);
        usernamePanel.add(checkDuplicateButton);
        formPanel.add(usernamePanel);

        // 비밀번호 입력 필드
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.setBackground(Color.BLACK);
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordLabel.setForeground(Color.WHITE);  // 텍스트 색상을 흰색으로 변경
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));  // 폰트 설정
        passwordField = new JPasswordField(10);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        formPanel.add(passwordPanel);


        // 회원가입 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.BLACK);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JButton signUpButton = new JButton("회원가입하기");
        signUpButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));  // 폰트 설정
        btnPanel.add(signUpButton);
        formPanel.add(btnPanel);

        // 중복확인 버튼의 액션 리스너
        checkDuplicateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                if (isUsernameAvailable(username)) {
                    JOptionPane.showMessageDialog(null, "사용 가능한 닉네임입니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "이미 사용 중인 닉네임입니다.");
                }
            }
        });

        // 회원가입 버튼의 액션 리스너
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());

                if (isUsernameAvailable(username)) {
                    // 회원 정보를 DB에 저장
                    if (saveUserToDB(username, password)) {
                        JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
                        // 여기에 원하는 작업 추가 (예: 로그인 화면으로 이동)
                        dispose();
                        login login = new login();
                    } else {
                        JOptionPane.showMessageDialog(null, "회원가입에 실패했습니다.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "닉네임 중복 확인이 필요합니다.");
                }
            }
        });
    }

    private boolean isUsernameAvailable(String username) {
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
            String sql = "SELECT COUNT(*) FROM user WHERE userName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            // 쿼리 실행
            ResultSet resultSet = preparedStatement.executeQuery();

            // 결과 확인
            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                // 연결 종료
                preparedStatement.close();
                connection.close();

                // count가 0이면 사용 가능한 username
                return count == 0;
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "JDBC 드라이버를 찾지 못했습니다.");
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "데이터베이스 연결에 실패했습니다. 에러 메시지: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        // 예외가 발생하거나 검색 결과가 없는 경우 기본적으로 사용 불가능한 username으로 간주
        return false;
    }


    private boolean saveUserToDB(String username, String password) {
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
            String sql = "INSERT INTO user (userName, userPasswd) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // 쿼리 실행
            int result = preparedStatement.executeUpdate();

            // 연결 종료
            preparedStatement.close();
            connection.close();

            // 쿼리 실행 결과 반환
            return result > 0;
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
                new signup();
            }
        });
    }
}