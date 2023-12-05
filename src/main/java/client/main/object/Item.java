package client.main.object;

public class Item {

    private int itemPrice;
    private int itemId;
    private String itemName; // 1번: '모아니면도', 2번: '부스터' 아이템
    private String itemInfo;

    private int itemStock;

    public int getItemPrice() {
        return itemPrice;
    }

    public int getItemId() { return itemId; }

    public String getItemName() {
        return itemName;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public int getItemStock() {
        return itemStock;
    }


    public Item(int itemId, int itemPrice, String itemName, String itemInfo) {
        this.itemId = itemId;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
        this.itemInfo = itemInfo;
    }
}

