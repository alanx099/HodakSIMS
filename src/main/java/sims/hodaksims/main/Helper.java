package sims.hodaksims.main;

import sims.hodaksims.model.Warehouse;
import sims.hodaksims.repository.AbstractRepository;
import sims.hodaksims.repository.WarehouseRepository;

import java.util.Arrays;
import java.util.List;

public class Helper {

    public static void main(String[] args) {
        AbstractRepository<Warehouse> warehouse = new WarehouseRepository<>();
        List<Warehouse> warehousesTest = warehouse.findAll();
        Warehouse newGuy = warehousesTest.get(0);
        String[] oldValue = newGuy.toString().split(",");
        newGuy.setCity("Varazdin");
        String[] printNew = newGuy.toString().split(",");
        String changes = new String("Promjene:");

        for(int i =0; i< Arrays.stream(oldValue).count();i++){
            if(oldValue[i].compareTo(printNew[i]) != 0){
                changes = changes + oldValue[i]+"->"+printNew[i];
            }
        }

        System.out.println(changes);

    }

}
