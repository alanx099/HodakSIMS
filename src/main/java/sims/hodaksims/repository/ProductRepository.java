package sims.hodaksims.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.exceptions.EmptyRepositoryResultException;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.*;
import sims.hodaksims.utils.DbConUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * WareHouse repository nasljeđuuje abstrakrnu klasu {@link AbstractRepository}
 * te implementira sve njene metode
 * @param <T>
 */
public class ProductRepository<T extends Product> extends AbstractRepository<T>{
    private static Logger log = LoggerFactory.getLogger(ProductRepository.class);

    /**
     *Metoda find by id potražuje klasu skladište u bazipodadataka te nam vraća objekt
     * @param id
     * @return
     */
    public T findById(Long id){
        T product;
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("SELECT PRODUCT.* FROM PRODUCT WHERE ID = ?");
        ){
            stmt.setLong(1,id);
            ResultSet resultSet = stmt.executeQuery();
            if(!resultSet.next()){
                throw new EmptyRepositoryResultException("Not found");
            }else {
                product = this.extracProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
        product.setSuppliers(this.getSupplierList(id));
        return product;
    }

    /**
     * Metoda find all nam vraća sva skladišta unutar baze
     * @return
     * @throws RepositoryAccessException
     */
    public List<T>findAll() throws RepositoryAccessException{
            List<T> products = new ArrayList<>();

            try(Connection connection = DbConUtil.getConnection();
                Statement stmt = connection.createStatement();) {

                ResultSet resultSet = stmt.executeQuery("SELECT PRODUCT.* FROM PRODUCT");
                    while(resultSet.next()){
                        Product product = this.extracProductFromResultSet(resultSet);
                        product.setSuppliers(this.getSupplierList(product.getId()));
                        products.add((T)product);
                    }
                return products;
            }catch(SQLException e) {
                throw new RepositoryAccessException(e.getMessage());
            }
    }

    /**
     * Metoda save u sklopu Product radi transakciju koja sprema u odgovarajuću
     * tablicu skladište te uz to i u odvojenu tablicu njen kapacitet
     * @param entity
     */
    public void save(T entity){
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("INSERT INTO " +
                    "PRODUCT(SKU, NAME, PRICE, CATEGORY_ID) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
        );){
            connection.setAutoCommit(false);
            productFields(entity, stmt);

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Failed to insert new productr");
            }
            connection.commit();
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), entity.toString(), LocalDateTime.now());
            unosLog.newEntry("product");
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    for (Supplier sup : entity.getSuppliers()){
                        try(PreparedStatement capStmt = connection.prepareStatement("INSERT INTO PRODUCT_SUPPLIER(PRODUCT_ID, SUPPLIER_ID) VALUES(?,?)");) {
                            capStmt.setLong(1, generatedKeys.getLong(1));
                            capStmt.setLong(2, sup.getId());
                            capStmt.executeUpdate();
                            connection.commit();
                        }
                    }
                }else{
                    throw new SQLException("No id retrived!");
                }

            }
            connection.setAutoCommit(true);
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
    }
    private void productFields(T entity, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getSku());
        stmt.setString(2,entity.getName());
        stmt.setBigDecimal(3, entity.getPrice());
        stmt.setLong(4, entity.getCategory().getId());
    }
    /**
     * Metoda update postavlja nove vrijednosti za postojeće skladište
     * Izmjena briše sve unose za kapaciteta u bazi i piše nove
     * @param entity
     */
    public void update(T entity){
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("UPDATE " +
                    "PRODUCT SET SKU=?, NAME=?, PRICE=?, CATEGORY_ID=? WHERE ID =?",
                    Statement.RETURN_GENERATED_KEYS
            );){
            T oldItem = this.findById(entity.getId());
            productFields(entity, stmt);
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();

            try(PreparedStatement capStmt = connection.prepareStatement("DELETE FROM PRODUCT_SUPPLIER WHERE PRODUCT_ID = ? ")) {
                capStmt.setLong(1, entity.getId());
                capStmt.executeUpdate();
            }

            for (Supplier cap : entity.getSuppliers()) {
                try (PreparedStatement capStmt = connection.prepareStatement("INSERT INTO PRODUCT_SUPPLIER(PRODUCT_ID,SUPPLIER_ID) VALUES(?,?)")) {
                    capStmt.setLong(1, entity.getId());
                    capStmt.setLong(2, cap.getId());
                    capStmt.executeUpdate();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }
            T newItem = this.findById(entity.getId());
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), entity.getName(), LocalDateTime.now());
            unosLog.updateEntry(oldItem,newItem, "product");
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Metoda kojom brišemo Skladišta i kapacitete iz skladišta
     *
     * @param entity
     * @throws RepositoryAccessException
     */
    @Override
    public void delete(T entity) throws RepositoryAccessException {
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement capStmt = connection.prepareStatement("DELETE FROM PRODUCT_SUPPLIER WHERE PRODUCT_ID = ? ")) {
            capStmt.setLong(1, entity.getId());
            capStmt.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("DELETE " +  "FROM PRODUCT  WHERE ID =?" );){
            stmt.setLong(1, entity.getId());
            stmt.executeUpdate();
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), entity.toString(), LocalDateTime.now());
            unosLog.delEntry("product");
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Metoda extracProductFromResultSet je pomoćna metoda za instanciranje
     * objekat iz podataka dobivenih result setom
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private T extracProductFromResultSet(ResultSet resultSet) throws SQLException{
        CategoryRepository categoryRepository = new CategoryRepository();
        Long id =resultSet.getLong("id");
        String sku = resultSet.getString("sku");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");
        Long categoryId = resultSet.getLong("category_id");
        Product product = new Product(sku, name, price, categoryRepository.findById(categoryId));
        product.setId(id);
        return (T)product;
    }
    private List<Supplier> getSupplierList(Long id){
        List<Supplier> result = new ArrayList<>();
        SupplierRepository<Supplier> sRep = new SupplierRepository<>();
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("SELECT PRODUCT_SUPPLIER .* FROM PRODUCT_SUPPLIER  WHERE PRODUCT_ID = ?");
        ){
            stmt.setLong(1,id);
            ResultSet resultSet = stmt.executeQuery();
            try {while(resultSet.next()){
                Long suppId = resultSet.getLong("supplier_id");
                result.add(sRep.findById(suppId));
            }}catch(SQLException e){
                log.error(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
        return result;
    }

}
