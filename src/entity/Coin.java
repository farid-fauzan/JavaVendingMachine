package entity;

public enum Coin {
    NOL(0), DUARIBU(2000),LIMARIBU(5000),SEPULUHRIBU(10000),DUAPULUHRIBU(20000),LIMAPULUHRIBU(50000);

    private int coinValue;
    Coin(int i) {
        this.coinValue = i;
    }
    public int getCoinValue(){
        return this.coinValue;
    }
}