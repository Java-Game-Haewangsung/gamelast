package client.main.view;

import client.main.GameRoom;
import client.main.GameUser;
import client.main.minigame.mini1.MiniGame1;
import client.main.minigame.mini2.MiniGame2;
import client.main.object.Dice;
import client.main.object.PlanetNode;
import client.main.object.Store;
import client.main.object.Sun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainMapView extends JPanel implements Runnable {

    private int currentIndex; // 노드 이동 갱신용
    private int checkTurn = 1;
    private boolean isMiniGameExecuted = false;
    private boolean miniAvailable = false;

    int frameWidth = 800; // Panel 폭
    int frameHeight = 800; // Panel 넓이
    int gridSize = 70; // 각 이미지의 크기
    int gridCount = 5;  // 격자의 행과 열 개수
    int gap = 50;       // 이미지 간격

    Dice dice; // 주사위 객체
    Sun sun; // 태양 객체
    Store store; // 상점 객체
    Thread th; // KeyAdapter 쓰레드
    boolean checkExit; // JFrame 종료 여부

    // 이미지를 불러오는 역할 , 더블 버퍼.
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image buffImg;
    Graphics buffG;

    Image background_ = tk.getImage("SOURCE/bg0.png"); // 배경화면
    Image planetImages[] = new Image[16];
    String playerImgPath = "SOURCE/Players/playerImg"; // 플레이어 이미지들 경로
    Image playerImages[] = new Image[3];
    int scaledWidth = 800;
    int scaledHeight = 800;
    Image background = background_.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

    ArrayList<PlanetNode> nodes = new ArrayList<>();

    // 각 노드별 코인 정보 저장 배열
    int[] coinInfo = {3, 3, 3, 3, -3, -3, 3, -3, -3, 0, 3, 3, 3, -3, -3, 3};

    GameRoom room;
    ArrayList<GameUser> users;

    GameUser turnPlayer; //현재 자신의 차례인 플레이어
    HashMap<Integer, Integer> turnInfo = new HashMap<>(); // key : 현재 턴 값 value : 해당 턴 주사위 던진 플레이어 수

    /**
     * 플레이어 노드 처리 메서드들 (시작)
     */
    // 주사위 결과에 따라 이동할 타겟 노드 계산
    private PlanetNode calculateTargetNode(GameUser player, int diceResult) {
        int targetNodeId = (player.getCurrentNode().getId() + diceResult) % 17;
        return findNodeById(targetNodeId);
    }

    // 노드 ID로 노드를 찾는 메서드
    private PlanetNode findNodeById(int nodeId) {
        for (PlanetNode node : nodes) {
            if (node.getId() == nodeId) {
                return node;
            }
        }
        return null;
    }

    // (타겟 노드가 아닌 동안) 중간 노드 밟고 이동 -> repaint 메서드 (이동 모션)
    public void moveNoneTargetNodes(GameUser player, PlanetNode targetNode) {
        List<PlanetNode> nonTargetNodes = getNonTargetNodes(player.getCurrentNode(), targetNode);
        currentIndex = 0;

        Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.moveToNode(nonTargetNodes.get(currentIndex));
                repaint();
                // 태양 존재 검사
                if (nonTargetNodes.get(currentIndex).isSun() == true)
                    // 노드 위 플레이어가 태양 구매
                    if (sun.buySun(player) == 1) {
                        nonTargetNodes.get(currentIndex).setSun(false); // 노드 위 태양 제거
                        createSun(); // 다른 노드에 태양 생성
                    }

                if (currentIndex == nonTargetNodes.size() - 1) {
                    ((Timer) e.getSource()).stop();
                    player.moveToNode(targetNode);

                    // 도착 노드가 상점이면 상점 실행
                    if (targetNode == nodes.get(9))
                        store = new Store(player);
                    // 노드에 따른 플레이어 코인 처리
                    player.addCoin(targetNode.getCoin());
                    repaint();
                } else {
                    currentIndex++;
                }
            }
        });

        timer.start();
    }

    // 플레이어가 밟아갈 중간 노드들을 가져오는 메서드
    private List<PlanetNode> getNonTargetNodes(PlanetNode startNode, PlanetNode targetNode) {
        List<PlanetNode> nonTargetNodes = new ArrayList<>();

        // 시작 노드부터 타겟 노드까지의 모든 중간 노드를 가져옴
        int startNodeId = startNode.getId();
        int targetNodeId = targetNode.getId();

        // 시계 방향으로 노드를 가져오는 경우
        if (startNodeId < targetNodeId) {
            for (int nodeId = startNodeId + 1; nodeId <= targetNodeId; nodeId++) {
                nonTargetNodes.add(findNodeById(nodeId));
            }
        } else {
            // 반시계 방향으로 노드를 가져오는 경우(ex.16번 -> 1번 노드 이동 시)
            for (int nodeId = startNodeId + 1; nodeId <= 16; nodeId++) {
                nonTargetNodes.add(findNodeById(nodeId));
            }
            for (int nodeId = 1; nodeId <= targetNodeId; nodeId++) {
                nonTargetNodes.add(findNodeById(nodeId));
            }
        }

        return nonTargetNodes;
    }

    // 주사위 결과에 따라 플레이어 이동 처리(최종적으로 처리하는 부분)
    public void handleMoveToNode(GameUser player) {
        int diceResult = dice.getDiceResult();
        PlanetNode targetNode = calculateTargetNode(player, diceResult);
        // 아이템 사용 여부 체크
        if (player.isUseItem()) {
            if (player.useItem(targetNode) != 1)
                targetNode = calculateTargetNode(player, diceResult + 3);
            player.endUseItem();
        }
        moveNoneTargetNodes(player, targetNode);
    }

    /**
     * 플레이어 노드 처리 메서드들 (끝)
     */

    // 랜덤 노드에 태양 생성
    public void createSun() {
        // nodes 리스트(노드들 정보) 비어있으면 아무 작업도 수행하지 않음
        if (nodes == null || nodes.isEmpty()) {
            System.out.println("태양 생성 불가");
            return;
        }

        // Random 객체 생성
        Random random = new Random(System.currentTimeMillis());

        // nodes 리스트에서 랜덤한 인덱스 선택
        int randomIndex = random.nextInt(nodes.size());

        // 선택된 노드로 Sun 생성
        PlanetNode randomNode = nodes.get(randomIndex);
//        randomNode = nodes.get(5); // 테스트용
        sun = new Sun(randomNode);

        sun.setImg(resizeImage(sun.getImg(), 32, 32));
        System.out.println("태양 생성 노드: " + randomNode.getId());
        repaint();
    }

    /**
     * 생성자 함수
     */
    public MainMapView(ArrayList<GameUser> users, GameRoom room) {
        setSize(frameWidth, frameHeight);
        setVisible(true);

        this.users = users;

        this.room = room;
        this.checkExit = false;
//        turnPlayer = room.getGameOwner(); // 게임 시작시 방장부터 시작
        // (테스트) 임의의 현재 플레이어: users의 첫 번째 플레이어
        turnPlayer = users.get(0);
        turnInfo.put(0, 0);

        //플레이어 마우스 입력 스레드
        MouseControl mouse = new MouseControl(turnPlayer, this);
        th = new Thread(mouse);
        th.start();

        // 테두리 이미지 배치
        int totalSize = gridSize * gridCount + gap * (gridCount - 1);

        // 행성 노드 생성 및 추가
        for (int i = 0; i < 16; i++) {
            String path = "SOURCE/Planets/planet" + ((i % 9) + 1) + ".png";
            Image img = tk.getImage(path);
            planetImages[i] = resizeImage(img, 70, 70);
        }

        // 절대 좌표에 노드들 생성
        nodes.add(new PlanetNode(1, 125, 125, planetImages[0], coinInfo[0]));
        nodes.add(new PlanetNode(2, 245, 125, planetImages[1], coinInfo[1]));
        nodes.add(new PlanetNode(3, 365, 125, planetImages[2], coinInfo[2]));
        nodes.add(new PlanetNode(4, 485, 125, planetImages[3], coinInfo[3]));
        nodes.add(new PlanetNode(5, 605, 125, planetImages[4], coinInfo[4]));
        nodes.add(new PlanetNode(6, 605, 245, planetImages[5], coinInfo[5]));
        nodes.add(new PlanetNode(7, 605, 365, planetImages[6], coinInfo[6]));
        nodes.add(new PlanetNode(8, 605, 485, planetImages[7], coinInfo[7]));
        nodes.add(new PlanetNode(9, 605, 605, planetImages[8], coinInfo[8]));
        nodes.add(new PlanetNode(10, 485, 605, planetImages[9], coinInfo[9]));
        nodes.add(new PlanetNode(11, 365, 605, planetImages[10], coinInfo[10]));
        nodes.add(new PlanetNode(12, 245, 605, planetImages[11], coinInfo[11]));
        nodes.add(new PlanetNode(13, 125, 605, planetImages[12], coinInfo[12]));
        nodes.add(new PlanetNode(14, 125, 485, planetImages[13], coinInfo[13]));
        nodes.add(new PlanetNode(15, 125, 365, planetImages[14], coinInfo[14]));
        nodes.add(new PlanetNode(16, 125, 245, planetImages[15], coinInfo[15]));

        // 플레이어별 이미지 설정 및 현재 노드 초기화 (플레이어 수: 3)
        for (int i = 0; i < 3; i++) {
            GameUser u = users.get(i);
            PlanetNode node = nodes.get(0);
            // 노드 이미지 처리
            String imagePath = playerImgPath + (i + 1) + ".png";
            Image image = tk.getImage(imagePath);
            playerImages[i] = resizeImage(image, 64, 64);
            u.setImg(playerImages[i]);
            u.setCurrentNode(node); // 현재 노드 첫 번째 노드로 초기화
        }

        // 랜덤 위치 노드에 태양 생성
        createSun();

        /**
         * 주사위 굴리기
         */
        // 주사위 패널 생성 및 설정
        dice = new Dice();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 마우스 클릭 시 주사위 굴림
                dice.startRolling();
                dice.rollDice();

                // 1초 후 주사위 멈춤 (1000ms = 1초)
                Timer stopTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        dice.stopRolling();

                        // 주사위 멈춘 후 주사위 결과에 따라 플레이어 이동 처리
                        handleMoveToNode(turnPlayer);

                        // 턴 수 +1
                        checkTurn++;

                        if ((checkTurn % 4 == 0)) {
                            miniAvailable = true;
                        }
                    }

                });
                stopTimer.setRepeats(false); // 타이머 한 번만 실행
                stopTimer.start();
            }
        });
        dice.setBounds(370, 370, 64, 64); // 주사위 위치 설정
        add(dice);


        // 테스트(테스트2,3) 임의로 코인 설정
        turnPlayer.addCoin(12);
