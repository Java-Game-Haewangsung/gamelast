package client.main.minigame.mini3;

import client.main.GameUser;
import client.main.member.Member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;

class TestMiniView extends JFrame {
    Toolkit imageTool = Toolkit.getDefaultToolkit();
    Image flight_ = imageTool.getImage("C:\\Game-main\\src\\main\\SOURCE\\spaceship1.png");
    Image flight = flight_.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

    // 이미지 버퍼
    Image buffImg;
    Graphics buffG;

    // 플레이어 비행기의 위치값.
    int xpos = 100;
    int ypos = 100;

    public TestMiniView() {
        //프레임에 대한 설정
        setTitle("JFrmae 테스트");
        setSize(800,800);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 키 어댑터 ( 키 처리용 )
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
//                    case KeyEvent.VK_UP:
//                        ypos-=5;
//                        break;
//                    case KeyEvent.VK_DOWN:
//                        ypos+=5;
//                        break;
                    case KeyEvent.VK_LEFT:
                        xpos-=5;
                        break;
                    case KeyEvent.VK_RIGHT:
                        xpos+=5;
                        break;
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        buffImg = createImage(getWidth(),getHeight()); // 버퍼링용 이미지 ( 도화지 )
        buffG = buffImg.getGraphics(); // 버퍼링용 이미지에 그래픽 객체를 얻어야 그릴 수 있다고 한다. ( 붓? )
        update(g);
    }

    @Override
    public void update(Graphics g) {
        buffG.clearRect(0, 0, 854, 480); // 백지화
        buffG.drawImage(flight,xpos,ypos, this); // 유저 비행기 그리기.
        g.drawImage(buffImg,0,0,this); // 화면g애 버퍼(buffG)에 그려진 이미지(buffImg)옮김. ( 도화지에 이미지를 출력 )
        repaint();
    }
}
public class Mini3Main {
<<<<<<< HEAD
    public Mini3Main(GameUser user) {
        Player player = new Player(user);
        //TestMiniView testMiniView = new TestMiniView();
        GameView gameObject = new GameView(player, user);
        LocalDateTime beforeTime = LocalDateTime.now();
        LocalDateTime afterTime = LocalDateTime.now();
        Thread gameThread = new Thread(gameObject);

        gameThread.start();

        // 스레드가 종료될 때까지 대기

        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
=======
        public Mini3Main(GameUser user) {
            Player player = new Player(user);
            //TestMiniView testMiniView = new TestMiniView();
            GameView gameObject = new GameView(player, user);
            LocalDateTime beforeTime = LocalDateTime.now();
            LocalDateTime afterTime = LocalDateTime.now();
            Thread gameThread = new Thread(gameObject);

            gameThread.start();

            // 스레드가 종료될 때까지 대기

            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
>>>>>>> 852e1f8a2334240e78f52837c6795ad0884b8539


    public static void main(String[] args) {

        Member member = new Member(1,"j",0,0);
        GameUser user = new GameUser(member);
        new Mini3Main(user);
    }
}

