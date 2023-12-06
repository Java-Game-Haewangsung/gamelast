package client.main.object;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dice extends JPanel {
    private Timer rollTimer;
    private ImageIcon[] diceIcons;
    private int currentDiceResult;
    private int width = 64, height = 64;

    public Dice() {
        // 주사위 이미지 경로 배열 (1부터 6까지)
        String[] imagePaths = {
                "SOURCE/Dice/dice1.png",
                "SOURCE/Dice/dice2.png",
                "SOURCE/Dice/dice3.png",
                "SOURCE/Dice/dice4.png",
                "SOURCE/Dice/dice5.png",
                "SOURCE/Dice/dice6.png"
        };

        diceIcons = new ImageIcon[6];

        for (int i = 0; i < 6; i++) {
            ImageIcon imageIcon = new ImageIcon(imagePaths[i]);
            diceIcons[i] = imageIcon;
        }

        // 주사위 이미지 변경 타이머
        rollTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rollDice();
            }
        });
    }

    public void setPosition(int x, int y) {
        setLocation(x, y);
    }

    public Image getImage() {
        // 현재 currentDiceResult를 유효한 범위 내로 조정
        int index = (currentDiceResult - 1 + diceIcons.length) % diceIcons.length;
        return diceIcons[index].getImage();
    }

    // 주사위 굴리기
    public void rollDice() {
        int diceResult = (int) (Math.random() * 6) + 1; // 1~6 랜덤 정수 값
//        int diceResult = 3; // (테스트용) 임의의 주사위 값
        currentDiceResult = diceResult; // 현재 주사위 값에 할당
        repaint(); // 이미지 변화 화면 반영
    }

    public void startRolling() {
        rollTimer.start();
    }

    public void stopRolling() {
        rollTimer.stop();
        repaint();
        System.out.println("주사위 멈춤. 결과: " + currentDiceResult);
    }

    public int getDiceResult() {
        return currentDiceResult;
    }
}
