package client.main.object;

import client.main.GameUser;

import javax.swing.*;
import java.awt.*;

public class Sun extends Unit {
    private Image sunImg;
//    private int posX, posY;
//    private int width = 18, height = 18;

    public Sun(PlanetNode node) {
        posX = node.getPosX();
        posY = node.getPosY() - 10; // 노드 살짝 위에 배치
        node.setSun(true);

        // 이미지 로드 및 크기 조절
        String imagePath = "SOURCE/Planets/sun1.png";
        img = tk.getImage(imagePath);

    }

    public int buySun(GameUser player) {
        int choice = JOptionPane.showConfirmDialog(null,
                "태양을 구매하시겠습니까?\n(10코인을 지불하여 태양을 구매합니다.)", "태양 구매",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (player.getCoin() >= 10) {
                // 태양을 구매할 경우
                player.addCoin(-10); // 코인 차감
                player.addSun(); // 태양 획득
                System.out.println("태양을 구매하였습니다.");
                return 1;
            } else {
                // 코인이 부족할 경우
                JOptionPane.showMessageDialog(null, "코인이 부족합니다.");
                return 0;
            }
        } else {
            // 아니오(No)를 선택한 경우: 아무 작업도 수행하지 않음
            return 0;
        }
    }
}
