package client.main;

import client.main.member.Member;
import client.main.object.Item;
import client.main.object.PlanetNode;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.util.ArrayList;

public class GameUser {
    private int id;
    private Member member;
    private String nickName;
    private Socket sock;
    private GameRoom room;
    private int enteredCode;
    Image img;
    Toolkit tk = Toolkit.getDefaultToolkit();
    private int posX, posY;
    private int initialX = 125;
    private int initialY = 125;
    private int width, height = 64;
    private int coin = 0; // 보유 코인 수
    private int sun = 0; // 보유 태양 수

    private PlanetNode currentNode; // 현재 노드 위치
    private int miniGameScore; // 미니게임 점수
    private ArrayList<Item> userItems = null; // 보유 아이템
    private boolean isUseItem = false; // 아이템 사용할 턴인지
    private int useItemId = 0;
    private int itemSize = 0; // 아이템 사용 턴에 아이템창 한 번만 뜨도록 처리

    public GameUser(Member member) {
        this.member = member;
        this.nickName = member.getNickName();
        this.userItems = new ArrayList<>();
        posX = initialX;
        posY = initialY;
    }

    /**
     * 방에 입장시킴
     *
     * @param room 입장할 방
     */
    public void enterRoom(GameRoom room) {
        this.room = room;
    }

    /**
     * 방에서 퇴장
     */
    public void exitRoom() {
        this.room = null;
    }

    public GameRoom getRoom() {
        return room;
    }

//    // 노드 이동 멤버 함수
//    public void moveNode(PlanetNode targetNode) {
//        posX = targetNode.getX();
//        posY = targetNode.getY();
//    }

    /**
     * 노드 이동 처리
     */
    // get 현재 노드 위치
    public PlanetNode getCurrentNode() {
        return currentNode;
    }

    // set 현재 노드 위치
    public void setCurrentNode(PlanetNode node) {
        this.currentNode = node;
    }

    // set 캐릭터 현재 좌표
    public void setCurrentPosition(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    // 노드 이동: 타겟 노드로 직접(한번에) 이동
    public void moveToNode(PlanetNode targetNode) {
        setCurrentPosition(targetNode.getPosX(), targetNode.getPosY());
        setCurrentNode(targetNode);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getSock() {
        return sock;
    }

    public void setSock(Socket sock) {
        this.sock = sock;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public int getEnteredCode() {
        return enteredCode;
    }

    public void setEnteredCode(int enteredCode) {
        this.enteredCode = enteredCode;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    } // 코인 설정

    // 코인 변경
    public void addCoin(int coin) {
        this.coin += coin;
        if (this.coin == 0)
            this.coin = 0;
    }

    public int getSun() {
        return sun;
    }

    public void addSun() {
        this.sun += 1;
    } // 태양 구매 시 업데이트

    public boolean isUseItem() {
        return isUseItem;
    }

    // 아이템 사용 완료 처리
    public void endUseItem() {
        Item itemToRemove = findItemById(useItemId);

        if (itemToRemove != null) {
            userItems.remove(itemToRemove);
        }

        isUseItem = false;
        useItemId = 0;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    // 미니게임 점수 초기화
    public void initMiniGameScore() { miniGameScore = 0; }

    // get 미니게임 점수
    public int getMiniGameScore() {
        return miniGameScore;
    }

    // set 미니게임 점수
    public void setMiniGameScore(int miniGameScore) {
        this.miniGameScore = miniGameScore;
    }

    public void addUserItem(Item item) {
        itemSize++;
        this.userItems.add(item);
    }

    // 아이템 사용 턴에 아이템창 한 번만 뜨도록 처리
    public int getItemSize() { return itemSize; }
    public void decreaseItemSize() {
        itemSize--;
    }

    public ArrayList<Item> getUserItems() {
        return userItems;
    }

    // 보유 아이템 사용
    public void useItem(Item selectedItem) {
        if (userItems.contains(selectedItem)) {
            if (selectedItem.getItemId() == 2)

                // 아이템 사용 로직 추가
                System.out.println("아이템을 사용했습니다: " + selectedItem.getItemName());

            // 사용 후 아이템 제거
            userItems.remove(selectedItem);
        } else {
            System.out.println("보유한 아이템이 아닙니다.");
        }
    }

    // 보유 아이템 사용
    public int useItem(PlanetNode planetNode) {
        if (useItemId == 1) {
            if (planetNode.getCoin() == 3) {
                // 파란 노드 위면 3코인 추가 지급(두 배 효과)
                coin += 3;
                return 1; // 1번(모 아니면 도) 아이템 사용 성공
            }
        } else if (useItemId == 2) {
            return 2; // 2번(부스터) 아이템 사용 성공
        }

        // 사용 실패
        return 0;
    }

    // 보유 아이템 팝업 창
    public void showUserItems() {
        // 팝업 창으로 보유 아이템 목록 표시
        String[] itemOptions = new String[userItems.size() + 1]; // 사용 안 함 옵션 추가
        itemOptions[0] = "사용 안 함"; // 사용 안 함 옵션 추가

        for (int i = 0; i < userItems.size(); i++) {
            itemOptions[i + 1] = userItems.get(i).getItemName(); // 1부터 아이템 이름 추가
        }

        // 아이템 선택 팝업 창
        int choice = JOptionPane.showOptionDialog(null, "사용할 아이템을 선택하세요.", "아이템 목록",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, itemOptions, null);

        // 사용자가 창을 닫았을 때 -1 반환
        if (choice != -1) {
            // 선택한 옵션이 "사용 안 함"인 경우
            if (choice == 0) {
                isUseItem = false;
                useItemId = 0; // 사용 안 함을 나타내는 값으로 설정
            } else {
                // 선택한 옵션이 아이템인 경우
                Item selectedItem = userItems.get(choice - 1); // 0부터 시작했으므로 1을 뺌
                isUseItem = true;
                useItemId = selectedItem.getItemId();
            }
        }
    }


    public Item findItemById(int itemId) {
        for (Item item : userItems) {
            if (item.getItemId() == itemId) {
                return item; // 아이템을 찾았을 경우 해당 아이템 반환
            }
        }
        return null; // 아이템을 찾지 못했을 경우 null 반환
    }

    /*
                        equals와 hashCode를 override 해줘야, 동일유저를 비교할 수 있다
                        비교할 때 -> gameUser 간 equals 비교, list에서 find 등
                     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameUser gameUser = (GameUser) o;

        return id == gameUser.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
