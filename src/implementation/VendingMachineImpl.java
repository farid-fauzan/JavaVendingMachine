package implementation;

import api.Inventory;
import api.VendingMachine;
import entity.Bucket;
import entity.Coin;
import entity.Product;
import exception.ItemNotSelectedException;
import exception.NotAChangedException;
import exception.NotFullPaidException;
import exception.ProductNotFoundExcepton;

import java.util.*;
import java.util.stream.Collectors;

public class VendingMachineImpl implements VendingMachine {

    private Inventory<Product, Integer> itemInventory= new Inventory<Product, Integer>();
    private Inventory<Coin, Integer> cashInventory = new Inventory<Coin, Integer>();
    private Product currentItem;
    private int currentBalance;

    public VendingMachineImpl() {
        initialize();
    }
    private void initialize() {
        this.itemInventory.putInventory(new Product("Biskuit", 6000), 10);
        this.itemInventory.putInventory(new Product("Chips", 8000), 10);
        this.itemInventory.putInventory(new Product("Oreo", 10000), 10);
        this.itemInventory.putInventory(new Product("Tango", 12000), 10);
        this.itemInventory.putInventory(new Product("Cokelat", 15000), 10);

        this.cashInventory.putInventory(Coin.NOL, 10);
        this.cashInventory.putInventory(Coin.DUARIBU, 5);
        this.cashInventory.putInventory(Coin.LIMARIBU,5);
        this.cashInventory.putInventory(Coin.SEPULUHRIBU,10);
        this.cashInventory.putInventory(Coin.DUAPULUHRIBU,50);
        this.cashInventory.putInventory(Coin.LIMAPULUHRIBU,10);
        this.setCurrentBalance();

        //Biskuit: 6000
        //Chips: 8000
        //Oreo: 10000
        //Tango: 12000
        //Cokelat: 15000
    }
    private void setCurrentBalance() {
        if(this.cashInventory.getInvetory().size() >0){
            List<Integer> cashCoinList = this.cashInventory.getInvetory().entrySet()
                    .stream().map(e->e.getKey().getCoinValue()*e.getValue())
                    .collect(Collectors.toList());
            Optional<Integer> currentBalance = cashCoinList.stream().reduce(Integer::sum);
            this.currentBalance = currentBalance.get().intValue();
        }
    }
    @Override
    public int selectItemGetPrice(Product product) throws ProductNotFoundExcepton {
        List<Map.Entry<Product, Integer>> productPrice = this.itemInventory.getInvetory().entrySet().stream().filter(e->e.getKey().getItemName().equals(product.getItemName())).collect(Collectors.toList());
        if(!productPrice.isEmpty()){
            Product selectedProduct = productPrice.get(0).getKey();
            this.currentItem = selectedProduct;
            return (int)selectedProduct.getItemPrice();
        }else{
            throw new ProductNotFoundExcepton("Product Not available");
        }
    }
    public void displayInsertedCoinValue(Coin... coins){
        Optional<Integer> insertedCoinValue = Arrays.asList(coins).stream().map(e->e.getCoinValue()).collect(Collectors.toList()).stream().reduce(Integer::sum);
        int insertedValue = insertedCoinValue.get().intValue();
        System.out.println("Inserted Coin Value: "+insertedValue);
    }
    @Override
    public Optional<Bucket> insertCoin(Coin... coins) throws NotFullPaidException, ItemNotSelectedException {

        Bucket bucket= null;
        if(currentItem != null){
            Optional<Integer> insertedCoinValue = Arrays.asList(coins).stream().map(e->e.getCoinValue()).collect(Collectors.toList()).stream().reduce(Integer::sum);
            int insertedValue = insertedCoinValue.get().intValue();
            if(insertedValue < this.currentItem.getItemPrice()){
                bucket=new Bucket(new Product("Not a fullPaid"), Arrays.asList(coins));
            }else{
                try {
                    bucket = this.getItemsAndChange(insertedValue);
                } catch (NotAChangedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }else{
            throw new ItemNotSelectedException("Item Not Selected");
        }

        Bucket returnBucket = bucket==null ?new Bucket(new Product("Item not found"),Arrays.asList(coins)):bucket;
        Optional<Bucket> opt = Optional.ofNullable(returnBucket);
        return opt;
    }
    @Override
    public Bucket getItemsAndChange(int insertedValue) throws NotAChangedException {
        // TODO Auto-generated method stub
        this.addToCashInventory(insertedValue);
        this.setCurrentBalance();
        int changedValue = this.getChanged(insertedValue, (int) this.currentItem.getItemPrice());
        this.substractChangedFromInventory(changedValue);
        this.currentBalance = this.currentBalance-changedValue;
        this.removedItemFromInventory();
        ArrayList<Coin> coins = new ArrayList<Coin>();


        return new Bucket(this.currentItem, this.convertToCoin(new ArrayList<Coin>(),changedValue));
    }

    private List<Coin> convertToCoin(List<Coin> returnCoinsArray, int changedValue){
        int reminder=0;
//        if(changedValue>=Coin.HUNDREDROOPEE.getCoinValue()){
//            reminder = changedValue/Coin.HUNDREDROOPEE.getCoinValue();
//            if(reminder>0){
//                for(int i=0; i<=reminder-1;i++){
//                    returnCoinsArray.add(Coin.HUNDREDROOPEE);
//                }
//            }
//            int test = changedValue-(reminder*Coin.HUNDREDROOPEE.getCoinValue());
//            if(test!=0){
//                convertToCoin(returnCoinsArray, test);
//            }
//        }else
        if(changedValue>=Coin.LIMAPULUHRIBU.getCoinValue()){
            reminder = changedValue/Coin.LIMAPULUHRIBU.getCoinValue();
            if(reminder>0){
                for(int i=0; i<=reminder-1;i++){
                    returnCoinsArray.add(Coin.LIMAPULUHRIBU);
                }
            }
            int test = changedValue-(reminder*Coin.LIMAPULUHRIBU.getCoinValue());
            if(test!=0){
                convertToCoin(returnCoinsArray, test); }
        }else if(changedValue>=Coin.DUAPULUHRIBU.getCoinValue()){
            reminder = changedValue/Coin.DUAPULUHRIBU.getCoinValue();
            if(reminder>0){
                for(int i=0; i<=reminder-1;i++){
                    returnCoinsArray.add(Coin.DUAPULUHRIBU);
                }
            }
            int test = changedValue-(reminder*Coin.DUAPULUHRIBU.getCoinValue());
            if(test!=0){
                convertToCoin(returnCoinsArray, test); }
        }else if(changedValue>=Coin.SEPULUHRIBU.getCoinValue()){
            reminder = changedValue/Coin.SEPULUHRIBU.getCoinValue();
            if(reminder>0){
                for(int i=0; i<=reminder-1;i++){
                    returnCoinsArray.add(Coin.SEPULUHRIBU);
                }
            }
            int test = changedValue-(reminder*Coin.SEPULUHRIBU.getCoinValue());
            if(test!=0){
                convertToCoin(returnCoinsArray, test); }
        }else if(changedValue>=Coin.LIMARIBU.getCoinValue()){
            reminder = changedValue/Coin.LIMARIBU.getCoinValue();
            if(reminder>0){
                for(int i=0; i<=reminder-1;i++){
                    returnCoinsArray.add(Coin.LIMARIBU);
                }
            }
            int test = changedValue-(reminder*Coin.LIMARIBU.getCoinValue());
            if(test!=0){
                convertToCoin(returnCoinsArray, test);
            }
        }else if(changedValue>=Coin.DUARIBU.getCoinValue()){
            reminder = changedValue/Coin.DUARIBU.getCoinValue();
            if(reminder>0){
                for(int i=0; i<=reminder-1;i++){
                    returnCoinsArray.add(Coin.DUARIBU);
                }
            }
            int test = changedValue-(reminder*Coin.DUARIBU.getCoinValue());
            if(test!=0){
                convertToCoin(returnCoinsArray, test);
            }
        }else if(changedValue>=Coin.NOL.getCoinValue()){
            reminder = changedValue/Coin.NOL.getCoinValue();
            if(reminder>0){
                for(int i=0; i<=reminder-1;i++){
                    returnCoinsArray.add(Coin.NOL);
                }
            }
            int test = changedValue-(reminder*Coin.NOL.getCoinValue());
            if(test!=0){
                convertToCoin(returnCoinsArray, test);
            }
        }
        return returnCoinsArray;


    }
    private void removedItemFromInventory(){
        int itemCount= this.itemInventory.getInvetory().get(currentItem);
        this.itemInventory.getInvetory().put(currentItem, itemCount-1);
    }
    private void substractChangedFromInventory(int changedValue){

        int reminder = 0;
//        if(changedValue>=Coin.HUNDREDROOPEE.getCoinValue()){
//            int test = this.putCoinAndDecrement(Coin.HUNDREDROOPEE, changedValue);
//            if(test!=0){
//                substractChangedFromInventory(test);
//            }
//        }else

        if(changedValue>=Coin.LIMAPULUHRIBU.getCoinValue()){
            int test = this.putCoinAndDecrement(Coin.LIMAPULUHRIBU, changedValue);
            if(test!=0){
                substractChangedFromInventory(test);
            }
        }else if(changedValue>=Coin.DUAPULUHRIBU.getCoinValue()){
            int test = this.putCoinAndDecrement(Coin.DUAPULUHRIBU, changedValue);
            if(test!=0){
                substractChangedFromInventory(test);
            }
        }else if(changedValue>=Coin.SEPULUHRIBU.getCoinValue()){
            int test = this.putCoinAndDecrement(Coin.SEPULUHRIBU, changedValue);
            if(test!=0){
                substractChangedFromInventory(test);
            }
        }else if(changedValue>=Coin.LIMARIBU.getCoinValue()){
            int test = this.putCoinAndDecrement(Coin.LIMARIBU, changedValue);
            if(test!=0){
                substractChangedFromInventory(test);
            }
        }else if(changedValue>=Coin.DUARIBU.getCoinValue()){
            int test = this.putCoinAndDecrement(Coin.DUARIBU, changedValue);
            if(test!=0){
                substractChangedFromInventory(test);
            }
        }else if(changedValue>=Coin.NOL.getCoinValue()){
            int test = this.putCoinAndDecrement(Coin.NOL, changedValue);
            if(test!=0){
                substractChangedFromInventory(test);
            }
        }

    }

    private int putCoinAndDecrement(Coin coin, int changedValue){
        int reminder = changedValue/coin.getCoinValue();
        int numberOfCoin =this.cashInventory.getInvetory().get(coin);
        if(numberOfCoin>reminder)
            numberOfCoin=numberOfCoin-reminder;
        this.cashInventory.getInvetory().put(coin, numberOfCoin);
        int test = changedValue-(reminder*coin.getCoinValue());
        return test;
    }

    private int putCoinAndIncreament(Coin coin, int insertedCoinValue){
        int reminder = insertedCoinValue/coin.getCoinValue();
        int numberOfCoin =this.cashInventory.getInvetory().get(coin);
        numberOfCoin=reminder+numberOfCoin;
        this.cashInventory.getInvetory().put(coin, numberOfCoin);
        int test = insertedCoinValue-(reminder*coin.getCoinValue());
        return test;
    }
    private void addToCashInventory(int insertedCoinValue){
//        if(insertedCoinValue>=Coin.HUNDREDROOPEE.getCoinValue()){
//            int test = this.putCoinAndIncreament(Coin.HUNDREDROOPEE, insertedCoinValue);
//            if(test!=0){
//                addToCashInventory(test);
//            }
//        }else
        if(insertedCoinValue>=Coin.LIMAPULUHRIBU.getCoinValue()){
            int test = this.putCoinAndIncreament(Coin.LIMAPULUHRIBU, insertedCoinValue);
            if(test!=0){
                addToCashInventory(test);
            }
        }else if(insertedCoinValue>=Coin.LIMAPULUHRIBU.getCoinValue()){
            int test = this.putCoinAndIncreament(Coin.LIMAPULUHRIBU, insertedCoinValue);
            if(test!=0){
                addToCashInventory(test);
            }
        }else if(insertedCoinValue>=Coin.DUAPULUHRIBU.getCoinValue()){
            int test = this.putCoinAndIncreament(Coin.DUAPULUHRIBU, insertedCoinValue);
            if(test!=0){
                addToCashInventory(test);
            }
        }else if(insertedCoinValue>=Coin.LIMARIBU.getCoinValue()){
            int test = this.putCoinAndIncreament(Coin.LIMARIBU, insertedCoinValue);
            if(test!=0){
                addToCashInventory(test);
            }
        }else if(insertedCoinValue>=Coin.DUARIBU.getCoinValue()){
            int test = this.putCoinAndIncreament(Coin.DUARIBU, insertedCoinValue);
            if(test!=0){
                addToCashInventory(test);
            }
        }else if(insertedCoinValue>=Coin.NOL.getCoinValue()){
            int test = this.putCoinAndIncreament(Coin.NOL, insertedCoinValue);
            if(test!=0){
                addToCashInventory(test);
            }
        }
    }
    private int getChanged(int insertedValue, int itemPrice){
        if(insertedValue > itemPrice){
            return insertedValue-itemPrice;
        }else{
            return itemPrice -insertedValue;
        }
    }
    public void reset() {
        this.currentItem = null;
    }
}