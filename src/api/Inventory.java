package api;

import entity.Product;

import java.util.HashMap;

public class Inventory<T, T2>{

    private HashMap<T,T2> inentory = new HashMap<T,T2>();

    public boolean hashItem(Product product) {
        // TODO Auto-generated method stub
        //return inentory.contains(product) ;
        return true;
    }
    public HashMap<T,T2> getInvetory() {
        return inentory;
    }

    public void putInventory(T t, T2 t2){
        this.inentory.put(t, t2);

    }
}