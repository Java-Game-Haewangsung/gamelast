package client.main.object;

import java.awt.*;

public class PlanetNode extends Unit {
    private int id;
    private int coin; // 해당 행성에 할당되는 코인 수
    private boolean sun = false; // 해당 노드 태양 생성 여부

    public PlanetNode(int id, int x, int y, Image img, int coin) {
        this.id = id;
        this.posX = x;
        this.posY = y;
        this.img = img;
        this.coin = coin;
    }

    public PlanetNode(int x, int y, Image img) {
        this.posX = x;
        this.posY = y;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoin() {
        return coin;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public void setSun(boolean change) { // 메인에서 랜덤 돌려서 나온 노드에 sun 위치시킴
        sun = true;
    }

    public boolean isSun() {
        return sun;
    }

}

