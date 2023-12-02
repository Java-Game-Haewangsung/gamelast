package client.main.view;

import client.main.GameRoom;
import client.main.GameUser;
import client.main.mainmap.Dice;
import client.main.member.Member;
import client.main.object.PlanetNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

//스레드
public class MainMapView extends JFrame implements Runnable {

    int frameWidth = 800; // JFrame 폭
    int frameHeight = 800; // JFrame 넓이
    int gridSize = 70; // 각 이미지의 크기
    int gridCount = 5;  // 격자의 행과 열 개수
    int gap = 70;       // 이미지 간격

    Member member; // 멤버 정보 받아오기
    GameUser user; // 플레이어 정보 받아오기
    Thread th; // KeyAdapter 쓰레드
    boolean checkExit; // JFrame 종료 여부

    // 이미지를 불러오는 역할 , 더블 버퍼.
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image buffImg;
    Graphics buffG;

    Image background_ = tk.getImage("SOURCE/bg0.png"); // 배경화면;
    Image planetImages[] = new Image[16];
    int scaledWidth = 800;
    int scaledHeight = 800;
    Image background = background_.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

    ArrayList<PlanetNode> nodes = new ArrayList<>();
    // 각 노드별 코인 정보 저장 배열
    int[] coinInfo = {3, -3, 3, 3, -3, 3, 3, 3, -3, 3, 3, 3, -3, 3, -3, 3};

    Dice dice = new Dice();

    GameRoom room;
    ArrayList<GameUser> users = new ArrayList<>();
    GameUser turnPlayer; //현재 자신의 차례인 플레이어
    HashMap<Integer, Integer> turnInfo = new HashMap<>(); // key : 현재 턴 값 value : 해당 턴 주사위 던진 플레이어 수

    public MainMapView(ArrayList<GameUser> users, GameRoom room) {
        this.users = users;
        this.room = room;
        this.checkExit = false;
        turnPlayer = room.getGameOwner(); // 게임 시작시 방장부터 시작
        turnInfo.put(0, 0);

        //플레이어 키 입력 스레드
//        KeyControl key = new KeyControl(turnPlayer, this);
//        th = new Thread(key);
//        th.start();

        //플레이어 마우스 입력 스레드
        MouseControl mouse = new MouseControl(turnPlayer, this);
        th = new Thread(mouse);
        th.start();

        // 테두리 이미지 배치
        int totalSize = gridSize * gridCount + gap * (gridCount - 1);
        int startX = (800 - totalSize) / 2;
        int startY = (800 - totalSize) / 2;
        int distY = 0;

        // 행성 노드 생성 및 추가
        for (int i = 0; i < 16; i++) {
            String path = "SOURCE/planet" + ((i % 9) + 1)+ ".png";
            Image img = tk.getImage(path);
            planetImages[i] = resizeImage(img, 70, 70);
        }

        for (int row = 0; row < gridCount; row++) {
            for (int col = 0; col < gridCount; col++) {
                int index = row * gridCount + col;
                if (index < 5) {
                    int x = startX + col * (gridSize + gap);
                    int y = startY + row * (gridSize + gap);
                    //g.drawImage(planetImages[index], x, y, gridSize, gridSize, null);
                    nodes.add(new PlanetNode(index + 1, x, y, planetImages[index], coinInfo[index]));
                }
            }
        }
        // index 5 ~ 10
        int index = 5;
        for (int row = 1; row < 4; row++) {
            for (int col = 0; col < 2; col++) {
                if (index % 2 == 1) {
                    int x = startX;
                    int y = startY + row * (gridSize + gap);
                    //g.drawImage(planetImages[index], x, y, gridSize, gridSize, null);
                    nodes.add(new PlanetNode(index + 1, x, y, planetImages[index], coinInfo[index]));
                }
                else {
                    int x = startX + 4 * (gridSize + gap);
                    int y = startY + row * (gridSize + gap);
                    //g.drawImage(planetImages[index], x, y, gridSize, gridSize, null);
                    nodes.add(new PlanetNode(index + 1, x, y, planetImages[index], coinInfo[index]));
                }
                index++;
            }
        }

        int dist = 0;
        for (int col = 11; col < 16; col++) {
            int x = startX + dist++ * (gridSize + gap);
            int y = startY + 4 * (gridSize + gap);
            //g.drawImage(planetImages[col], x, y, gridSize, gridSize, null);
            nodes.add(new PlanetNode(col + 1, x, y, planetImages[0 + (col - 10)], coinInfo[index]));
        }

        // Frame 설정
//        addKeyListener(key);
        addMouseListener(mouse);
        setTitle("mini game: meteor shooter");
        // 창을 닫을 때 프로그램 종료 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 이미지 크기 조절 메서드
    private Image resizeImage(Image originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void paint(Graphics g) {
        super.paint(g);
        buffImg = createImage(getWidth(), getHeight());
        buffG = buffImg.getGraphics();
        update(buffG);
        g.drawImage(buffImg, 0, 0, this);
        if(turnInfo.size() >= 17 && turnInfo.get(16) >= 4) {
            return;
        }
    }

    public void update(Graphics g) {
        drawBackground(g);
        drawNodes(g);
        drawGameUsers(g);
    }

    private void drawGameUsers(Graphics g) {
        for (int i = 0; i < users.size(); i++) {
            GameUser u = users.get(i);
            buffG.drawImage(u.getImg(), u.getPosX(), u.getPosY(), this);
        }
    }

    private void drawNodes(Graphics g) {
        for(int i = 0; i < nodes.size(); i++) {
            PlanetNode node = nodes.get(i);
            buffG.drawImage(node.getImg(), node.getPosX(), node.getPosY(), this);
        }
    }

    private void drawBackground(Graphics g) {
        buffG.clearRect(0,0, frameWidth, frameHeight);
        buffG.drawImage(background, 0, 0, this);
    }


    @Override
    public void run() {
        // 게임 진행시 main 스레드를 join으로 묶어둔다.
        while(true) {
            if(this.checkExit == true)
                break;
            else {
                System.out.println("");

                try {
                    Thread.sleep(10); // 적절한 sleep 시간을 설정
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
