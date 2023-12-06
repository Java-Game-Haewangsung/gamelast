package client.main.minigame.mini3;

import client.main.GameUser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

//JFrame이며, 게임의 출력을 담당
public class GameView extends JFrame implements Runnable{

    static boolean Crash(int x1, int y1, int x2, int y2, int w1, int h1, int w2, int h2){
        // x,y : 위치값 , w,h : 이미지의 높이와 길이.
        boolean result = false;
        if(Math.abs( ( x1 + w1 / 2 )  - ( x2 + w2 / 2 ))  <  ( w2 / 2 + w1 / 2 )
                && Math.abs( ( y1 + h1 / 2 )  - ( y2 + h2 / 2 ))  <  ( h2 / 2 + h1/ 2 ))
            result = true;
        else result = false;
        return result;
    }

    int frameWidth = 800; // JFrame 폭.
    int frameHeight = 800; // JFrame 넓이.
    Player player; // 플레이어 정보 받아오기.
    GameUser user; // 유저 정보 받아오기.
    int Stage; // 스테이지.
    Thread th; // KeyAdapter 쓰레드.
    boolean checkExit; // JFrame 종료 여부.
    //	Music backGroundMusic; // 게임 배경음악.
    boolean checkSkill = true; // 스킬 쿨타임을 재는 변수.
    long leftTime = 0;
    long currentTime = 0;

    // 이미지를 불러오는 역할 , 더블 버퍼.
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image buffImg;
    Graphics buffG;

    Image background_ = tk.getImage("SOURCE/space1.png"); // 배경화면;
    int scaledWidth = 800;
    int scaledHeight = 800;
    Image background = background_.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    Image background2 = background_.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

    ArrayList<Missile> Missiles = new ArrayList<Missile>();
    ArrayList<Meteors> Meteors = new ArrayList<Meteors>();
    ArrayList<Bomb> Bombs = new ArrayList<Bomb>();

    Timer timer;
    long startTime;
    private int count;

    public GameView(Player p, GameUser u) {
        this.player = p;
        this.user = u;
        this.checkExit = false;
        // 게임 시작 시간 기록
        startTime = System.currentTimeMillis();
        count = 0;
        // 타이머 초기화
        timer = new Timer(1000 , e -> {
            count++;
        });
        timer.start();


        //플레이어 키 입력 스레드
        KeyControl key = new KeyControl(player, this);
        th = new Thread(key);
        th.start();

        // 랜덤 객체 생성
        Random random = new Random();

        //운석 객체 생성
        for (int i = 0; i < 7; i++) {
            // 1에서 799 사이의 랜덤 정수 생성
            int randomX = random.nextInt(799) + 1;
            Meteors.add(new Meteors(randomX, 1));
        }

        //폭탄 객체 생성
        for (int i = 0; i < 3; i++) {
            // 1에서 799 사이의 랜덤 정수 생성
            int randomX = random.nextInt(799) + 1;
            Bombs.add(new Bomb(randomX, 1));
        }


        //Frmae설정
        addKeyListener(key);
        setTitle("mini game: meteor shooter");
        setResizable(false);
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void paint(Graphics g) {
        repaint();
        buffImg = createImage(getWidth(), getHeight());
        buffG = buffImg.getGraphics();
        update(g);
        if(this.player.hp <= 0) {
            return;
        }
    }




    public void update(Graphics g) {
        drawBackground(g);
        drawPlayer(g);
        drawStatus(g);
        drawMissile(g);
        drawMeteors(g);
        drawBombs(g);
        //drawExplosions(g);
        currentTime = System.currentTimeMillis()/1000;
        if(currentTime - KeyControl.skillTime >= 4) {
            this.checkSkill = true;
        }
        g.drawImage(buffImg,0,0,this);
    }

    int count1 = 0;
    int count2 = -scaledHeight;
    int backgroundSpeed = 1; // 배경 이미지 이동 속도
    //배경 그리기
    public void drawBackground(Graphics g) {
        buffG.clearRect(0,0, frameWidth, frameHeight);
        // 두 개의 배경 이미지를 연속적으로 그립니다.
        buffG.drawImage(background, 0, count1, this);
        buffG.drawImage(background, 0, count2, this);

        count1 += backgroundSpeed;
        count2 += backgroundSpeed;

        // 첫 번째 이미지가 화면을 벗어나면 위치를 조정합니다.
        if (count1 >= scaledHeight) {
            count1 = 0;
        }

        // 두 번째 이미지가 화면을 벗어나면 위치를 조정합니다.
        if (count2 >= 0) {
            count2 = -scaledHeight;
        }

    }

    public void drawStatus(Graphics g) {
        buffG.setColor(Color.WHITE); // 흰색으로 설정
        buffG.drawString("Player HP : "+this.player.hp, 700, 50);
    }

    //플레이어 비행기에 대한 출력
    public void drawPlayer(Graphics g) {
        buffG.drawImage(this.player.img,this.player.posX,this.player.posY, this);

        // 플레이어의 피가0이 되면 모두 종료.
        if(this.player.hp <= 0) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {e.printStackTrace();}

            //리스트 초기화
            Missiles = new ArrayList<Missile>();
            Meteors = new ArrayList<Meteors>();
            Bombs = new ArrayList<Bomb>();

            // Frame 없애기.
            this.checkExit = true;
            // 걸린 시간이 5초 이상이면 코인 4개 지급
            if(this.checkExit==true) {
                timer.stop();
                user.setMiniGameScore(count);
                if(count>5){
                    user.setCoin(user.getCoin()+4);
                    System.out.println("5초 이상 버텼습니다.");
                }
                System.out.println("코인: " + user.getCoin());
            }
            this.dispose();
            return;
        }
    }

    //미사일 출력
    public void drawMissile(Graphics g) {
        for(int i = 0; i < Missiles.size(); i++) {
            Missile missile = Missiles.get(i);
            buffG.drawImage(missile.img, missile.posX, missile.posY+50, this );
            missile.move();
            if(missile.posY < 0) { //만약 미사일이 화면에서 사라지면 삭제
                Missiles.remove(i);
            }
        }
    }

    public void drawMeteors(Graphics g) {
        for(int i = 0; i < Meteors.size(); i++) {
            Meteors meteor = Meteors.get(i);
            buffG.drawImage(meteor.img, meteor.posX, meteor.posY+50, this);
            meteor.move();
            if( meteor.posY == 690) {
                this.player.hp -= 1;
            }
        }

    }

    public void drawBombs(Graphics g) {
        for(int i = 0; i < Bombs.size(); i++) {
            Bomb bomb = Bombs.get(i);
            buffG.drawImage(bomb.img, bomb.posX, bomb.posY+50, this);
            bomb.move();
        }
    }

    @Override
    public void run() {
        // 게임 진행시 main 스레드를 join으로 묶어둔다.
        while (true) {
            if (this.checkExit) {
                break;
            } else {
                System.out.println("");
                for (Meteors meteor : Meteors) {
                    meteor.move();
                }

                try {
                    Thread.sleep(10); // 적절한 sleep 시간을 설정해 주세요.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 게임 종료 조건
                if (this.player.hp <= 0) {
                    // 게임 종료 시 필요한 정리 작업을 수행
                    this.checkExit = true;

                    // 리스트 초기화
                    Missiles = new ArrayList<>();
                    Meteors = new ArrayList<>();
                    Bombs = new ArrayList<>();

                    // 프레임 닫기
                    this.dispose();
                    break; // 루프를 빠져나가서 스레드 종료
                }
            }
        }
    }
}
