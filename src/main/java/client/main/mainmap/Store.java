package client.main.mainmap;

import client.main.GameUser;

import javax.swing.*;
import java.util.ArrayList;

public class Store extends JFrame{

    ArrayList<Item> items;

    public Store(GameUser user) {
        items = new ArrayList<>();
        Item doubleCoin = new Item(3,"모아니면 도","코인 효과 두배");
        Item fiveNodeMore = new Item(5,"부스터","5칸 추가 이동");
        items.add(doubleCoin);
        items.add(fiveNodeMore);
        // 상점 팝업창 프레임 (현재 코인 수 표시,구매하기,나가기 버튼)
        String [] itemOption = {doubleCoin.getItemName() + "(" + doubleCoin.getItemInfo() + ")", fiveNodeMore.getItemName() + "(" + fiveNodeMore.getItemInfo() + ")"};
        int choice = JOptionPane.showOptionDialog(null,"구매할 아이템을 선택하세요.","상점",0,0,null,itemOption,null);
        // 구매하기
        if(choice==0){
            if(user.getCoin()>2){
                user.addUserItem(doubleCoin);
                user.setCoin(user.getCoin()-3);
                System.out.println(user.getCoin());
            }
            else{
                JOptionPane.showMessageDialog(null,"코인이 부족합니다.");
            }
        }
        else {
            if(user.getCoin()>4){
                user.addUserItem(fiveNodeMore);
                user.setCoin(user.getCoin()-5);
                System.out.println(user.getCoin());
            }
            else{
                JOptionPane.showMessageDialog(null,"코인이 부족합니다.");
            }

        }

    }


}
