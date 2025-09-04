package sims.hodaksims.repository;

import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.Product;
import sims.hodaksims.model.Warehouse;
import sims.hodaksims.utils.DbConUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDbActions {
    public static void deliver(Warehouse where, Product item, Integer ammount) throws RepositoryAccessException {
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM INVENTORY WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?")
        ){
            stmt.setLong(1, where.getId());
            stmt.setLong(2, item.getId());
            ResultSet res = stmt.executeQuery();
            if(res.next()) {
                PreparedStatement stmtNew =  connection.prepareStatement("UPDATE INVENTORY SET QUANTITY = QUANTITY + ? WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?");
                stmtNew.setInt(1, ammount);
                stmtNew.setLong(2, where.getId());
                stmtNew.setLong(3, item.getId());
                stmtNew.executeUpdate();
            }else{
                PreparedStatement stmtNew =  connection.prepareStatement("INSERT INTO INVENTORY (WAREHOUSE_ID, PRODUCT_ID, QUANTITY) VALUES (?, ?, ?);");
                stmtNew.setLong(1, where.getId());
                stmtNew.setLong(2, item.getId());
                stmtNew.setInt(3, ammount);
                stmtNew.executeUpdate();
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
