package client.main.object;

import client.main.GameUser;

import javax.swing.*;
import java.util.ArrayList;

public class Store extends JFrame {

    ArrayList<Item> items;

    public Store(GameUser user) {
        items = new ArrayList<>();
        Item doubleCoin = new Item(1, 3, "모 아니면 도", "5코인");
        Item fiveNodeMore = new Item(2, 5, "부스터", "3코인");
        items.add(doubleCoin);
        items.add(fiveNodeMore);

        // 상점 팝업창 프레임 (현재 코인 수 표시, 구매하기, 나가기 버튼)
        String[] itemOption = {
                doubleCoin.getItemName() + "(" + doubleCoin.getItemInfo() + ")",
                fiveNodeMore.getItemName() + "(" + fiveNodeMore.getItemInfo() + ")",
                "구매 안 함"
        };
        int choice = JOptionPane.showOptionDialog(null, "구매할 아이템을 선택하세요.", "상점", 0, 0, null, itemOption, null);

        // itemId:1 - '모 아니면 도' 구매
        if (choice == 0) {
            if (user.getCoin() != 0) {
                user.addUserItem(doubleCoin); // 플레이어 아이템 리스트에 아이템 객체 전달
                user.addCoin(-5); // 코인 차감
            } else {
                JOptionPane.showMessageDialog(null, "코인이 부족합니다.");
            }
        } else if (choice == 1) { // itemId:2 - '부스터' 구매
            if (user.getCoin() != 0) {
                user.addUserItem(fiveNodeMore);
                user.addCoin(-3);
            } else {
                JOptionPane.showMessageDialog(null, "코인이 부족합니다.");
            }
        } else {
            // 구매 안 함
        }
    }
}
