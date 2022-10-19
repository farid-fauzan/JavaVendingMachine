package api;


import entity.Bucket;
import entity.Coin;
import entity.Product;
import exception.ItemNotSelectedException;
import exception.NotAChangedException;
import exception.NotFullPaidException;
import exception.ProductNotFoundExcepton;

import java.util.Optional;

/**
 * @author milind.bhuktar
 *
 */
public interface VendingMachine {
    public int selectItemGetPrice(Product product) throws ProductNotFoundExcepton, ProductNotFoundExcepton;
    public Optional<Bucket> insertCoin(Coin... coins) throws ItemNotSelectedException, NotFullPaidException;
    public Bucket getItemsAndChange(int coinValue) throws NotAChangedException;
}