// Player.java

import javax.swing.*;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class Player extends JLabel {
    private int x, y, w, h, dx, dy;
    private Image playerImg;
    private int width = 800;
    private int height = 800;

    public Player(int initialX, int initialY, int width, int height, Image img) {
        x = initialX - width / 2;
        y = initialY - height;
        w = width;
        h = height;
        playerImg = img;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        x = this.x;
    }

    public void setY(int y) {
        y = this.y;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public Image getImage() {
        return playerImg;
    }

    public Image setImage(Image img) {
        playerImg = img;

        return playerImg;
    }

    public void move() {
        // 플레이어 좌표 변경
        x += dx;
        y += dy;

        // 플레이어가 화면 밖으로 나가지 않도록
        if (x < 0)
            x = 0;
        if (x > width - w)
            x = width - w;
        if (y < 0)
            y = 0;
        if (y > height - h)
            y = height - h;
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                dx = -5;
                break;
            case KeyEvent.VK_RIGHT:
                dx = 5;
                break;
            case KeyEvent.VK_UP:
                dy = -5;
                break;
            case KeyEvent.VK_DOWN:
                dy = 5;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                dx = 0;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                dy = 0;
                break;
        }
    }
}
