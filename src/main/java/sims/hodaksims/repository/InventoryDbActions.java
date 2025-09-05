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
    private InventoryDbActions() {
    }


    /**
     * deliver izvrši dostavu
     * @param where where
     * @param item item
     * @param ammount ammount
     * @throws RepositoryAccessException
     */
    public static void deliver(Warehouse where, Product item, Integer ammount) throws RepositoryAccessException {
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT INVENTORY.* FROM INVENTORY WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?")
        ){
            stmt.setLong(1, where.getId());
            stmt.setLong(2, item.getId());
            ResultSet res = stmt.executeQuery();
            if(res.next()) {
                sumProduct(connection, where,  item,  ammount );
            }else{
                insertIntoInventory(connection, where,  item,  ammount);
            }

        }catch (SQLException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
    }
    private static void sumProduct(Connection connection, Warehouse where, Product item, Integer ammount){
        try(PreparedStatement stmtNew =  connection.prepareStatement("UPDATE INVENTORY SET QUANTITY = QUANTITY + ? WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?");){
            stmtNew.setInt(1, ammount);
            stmtNew.setLong(2, where.getId());
            stmtNew.setLong(3, item.getId());
            stmtNew.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * depart isprazi proizvode iz skladišta
     * @param where where
     * @param item item
     * @param ammount ammount
     * @throws RepositoryAccessException RepositoryAccessException
     */
    public static void depart(Warehouse where, Product item, Integer ammount) throws RepositoryAccessException {
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT INVENTORY.* FROM INVENTORY WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?")
        ) {
            stmt.setLong(1, where.getId());
            stmt.setLong(2, item.getId());
            ResultSet res = stmt.executeQuery();
            Integer oldAmm = 0;
            while(res.next()){
                oldAmm = res.getInt("QUANTITY");
            }

            if (oldAmm.equals(ammount)){
                deleteFromInventory(connection, where, item);
            }else{
                subtractFromInventory(connection, where, item, ammount);
            }
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
     }
     private static void deleteFromInventory(Connection connection, Warehouse where, Product item) throws RepositoryAccessException {
         try(PreparedStatement stmtNew = connection.prepareStatement("DELETE FROM INVENTORY WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?")){
         stmtNew.setLong(1, where.getId());
         stmtNew.setLong(2, item.getId());
         stmtNew.executeUpdate();}
         catch (SQLException e){
             throw new RepositoryAccessException(e.getMessage());
         }
     }
     private static void subtractFromInventory(Connection connection, Warehouse where, Product item, Integer ammount)  {
        try(PreparedStatement stmtNew =  connection.prepareStatement("UPDATE INVENTORY SET QUANTITY = QUANTITY - ? WHERE WAREHOUSE_ID = ? AND PRODUCT_ID = ?")){
            stmtNew.setInt(1, ammount);
            stmtNew.setLong(2, where.getId());
            stmtNew.setLong(3, item.getId());
            stmtNew.executeUpdate();
        }catch (SQLException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
     }
     private static void insertIntoInventory(Connection connection, Warehouse where, Product item, Integer ammount ){
         try(PreparedStatement stmtNew =  connection.prepareStatement("INSERT INTO INVENTORY (WAREHOUSE_ID, PRODUCT_ID, QUANTITY) VALUES (?, ?, ?);")){
         stmtNew.setLong(1, where.getId());
         stmtNew.setLong(2, item.getId());
         stmtNew.setInt(3, ammount);
         stmtNew.executeUpdate();
     }catch (SQLException e){
         throw new RepositoryAccessException(e.getMessage());
         }
     }

    }
