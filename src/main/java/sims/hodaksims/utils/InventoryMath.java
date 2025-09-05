package sims.hodaksims.utils;

import javafx.collections.ObservableList;
import sims.hodaksims.model.Category;
import sims.hodaksims.model.Pair;
import sims.hodaksims.model.Product;
import sims.hodaksims.model.WareCapacity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * InventoryMath klasa s funkcijama za računanje po skladištu
 */
public class InventoryMath {
    private InventoryMath() {
    }

    /**
     * calculateStoredAmmount izračunaj ostatak skladišta
     * @param products products
     * @param category category
     * @return
     */
    public static Integer calculateStoredAmmount(ObservableList<Pair<Product, Integer>> products, Category category){
        return products.stream().filter(x -> x.getKey().getCategory().getName().equals(category.getName())).mapToInt(Pair::getValue).sum();
    }

    /**
     * calculateMaxOrder izračunak najveću naruđvu
     * @param products products
     * @param category category
     * @param capacity capacity
     * @return calculateMaxOrder
     */
    public static Integer calculateMaxOrder(ObservableList<Pair<Product, Integer>> products, Category category, List<WareCapacity> capacity){
        return  capacity.stream().filter(x -> x.getCategory().getName().equals(category.getName())).findFirst().map(WareCapacity::getCapacity).orElse(0) - calculateStoredAmmount(products, category);
    }

    /**
     * calculatePercentage ostatak skladišta u postotcima
     * @param products products
     * @param wareCapacity wareCapacity
     * @return calculatePercentage
     */
    public static BigDecimal calculatePercentage(ObservableList<Pair<Product, Integer>> products, WareCapacity wareCapacity){
        BigDecimal categorySum = BigDecimal.valueOf(calculateStoredAmmount(products, wareCapacity.getCategory()));
        BigDecimal productMax =  BigDecimal.valueOf(wareCapacity.getCapacity());
        BigDecimal hundred = BigDecimal.valueOf(100);
        if(productMax.equals(BigDecimal.ZERO)){
            return BigDecimal.ZERO;
        }
        return categorySum.divide(productMax,10, RoundingMode.HALF_UP).multiply(hundred).setScale(2, RoundingMode.HALF_UP);
    }
}
