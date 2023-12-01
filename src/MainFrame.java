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
        int collisionOccurred = 0;

        Timer blackHoleTimer;

        // 게임 시작 시간
        long startTime;

        // 남은 시간 변수
        long remainingTime = 30000; // 초기값

        // 플레이어 점수
        int playerScore = 0;

        public GamePanel() {
            // 플레이어 객체 생성
            player = new Player(400, 600, 64, 64, null);

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
                    checkCollision(); // 플레이어-블랙홀 충돌 처리
                    remainingTime -= 16; // 타이머 주기만큼 시간 감소
                    checkGameResult(); // 게임 결과 확인
                    updatePlayerScore(); // 플레이어 점수 업데이트
                    repaint();
                }
            });
            gameTimer.start();

            // 게임 시작 시간 초기화
            startTime = System.currentTimeMillis();
        }

//        // 충돌 체크
//        public boolean Crash(int x1, int y1, int x2, int y2, int w1, int h1, int w2, int h2) {
//            return x1 < x2 + w2 / 2 &&
//                    x1 + w1 > x2 - w2 / 2 &&
//                    y1 < y2 + h2 / 2 &&
//                    y1 + h1 > y2 - h2 / 2;
//        }

        // 플레이어-블랙홀 충동 처리 메소드
        void checkCollision() {
            int blackHoleW = 400;
            int blackHoleH = 150;
            for (Iterator<BlackHole> iterator = blackHoles.iterator(); iterator.hasNext(); ) {
                BlackHole blackHole = iterator.next();

                int playerLeft = player.getX() - player.getWidth() / 2;
                int playerRight = player.getX() + player.getWidth() / 2;
                int playerTop = player.getY() - player.getHeight() / 2;
                int playerBottom = player.getY() + player.getHeight() / 2;

                int blackHoleLeft = blackHole.getX() - blackHoleW / 2;
                int blackHoleRight = blackHole.getX() + blackHoleW / 2;
                int blackHoleTop = blackHole.getY() - blackHoleH / 2;
                int blackHoleBottom = blackHole.getY() + blackHoleH / 2;

                if (playerLeft < blackHoleRight && playerRight > blackHoleLeft &&
                        playerTop < blackHoleBottom && playerBottom > blackHoleTop) {
                    // 충돌이 감지되었음 - 특정 동작 수행 또는 블랙홀 제거
                    iterator.remove(); // 블랙홀을 리스트에서 제거
                    collisionOccurred++;
                    System.out.printf("플레이어와 블랙홀 충돌 횟수: %d\n", collisionOccurred);
                }
            }
        }

        // 게임 결과 확인
        void checkGameResult() {
            // 현재 시간과 시작 시간을 비교하여 30초 경과 여부 확인
            long elapsedTime = System.currentTimeMillis() - startTime;
            // 30초 동안 충돌이 없었을 경우 "통과" 표시
            if (elapsedTime < 0) {
                System.out.println("통과! 점수: " + playerScore);
                System.exit(0);
            }
            else if (collisionOccurred >= 3) {
                System.out.println("게임 종료! 점수: " + playerScore);
                System.exit(0);
            }
        }

        // 플레이어 점수 업데이트
        void updatePlayerScore() {
            // 5초마다 플레이어 점수를 10점씩 증가
            if (System.currentTimeMillis() - startTime >= 5000) {
                playerScore += 10;
                startTime = System.currentTimeMillis(); // 시간 초기화
                System.out.println("플레이어 점수: " + playerScore);
            }
        }

        // 화면 그리는 메소드
        @Override
        protected void paintComponent(Graphics g) {
            if (width == 0 || height == 0) {
                width = getWidth(); // 패널의 너비
                height = getHeight();
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

            // 플레이어 점수 표시 (오른쪽 맨 위 모서리)
            g.drawString("Score: " + playerScore, width - 100, 20);
        }

        // 블랙홀 생성 함수
        void createBlackHole() {
            int setW = 512, setH = 256;

            // 현재 시간과 비교하여 블랙홀 생성 주기를 체크
            if (System.currentTimeMillis() >= nextBlackHoleTime) {
                int x = random.nextInt(getWidth() - setW);
                int y = random.nextInt(getHeight() - setH);

                BlackHole blackHole = new BlackHole(x, y);
                blackHoles.add(blackHole);
                System.out.println("블랙홀 생성");

                // 다음 블랙홀 생성 시간 설정
                nextBlackHoleTime = System.currentTimeMillis() + random.nextInt(500);

                // 블랙홀이 일정 시간 후에 소멸하도록 타이머 설정
                Timer blackHoleLifeTimer = new Timer(random.nextInt(1000) + 6000, e -> {
                    blackHoles.remove(blackHole);
                    repaint(); // 블랙홀이 사라질 때마다 패널을 다시 그려줌
                    System.out.println("블랙홀 소멸");
                });
                blackHoleLifeTimer.setRepeats(false); // 한 번만 실행
                blackHoleLifeTimer.start();
            }
        }

        void move() {
            // 플레이어가 화면 밖으로 나가지 않도록
            if (player.getX() < 0)
                player.setX(0);
            if (player.getX() > width - player.getWidth())
                player.setX(width - player.getWidth());
            if (player.getY() < 0)
                player.setY(0);
            if (player.getY() > height - player.getHeight())
                player.setY(height - player.getHeight());

            // 블랙홀 방향으로 플레이어 좌표 및 이미지 이동
            for (BlackHole blackHole : blackHoles) {
                int blackHoleX = blackHole.getX() + blackHole.getX() / 2;
                int blackHoleY = blackHole.getY() + blackHole.getY() / 2;

                // 블랙홀 방향으로 플레이어 좌표를 이동
                if (player.getX() < blackHoleX) {
                    player.setDx(2);
                } else if (player.getX() > blackHoleX) {
                    player.setDx(-2);
                }

                // 블랙홀 방향으로 플레이어 좌표를 이동
                if (player.getY() < blackHoleY) {
                    player.setDy(2);
                } else if (player.getY() > blackHoleY) {
                    player.setDy(-2);
                }
            }

            player.move();
        }
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
