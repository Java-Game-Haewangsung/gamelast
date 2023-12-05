package client.main.minigame.mini3;

import client.main.GameUser;

import java.awt.*;
import java.util.Random;

class Unit {
    int posX;
    int posY;
    int width;
    int height;
    Image img;
    // 이미지를 불러오는 역할.
    Toolkit tk = Toolkit.getDefaultToolkit();
}

// 미니게임 플레이어
class Player extends Unit {
    GameUser user;
    int hp;
    public Player(GameUser user) {
        this.user = user;
        int playerHP = 100;
        this.hp = 100;
        this.posX = 400;
        this.posY = 700;
        this.width = 60;
        this.height = 60;
        Image img_ = tk.getImage("SOURCE/spaceship1.png");
        this.img = img_.getScaledInstance(85, 85, Image.SCALE_SMOOTH);
    }
}

// 우주선에서 발사되는 미사일
class Missile extends Unit {
    Player player;
    int speed;
    public Missile(Player player, int speed) {
        this.player = player;
        this.speed = speed;
        this.width = 30;
        this.height = 50;

        // 플레이어의 현재 위치를 기준으로 미사일 초기 위치 설정
        this.posX = player.posX + (player.width - this.width) / 2;
        this.posY = player.posY;
        this.speed = speed;

        // 이미지 로드 및 크기 조절
        Image img_ = tk.getImage("SOURCE/Missile/Missile_3_Flying_000.png");
        this.img = img_.getScaledInstance(45, 75, Image.SCALE_SMOOTH);    }

    public void move() {
        this.posY -= speed;
    }
}

// 운석, 폭탄이 상속받을 Floating 클래스
class Floating extends Unit {
    int moveSpeed;
    int frameHeight = 800;
    int frameWidth = 800;
    public void move() {

    }
}

//운석들
class Meteors extends Floating {
    int damage = 5;

    public Meteors(int posX, int posY) {
        this.moveSpeed = 1;
        // 랜덤 객체 생성
        Random random = new Random();
        // 1에서 10 사이의 랜덤 정수 생성
        int randomNumber = random.nextInt(10) + 1;

        this.width = 60;
        this.height = 60;
        this.posX = posX;
        // posY를 프레임의 상단에서부터 시작하도록 랜덤하게 설정
        this.posY = -random.nextInt(800);

        Image img_ = tk.getImage("SOURCE/Meteors/Meteor_0" + randomNumber + ".png");
        this.img = img_.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
    }

    @Override
    public void move() {
        this.posY += this.moveSpeed;

        Random random = new Random();

        // 운석이 화면 아래로 나갔을 때 초기화
        if (this.posY >= 695) {
            int randomNumber = random.nextInt(10) + 1;


            this.posY = -random.nextInt(frameHeight);
            this.posX = random.nextInt(frameWidth);
        }
    }
}

class Bomb extends Floating {
    public Bomb(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.moveSpeed = 3;
        this.width = 30;
        this.height = 50;

        // 이미지 로드 및 크기 조절
        Image img_ = tk.getImage("SOURCE/Bombs/Bomb.png");
        this.img = img_.getScaledInstance(45, 75, Image.SCALE_SMOOTH);
    }

    @Override
    public void move() {
        this.posY += moveSpeed;

        // 폭탄이 화면 아래로 나갔을 때 초기화
        if (this.posY >= frameHeight) {
            int randomNumber = new Random().nextInt(10) + 1;

            this.posY = 1;
            this.posX = new Random().nextInt(frameWidth);

        }
    }
}