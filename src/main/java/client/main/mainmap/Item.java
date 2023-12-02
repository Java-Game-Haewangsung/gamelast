package client.main.mainmap;

public class Item {

    private int itemPrice;
    private String itemName;
    private String itemInfo;

    public int getItemPrice() {
        return itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public Item (int itemPrice,String itemName,String itemInfo) {
        this.itemPrice = itemPrice;
        this.itemName = itemName;
        this.itemInfo = itemInfo;
    }

}
