package sims.hodaksims.main;

import sims.hodaksims.model.Warehouse;
import sims.hodaksims.repository.AbstractRepository;
import sims.hodaksims.repository.WarehouseRepository;

import java.util.List;

public class Helper {

    public static void main(String[] args) {
        AbstractRepository<Warehouse> warehouse = new WarehouseRepository<>();
        List<Warehouse> warehousesTest = warehouse.findAll();

        System.out.println(warehousesTest);
    }

}
