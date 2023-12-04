package client.mini3;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class KeyControl extends KeyAdapter implements Runnable{
    Random random = new Random();
    static long skillTime = 0;
    boolean KeyLeft = false; // 왼쪽 이동.
    boolean KeyRight = false; // 오른쪽 이동.
    boolean KeySpace = false; // 미사일발사.
    Player player;
    int cnt; // 총알 발사의 주기를 낮추기 위한 카운트.
    GameView gameView;

    public KeyControl(Player player, GameView gameView) {
        this.player = player;
        this.cnt = 0;
        this.gameView = gameView;
    }

    // 키가 눌렸을 때.
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT :
                KeyLeft = true;
                break;
            case KeyEvent.VK_RIGHT :
                KeyRight = true;
                break;
            case KeyEvent.VK_SPACE:
                KeySpace = true;
                break;

        }
    }

    // 키가 눌렸다 때어졌을 때.
    @Override
    public void keyReleased(KeyEvent e) {

        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT :
                KeyLeft = false;
                break;
            case KeyEvent.VK_RIGHT :
                KeyRight = false;
                break;
            case KeyEvent.VK_SPACE:
                KeySpace = false;
                break;
        }

    }

    // 키를 눌렀을 때의 처리.
    public void keyProcess() {
        if(KeyLeft == true) {
            if(this.player.posX > 0)
                this.player.posX -= 5;
        }
        if(KeyRight == true) {
            if(this.player.posX < 740)
                this.player.posX +=5;
        }
        if(KeySpace == true) { // 미사일 발사.
            // 총알 발사시, 총알 객체 만들고 총알들을 모아두는 리스트에 추가.
            if(cnt % 8 == 0) {
                //발사음.playOnetime(2);// 발사때마다 효과음 발생.
                Missile missile = new Missile(this.player,3);
                this.gameView.Missiles.add(missile);
//                else {
//                    Bullet top = new Bullet(this.player,330,2);
//                    this.gameView.Bullets.add(top);
//
//                    Bullet middle = new Bullet(this.player,0,2);
//                    this.gameView.Bullets.add(middle);
//
//                    Bullet bottom = new Bullet(this.player,30,2);
//                    this.gameView.Bullets.add(bottom);
//                }
            }
        }
    }

    // 미사일과 운석 충돌 처리
    public void crashProcess1() {
        Iterator<Missile> missileIterator = this.gameView.Missiles.iterator();

        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();

            Iterator<Meteors> meteorIterator = this.gameView.Meteors.iterator();

            while (meteorIterator.hasNext()) {
                Meteors meteor = meteorIterator.next();

                if (gameView.Crash(missile.posX, missile.posY, meteor.posX, meteor.posY, missile.width, missile.height, meteor.width, meteor.height)) {
                    missileIterator.remove();
                    meteorIterator.remove();
                    int randomX = random.nextInt(799) + 1;
                    this.gameView.Meteors.add(new Meteors(randomX, 1));
                    break; // 충돌 처리 후 바로 while 루프를 빠져나가게 수정
                }
            }
        }
    }

    // 미사일과 폭탄 충돌 처리
    public void crashProcess2() {
        Iterator<Missile> missileIterator = this.gameView.Missiles.iterator();

        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();

            Iterator<Bomb> bombIterator = this.gameView.Bombs.iterator();

            while (bombIterator.hasNext()) {
                Bomb bomb = bombIterator.next();

                if (gameView.Crash(missile.posX, missile.posY, bomb.posX, bomb.posY, missile.width, missile.height, bomb.width, bomb.height)) {
                    missileIterator.remove();
                    bombIterator.remove();
                    int randomX = random.nextInt(799) + 1;
                    this.gameView.Bombs.add(new Bomb(randomX, 1));
                    this.player.hp -= 10;
                    break; // 충돌 처리 후 바로 while 루프를 빠져나가게 수정
                }
            }
        }
    }

    public void bombProcess2() {
        int randomX = random.nextInt(799) + 1;
        List<Bomb> bombsToRemove = new ArrayList<>();

        // 폭탄과 우주선 충돌시 처리
        for (int i = 0; i < this.gameView.Bombs.size(); i++) {
            Bomb bomb = this.gameView.Bombs.get(i);
            for (int j = 0; j < this.gameView.Missiles.size(); j++) {

            }
            if (gameView.Crash(this.gameView.player.posX, this.gameView.player.posY, bomb.posX, bomb.posY, this.gameView.player.width, this.gameView.player.height, bomb.width, bomb.height)) {
                bombsToRemove.add(bomb);  // 제거 목록에 폭탄 추가
                this.gameView.Bombs.add(new Bomb(randomX, 1));
                int x = this.gameView.player.posX;
                int y = this.gameView.player.posY;
                this.gameView.player.hp -= 50;
                //this.gameView.Effects.add(new Effect(x, y));
            }
        }

        // 반복이 완료된 후에 폭탄 제거
        this.gameView.Bombs.removeAll(bombsToRemove);
    }

    @Override
    public void run() {
        while(true) {
            try {
                keyProcess();
                crashProcess1();
                crashProcess2();
                bombProcess2();
                this.cnt++;
                //PlayerProcess();
                Thread.sleep(20);
                if(this.player.hp <= 0) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
