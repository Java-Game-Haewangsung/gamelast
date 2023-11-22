package client.mini1;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class miniGame extends JFrame {


    //전역변수 필드

    public long score = 100;
    imgLabel selected;
    boolean status = false;
    boolean selecting = false;
    JLabel timerLabel;
    long count = 0;

    private ArrayList<imgLabel> labels = new ArrayList<>();


    // 태양, 달 이미지 설정
    String sun1 = "assets/minigame1/sun1.png";
    String sun2 = "assets/minigame1/sun2.png";
    String sun3 = "assets/minigame1/sun3.png";
    String black1 = "assets/minigame1/black1.png";
    String black2 = "assets/minigame1/black2.png";
    String player = "assets/minigame1/player.png";


    //이미지 설정

    imgLabel background = new imgLabel("assets/minigame1/background.png","background");
    imgLabel l1 = new imgLabel(sun1,"sun");
    imgLabel l2 = new imgLabel(black1,"black");
    imgLabel l3 = new imgLabel(sun3,"sun");
    imgLabel l4 = new imgLabel(sun2,"sun");
    imgLabel l5 = new imgLabel(black2,"black");
    imgLabel l6 = new imgLabel(black1,"black");
    imgLabel l7 = new imgLabel(sun1,"sun");

    JLayeredPane mainLayer = new JLayeredPane();



    class imgLabel extends JLabel {
        BufferedImage img;

        String filename;
        String flag;

        public imgLabel (String filename,String flag) {
            try{
                img = ImageIO.read(new File(filename));

            } catch (IOException e){
                e.printStackTrace();
            }
            this.filename = filename;
            this.flag = flag;

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img,0,0,getWidth(),getHeight(),null);

        }


        // 이미지 랜덤설정
        public void setRandomImg() {
            try {
                String[] imageOptions = {"sun1.png", "sun2.png", "sun3.png", "black1.png", "black2.png","sun3.png","black3.png","black4.png","black.png","sun4.png"};
                Random random = new Random(System.currentTimeMillis());
                int randomIndex = random.nextInt(imageOptions.length);
                String imagePath = "assets/minigame1/" + imageOptions[randomIndex];
                img = ImageIO.read(new File(imagePath));
                if(randomIndex>3)
                    this.flag = "black";
                else
                    this.flag = "sun";
                repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainLayer.repaint();
        }

        public String getImgLabel() {
            return this.filename;
        }
    }

    // 맨앞의 이미지 제거, 뒤에 있는 이미지를 앞으로 이동
    public void setAllImages(imgLabel selected){
        labels.remove(selected);
        labels.add(selected);
        for(int i = 0; i<labels.size();i++) {
            selected.setRandomImg();
            imgLabel label = labels.get(i);
            mainLayer.remove(label);
            mainLayer.add(label);
            mainLayer.setLayer(label,7-i);
            label.setLocation(280,50+(6-i)*50);
            System.out.println(label.flag);
            System.out.println(score);
        }
        mainLayer.repaint();
    }

    public miniGame() {

        timerLabel = new JLabel("Time: " + count);
        Timer timer = null;
        Timer finalTimer = timer;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count--;
                timerLabel.setText("Time: " + count);
                if (count <= 0) {
                    System.out.println("시간 종료");
                    System.out.println(score);
                    status = false;
                    finalTimer.stop();
                }
            }
        });


        mainLayer.setBounds(0, 0, 800, 800);

        //메인 섹션

        background.setBounds(0,0,800,800);
        l1.setBounds(280,50, 200, 200);
        l2.setBounds(280, 100, 200, 200);
        l3.setBounds(280, 150, 200, 200);
        l4.setBounds(280, 200, 200, 200);
        l5.setBounds(280, 250, 200, 200);
        l6.setBounds(280, 300, 200, 200);
        l7.setBounds(280,350,200,200);


        // 위치 설정
        mainLayer.add(background,8);
        mainLayer.add(l1);
        mainLayer.setLayer(l1,1);
        mainLayer.add(l2);
        mainLayer.setLayer(l2,2);
        mainLayer.add(l3);
        mainLayer.setLayer(l3,3);
        mainLayer.add(l4);
        mainLayer.setLayer(l4,4);
        mainLayer.add(l5);
        mainLayer.setLayer(l5,5);
        mainLayer.add(l6);
        mainLayer.setLayer(l6,6);
        mainLayer.add(l7);
        mainLayer.setLayer(l7,7);



        // 사용자 캐릭터
        imgLabel playerImg = new imgLabel(player,"player");
        mainLayer.add(playerImg);
        mainLayer.setLayer(playerImg,1);
        playerImg.setBounds(300,550,150,150);

        //사이드 섹션 - 절대좌표
        imgLabel sunSec = new imgLabel(sun1,"sun");
        imgLabel blackSec = new imgLabel(black2,"black");
        JLabel scoreSec = new JLabel("Score: " + String.valueOf(score));
        scoreSec.setFont(new Font("TimesRomanBold",Font.BOLD,20));
        scoreSec.setForeground(Color.WHITE);
        mainLayer.add(sunSec);
        mainLayer.setLayer(sunSec,1);
        sunSec.setBounds(20,550,150,150);
        mainLayer.add(blackSec);
        mainLayer.setLayer(blackSec,1);
        mainLayer.add(scoreSec);
        timerLabel.setText("Time: " + count);
        mainLayer.add(timerLabel);
        mainLayer.setLayer(timerLabel,1);
        mainLayer.setLayer(scoreSec,1);
        timerLabel.setFont(new Font("TimesRomanBold",Font.BOLD,20));
        timerLabel.setForeground(Color.WHITE);
        blackSec.setBounds(580,550,150,150);
        timerLabel.setBounds(580,50,200,100);
        scoreSec.setBounds(50,50,200,100);



        add(mainLayer,BorderLayout.NORTH);

        setLayout(new BorderLayout(10,10));
        setSize(800,800);
        setTitle("태양과 행성 구분하기");
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        // 백터 초기화
        labels.add(l7);
        labels.add(l6);
        labels.add(l5);
        labels.add(l4);
        labels.add(l3);
        labels.add(l2);
        labels.add(l1);




        init();
        scoreSec.setText("Score: " + String.valueOf(score));
        if(status==true && score==0 && count ==100) {
            timer.start();
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (status) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        imgLabel label = labels.get(0);
                        selected = label;
                        if (selected.flag.equals("black") && e.getKeyCode() == KeyEvent.VK_LEFT) {
                            setAllImages(selected);
                            if (score > 0) score--;
                        } else if (selected.flag.equals("sun") && e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            setAllImages(selected);
                            if (score > 0) score--;
                        }
                        else if(selected.flag.equals("black") && e.getKeyCode() == KeyEvent.VK_RIGHT){
                            score++;
                            setAllImages(selected);
                        }
                        else if(selected.flag.equals("sun") && e.getKeyCode() == KeyEvent.VK_LEFT){
                            score++;
                            setAllImages(selected);
                        }
                        if(score>100)
                            scoreSec.setText("Score: " + String.valueOf(score));
                        else
                            scoreSec.setText("Score: " + String.valueOf(score));
                    }
                }
            }
        });


    }

    public static void main(String[] args) {
        new miniGame();
    }


    public void init() {
        // 점수 0으로
        // 타이머 0으로
        score = 0;
        status = true;
        count = 100;

    }








}