//        users.get(1).addCoin(3);
//        users.get(1).addSun();
//        users.get(1).setCurrentPosition(365, 125);
//        users.get(2).addCoin(6);
//        users.get(2).setCurrentPosition(125, 605);

    }

    // 이미지 크기 조절 메서드
    private Image resizeImage(Image originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        buffImg = createImage(getWidth(), getHeight());
        buffG = buffImg.getGraphics();
        update(buffG);
        g.drawImage(buffImg, 0, 0, this);
        if (turnInfo.size() >= 17 && turnInfo.get(16) >= 4) {
            return;
        }

        // 주사위 굴리기 전에 아이템 확인
        if (turnPlayer.getUserItems().size() > 0 && turnPlayer.getItemSize() > 0) {
            turnPlayer.decreaseItemSize(); // 아이템 수 감소시킴(이후 던질 때 아이템 창 안 뜨게)
            turnPlayer.showUserItems(); // 아이템 정보 보여주고 사용할 건지 체크
        }

        repaint();
        g.drawImage(dice.getImage(), 370, 370, 64, 64, this); // 주사위 그림
        g.drawImage(sun.getImg(), sun.getPosX(), sun.getPosY(), this); // 태양 그림

    }

    // 요소들 그림
    public void update(Graphics g) {
        drawBackground(g);
        drawNodes(g);
        drawGameUsers(g);
        // 각 모서리에 120x120 크기의 점수판 그리기
        int boardSize = 100;
        int padding = 6;
        drawScoreBoard(g, users.get(0), 0, 0, boardSize, boardSize);
        drawScoreBoard(g, users.get(1), getWidth() - boardSize - padding, 0, boardSize, boardSize);
        drawScoreBoard(g, users.get(2), 0, getHeight() - boardSize - padding, boardSize, boardSize);
    }

    // 유저 이미지 그리기
    private void drawGameUsers(Graphics g) {
        for (int i = 0; i < users.size(); i++) {
            GameUser u = users.get(i);
            buffG.drawImage(u.getImg(), u.getPosX(), u.getPosY(), this);
        }
    }

    // 행성 노드들 그리기
    private void drawNodes(Graphics g) {
        for (int i = 0; i < nodes.size(); i++) {
            PlanetNode node = nodes.get(i);
            buffG.drawImage(node.getImg(), node.getPosX(), node.getPosY(), this);
        }
    }

    // 배경 이미지 그리기
    private void drawBackground(Graphics g) {
        buffG.clearRect(0, 0, frameWidth, frameHeight);
        buffG.drawImage(background, 0, 0, this);
    }

    // 점수판 그리기
    private void drawScoreBoard(Graphics g, GameUser user, int x, int y, int width, int height) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        // 유저 정보 표시
        String nickname = user.getNickName();
        int coin = user.getCoin();
        int sun = user.getSun();
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 15);
        g.setFont(f);
        g.drawString(nickname, x + 10, y + 30);
        g.drawString("코인: " + coin, x + 10, y + 60);
        g.drawString("태양: " + sun, x + 10, y + 90);
    }

    public int getCheckTurn() {
        return checkTurn;
    }

    @Override
    public void run() {
        // 게임 진행시 main 스레드를 join으로 묶어둔다.
        while (true) {
            if (this.checkExit == true)
                break;
            else {
                System.out.println("");

                try {
                    Thread.sleep(10); // 적절한 sleep 시간을 설정
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // checkTurn이 4의 배수인 경우 미니게임 스레드 실행
                if (miniAvailable) {
                    // 미니게임 랜덤 실행
                    Random miniRan = new Random(System.currentTimeMillis());
                    int ranNum = miniRan.nextInt(2);
                    Thread miniGameThread = null;

                    if (ranNum == 0) {
                        miniGameThread = new Thread(new MiniGame1(users.get(0))); // 미니게임1 실행
                    } else if (ranNum == 1) {
                        miniGameThread = new Thread(new MiniGame2(users.get(0))); // 미니게임2 실행
                    } else if (ranNum == 2) {
//            miniGameThread = new Thread((Runnable) new Mini3Main(users.get(0))); // 미니게임3 실행
                    }

                    // 메인 스레드는 미니게임 스레드의 종료를 기다림
                    try {
                        miniAvailable = false;
                        miniGameThread.join();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}