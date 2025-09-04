package sims.hodaksims.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.controller.InventoryController;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.Product;
import sims.hodaksims.model.Warehouse;
import sims.hodaksims.utils.DbConUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDbActions {
    private static Logger log = LoggerFactory.getLogger(InventoryDbActions.class);

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
    public static void depart(Warehouse where, Product item, Integer ammount) throws RepositoryAccessException {
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM INVENTORY WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?")
        ) {
            stmt.setLong(1, where.getId());
            stmt.setLong(2, item.getId());
            ResultSet res = stmt.executeQuery();
            Integer oldAmm = 0;
            while(res.next()){
                oldAmm = res.getInt("QUANTITY");
            }


            if (oldAmm.equals(ammount)){
                log.info(oldAmm.toString());
                PreparedStatement stmtNew = connection.prepareStatement("DELETE FROM INVENTORY WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?");
                stmtNew.setLong(1, where.getId());
                stmtNew.setLong(2, item.getId());
                stmtNew.executeUpdate();
            }else{
                log.info(oldAmm  + "/" +ammount);
                PreparedStatement stmtNew =  connection.prepareStatement("UPDATE INVENTORY SET QUANTITY = QUANTITY - ? WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?");
                stmtNew.setInt(1, ammount);
                stmtNew.setLong(2, where.getId());
                stmtNew.setLong(3, item.getId());
                stmtNew.executeUpdate();
            }
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
     }
    }
