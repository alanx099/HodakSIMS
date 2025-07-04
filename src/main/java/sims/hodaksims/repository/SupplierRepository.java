package sims.hodaksims.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.exceptions.EmptyRepositoryResultException;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.*;
import sims.hodaksims.utils.DbConUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * WareHouse repository nasljeđuuje abstrakrnu klasu {@link AbstractRepository}
 * te implementira sve njene metode
 * @param <T>
 */
public class SupplierRepository<T extends Supplier> extends AbstractRepository<T>{
    private static Logger log = LoggerFactory.getLogger(SupplierRepository.class);

    /**
     *Metoda find by id potražuje klasu skladište u bazipodadataka te nam vraća objekt
     * @param id
     * @return
     */
    public T findById(Long id){
        T supplier;
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("SELECT SUPPLIER.* FROM SUPPLIER WHERE ID = ?");
        ){
            stmt.setLong(1,id);
            ResultSet resultSet = stmt.executeQuery();
            if(!resultSet.next()){
                throw new EmptyRepositoryResultException("Not found");
            }else {
                supplier = this.extracSupplierFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("SELECT SUPPLIER_CONTACT.* FROM SUPPLIER_CONTACT  WHERE SUPPLIER_ID = ?");
        ){
            stmt.setLong(1,id);
            ResultSet resultSet = stmt.executeQuery();
            supplier.setContacts(this.extractContactList(resultSet));

        } catch (SQLException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
        return supplier;
    }

    /**
     * Metoda find all nam vraća sva skladišta unutar baze
     * @return
     * @throws RepositoryAccessException
     */
    public List<T>findAll() throws RepositoryAccessException{
            List<T> suppliers = new ArrayList<>();

            try(Connection connection = DbConUtil.getConnection();
                Statement stmt = connection.createStatement();) {

                ResultSet resultSet = stmt.executeQuery("SELECT SUPPLIER.* FROM SUPPLIER");
                    while(resultSet.next()){
                        Supplier supplier = this.extracSupplierFromResultSet(resultSet);
                        try(PreparedStatement stmtCap =connection.prepareStatement("SELECT supplier_contact.* FROM supplier_contact  WHERE supplier_id = ?")){
                            stmtCap.setLong(1,resultSet.getLong(1));
                            ResultSet resultSetCap = stmtCap.executeQuery();
                            supplier.setContacts(this.extractContactList(resultSetCap));
                        } catch (SQLException e) {
                            throw new RepositoryAccessException(e.getMessage());
                        }
                        suppliers.add((T)supplier);
                    }
                return suppliers;
            }catch(SQLException e) {
                throw new RepositoryAccessException(e.getMessage());
            }
    }

    /**
     * Metoda save u sklopu Warehouse radi transakciju koja sprema u odgovarajuću
     * tablicu skladište te uz to i u odvojenu tablicu njen kapacitet
     * @param entity
     */
    public void save(T entity){
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("INSERT INTO " +
                    "SUPPLIER(NAME, OIB, MIN_ORDER, DELIVERY_TIME, JOINED) VALUES ( ?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
        );){
            connection.setAutoCommit(false);
            supplierFields(entity, stmt);

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Failed to insert new warehouser");
            }
            connection.commit();
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), entity.toString(), LocalDateTime.now());
            unosLog.newEntry("supplier");
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    for (SupplierContact cap : entity.getContacts()){
                        try(PreparedStatement capStmt = connection.prepareStatement("INSERT INTO SUPPLIER_CONTACT(NAME, EMAIL, PHONE, ADDRESS, SUPPLIER_ID) VALUES ( ?,?,?,?,?) ");) {
                            capStmt.setString(1, cap.getName());
                            capStmt.setString(2, cap.getEmail());
                            capStmt.setString(3, cap.getPhone());
                            capStmt.setString(4, cap.getAddress());
                            capStmt.setLong(5, generatedKeys.getLong(1));
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

    private void supplierFields(T entity, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getOib());
        stmt.setInt(3, entity.getMinOrder());
        stmt.setInt(4, entity.getDeliveryTime());
        stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * Metoda update postavlja nove vrijednosti za postojeće skladište
     * Izmjena briše sve unose za kapaciteta u bazi i piše nove
     * @param entity
     */
    public void update(T entity){
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("UPDATE " +
                    "supplier SET NAME=?, OIB=?, MIN_ORDER=?, DELIVERY_TIME=? WHERE ID =?",
                    Statement.RETURN_GENERATED_KEYS
            );){
            T oldItem = this.findById(entity.getId());
            supplierFields(entity, stmt);
            stmt.setLong(5, entity.getId());
            stmt.executeUpdate();

            try(PreparedStatement capStmt = connection.prepareStatement("DELETE FROM SUPPLIER_CONTACT WHERE SUPPLIER_ID = ? ")) {
                capStmt.setLong(1, entity.getId());
                capStmt.executeUpdate();
            }

            for (SupplierContact cap : entity.getContacts()){
                try(PreparedStatement capStmt = connection.prepareStatement("INSERT INTO SUPPLIER_CONTACT(NAME, EMAIL, PHONE, ADDRESS, SUPPLIER_ID) VALUES ( ?,?,?,?,?)");) {
                    capStmt.setString(1, cap.getName());
                    capStmt.setString(2, cap.getEmail());
                    capStmt.setString(3, cap.getPhone());
                    capStmt.setString(4, cap.getAddress());
                    capStmt.setLong(5, entity.getId() );
                    capStmt.executeUpdate();
                    connection.commit();
                }
            }
            T newItem = this.findById(entity.getId());
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), entity.getName(), LocalDateTime.now());
            unosLog.updateEntry(oldItem,newItem, "supplier");
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
            PreparedStatement capStmt = connection.prepareStatement("DELETE FROM SUPPLIER_CONTACT WHERE SUPPLIER_ID = ? ");) {
            capStmt.setLong(1, entity.getId());
            capStmt.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("DELETE FROM SUPPLIER WHERE ID = ? " );) {
            stmt.setLong(1, entity.getId());
            stmt.executeUpdate();
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), entity.toString(), LocalDateTime.now());
            unosLog.delEntry("supplier");
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Metoda extracWarehouseFromResultSet je pomoćna metoda za instanciranje
     * objekat iz podataka dobivenih result setom
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private T extracSupplierFromResultSet(ResultSet resultSet) throws SQLException{
        Long id =resultSet.getLong("id");
        String name = resultSet.getString("name");
        String oib = resultSet.getString("oib");
        Integer minOrder = resultSet.getInt("MIN_ORDER");
        Integer deliveryTime = resultSet.getInt("DELIVERY_TIME");
        LocalDate dateJoined = resultSet.getTimestamp("JOINED").toLocalDateTime().toLocalDate();
        Supplier supplier = new Supplier(name, oib, minOrder, deliveryTime, dateJoined);
        supplier.setId(id);
        return (T)supplier;
    }
    private Set<SupplierContact> extractContactList(ResultSet rez){
        Set<SupplierContact> result = new HashSet<>();

        try {while(rez.next()){
            String name = rez.getString("name");
            String email = rez.getString("email");
            String phone = rez.getString("phone");
            String address = rez.getString("address");

            result.add(new SupplierContact(name, email, phone, address));
        }}catch(SQLException e){
            log.error(e.getMessage());
        }
        return result;
    }

}
