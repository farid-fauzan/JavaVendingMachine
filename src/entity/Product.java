package entity;
public class Product {
    private String itemName;
    private long itemPrice;
    public Product(String name, long price) {
        this.itemName = name;
        this.itemPrice = price;
    }
    public Product(String name) {
        this.itemName= name;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public long getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(long itemPrice) {
        this.itemPrice = itemPrice;
    }
}