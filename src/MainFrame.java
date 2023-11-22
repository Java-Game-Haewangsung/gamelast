import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;
import javax.swing.Timer;

public class MainFrame extends JFrame {
    GamePanel panel;

    public MainFrame() {
        setTitle("미니게임1");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // 패널 객체 생성
        panel = new GamePanel();
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    // 게임 화면 Panel 클래스
    class GamePanel extends JPanel {
        Player player; // 플레이어 객체 불러옴

        Random random = new Random();
        Image backImg; // 배경 이미지
        Image playerImg; // 플레이어 이미지

        // 패널 크기
        int width;
        int height;

        // 블랙홀 객체 참조 변수
        ArrayList<BlackHole> blackHoles = new ArrayList<>();
        // 블랙홀 생성 주기
        long nextBlackHoleTime;

        // 플레이어와 블랙홀 간 충돌 여부
        boolean collisionOccurred = false;

        Timer blackHoleTimer;

        // 게임 시작 시간
        long startTime;

        // 남은 시간 변수
        long remainingTime = 30000; // 초기값 30초

        public GamePanel() {
            // 플레이어 객체 생성
            player = new Player(0, 0, 64, 64, null);

            // KeyListener 추가
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    player.keyPressed(e);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    player.keyReleased(e);
                }
            });

            // 포커스 설정
            setFocusable(true);
            requestFocusInWindow();

            blackHoleTimer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createBlackHole();
                }
            });
            blackHoleTimer.start();

            Timer gameTimer = new Timer(16, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    move(); // 플레이어 이동 처리
                    checkCollision();
                    remainingTime -= 16; // 타이머 주기만큼 시간 감소
                    checkGameResult(); // 게임 결과 확인
                    repaint();
                }
            });
            gameTimer.start();

            // 게임 시작 시간 초기화
            startTime = System.currentTimeMillis();
        }

        // 충돌 체크
        public boolean Crash(int x1, int y1, int x2, int y2, int w1, int h1, int w2, int h2) {
            return Math.abs((x1 + w1 / 2) - (x2 + w2 / 2)) < (w2 / 2 + w1 / 2) &&
                    Math.abs((y1 + h1 / 2) - (y2 + h2 / 2)) < (h2 / 2 + h1 / 2);
        }

        // 플레이어-블랙홀 충동 처리 메소드
        void checkCollision() {
            for (Iterator<BlackHole> iterator = blackHoles.iterator(); iterator.hasNext(); ) {
                BlackHole blackHole = iterator.next();

                // 플레이어와 블랙홀 이미지의 크기
                int playerWidth = 64;
                int playerHeight = 64;
                int blackHoleWidth = 512;
                int blackHoleHeight = 128;

                // 플레이어의 충돌 영역 계산
                int playerLeft = player.getX();
                int playerRight = player.getX() + playerWidth;
                int playerTop = player.getY();
                int playerBottom = player.getY() + playerHeight;

                // 블랙홀의 충돌 영역 계산
                int blackHoleLeft = blackHole.getX();
                int blackHoleRight = blackHole.getX() + blackHoleWidth;
                int blackHoleTop = blackHole.getY();
                int blackHoleBottom = blackHole.getY() + blackHoleHeight;

                // 충돌 감지
                if (Crash(playerLeft, playerTop, blackHoleLeft, blackHoleTop, playerWidth, playerHeight, blackHoleWidth, blackHoleHeight)) {
                    System.out.println("게임 오버");
                    System.out.printf("플레이어 좌표: (%d, %d, %d, %d)\n", playerLeft, playerTop, playerRight, playerBottom);
                    System.out.printf("블랙홀 좌표(l, t, r, b): (%d, %d, %d, %d)\n", blackHoleLeft, blackHoleTop, blackHoleRight, blackHoleBottom);
                    iterator.remove(); // 충돌이 감지된 블랙홀을 리스트에서 제거
                    collisionOccurred = true;
                    // System.exit(0);
                }
            }
        }

        // 게임 결과 확인
        void checkGameResult() {
            // 현재 시간과 시작 시간을 비교하여 30초 경과 여부 확인
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= 30000) {
                // 30초 동안 충돌이 없었을 경우 "통과" 표시
                if (!collisionOccurred) {
                    System.out.println("통과!");
                }
            }
        }

        // 화면 그리는 메소드
        @Override
        protected void paintComponent(Graphics g) {
            if (width == 0 || height == 0) {
                width = getWidth(); // 패널의 너비
                height = getHeight(); // 패널의 높이

                Toolkit toolkit = Toolkit.getDefaultToolkit();
                backImg = toolkit.getImage("D:\\2023-2\\AJ\\mini1\\src\\img\\backImg.jpg");
                playerImg = toolkit.getImage("D:\\2023-2\\AJ\\mini1\\src\\img\\playerImg.png");

                // 배경 이미지 리사이징
                backImg = backImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);

                // 플레이어 이미지 크기 조정
                player.setImage(playerImg.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
                player.setX(width / 2 - player.getWidth() / 2);
                player.setY(height - 100 - player.getHeight());
                playerImg = playerImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }

            // 이미지들 그림
            g.drawImage(backImg, 0, 0, this);
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);

            // 블랙홀 이미지 그리기
            for (BlackHole blackHole : blackHoles) {
                g.drawImage(blackHole.getImage(), blackHole.getX(), blackHole.getY(), this);
            }

            // 남은 시간 표시
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Time: " + remainingTime / 1000 + "s", 10, 20);
        }

        // createBlackHole 메소드
        void createBlackHole() {
            int setW = 512, setH = 256;

            // 현재 시간과 비교하여 블랙홀 생성 주기를 체크
            if (System.currentTimeMillis() >= nextBlackHoleTime) {
                int x = random.nextInt(800 - setW) + 64; // 랜덤 X 좌표
                int y = random.nextInt(800 - setH) + 100; // 랜덤 Y 좌표
                BlackHole blackHole = new BlackHole(x, y);
                blackHoles.add(blackHole);
                System.out.println("블랙홀 생성");

                // 다음 블랙홀 생성 시간 설정
                nextBlackHoleTime = System.currentTimeMillis() + random.nextInt(3000);

                // 블랙홀이 일정 시간 후에 소멸하도록 타이머 설정
                Timer blackHoleLifeTimer = new Timer(random.nextInt(3000) + 10000, e -> {
                    blackHoles.remove(blackHole);
                    repaint(); // 블랙홀이 사라질 때마다 패널을 다시 그려줌
                    System.out.println("블랙홀 소멸");
                });
                blackHoleLifeTimer.setRepeats(false); // 한 번만 실행
                blackHoleLifeTimer.start();
            }
        }

        void move() {
            player.move();
        }
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}

