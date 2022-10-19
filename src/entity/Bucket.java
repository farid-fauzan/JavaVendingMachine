package entity;

import java.util.List;

public class Bucket {
    private Product product;
    private List<Coin> coin;
    public Bucket(Product product2, List<Coin> coines) {
// TODO Auto-generated constructor stub
        this.product = product2;
        this.coin = coines;
    }
    public Bucket(List<Coin> coins) {
// TODO Auto-generated constructor stub
        this.coin = coins;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public List<Coin> getCoin() {
        return coin;
    }
    public void setCoin(List<Coin> coin) {
        this.coin = coin;
    }
}