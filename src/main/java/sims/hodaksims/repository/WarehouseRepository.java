package sims.hodaksims.repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.exceptions.EmptyRepositoryResultException;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.*;
import sims.hodaksims.utils.DbConUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * WareHouse repository nasljeđuuje abstrakrnu klasu {@link AbstractRepository}
 * te implementira sve njene metode
 * @param <T>
 */
public class WarehouseRepository<T extends Warehouse> extends AbstractRepository<T>{
    private static Logger log = LoggerFactory.getLogger(WarehouseRepository.class);

    /**
     *Metoda find by id potražuje klasu skladište u bazipodadataka te nam vraća objekt
     * @param id
     * @return
     */
    public T findById(Long id){
        T warehouse;
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("SELECT WAREHOUSE.* FROM WAREHOUSE WHERE ID = ?");
        ){
            stmt.setLong(1,id);
            ResultSet resultSet = stmt.executeQuery();
            if(!resultSet.next()){
                throw new EmptyRepositoryResultException("Not found");
            }else {
                warehouse = this.extracWarehouseFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("SELECT WAREHOUSE_CAPACITY .* FROM WAREHOUSE_CAPACITY  WHERE WAREHOUSE_ID = ?");
        ){
            stmt.setLong(1,id);
            ResultSet resultSet = stmt.executeQuery();
            warehouse.setCapacity(this.extractCapacatyList(resultSet));

        } catch (SQLException e) {
            throw new RepositoryAccessException(e.getMessage());
        }
        return warehouse;
    }

    /**
     * Metoda find all nam vraća sva skladišta unutar baze
     * @return
     * @throws RepositoryAccessException
     */
    public List<T>findAll() throws RepositoryAccessException{
            List<T> warehouses = new ArrayList<>();

            try(Connection connection = DbConUtil.getConnection();
                Statement stmt = connection.createStatement();) {

                ResultSet resultSet = stmt.executeQuery("SELECT WAREHOUSE.* FROM WAREHOUSE");
                    while(resultSet.next()){
                        Warehouse warehouse = this.extracWarehouseFromResultSet(resultSet);
                        try(PreparedStatement stmtCap =connection.prepareStatement("SELECT WAREHOUSE_CAPACITY .* FROM WAREHOUSE_CAPACITY  WHERE WAREHOUSE_ID = ?")){
                            stmtCap.setLong(1,resultSet.getLong(1));
                            ResultSet resultSetCap = stmtCap.executeQuery();
                            warehouse.setCapacity(this.extractCapacatyList(resultSetCap));
                        } catch (SQLException e) {
                            throw new RepositoryAccessException(e.getMessage());
                        }
                        warehouses.add((T)warehouse);
                    }
                return warehouses;
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
                    "WAREHOUSE(NAME, CITY, COUNTRY, POSTAL_CODE, STREET_NUMBER, STREET_NAME) VALUES (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
        );){
            connection.setAutoCommit(false);
            warehoseFields(entity, stmt);

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Failed to insert new warehouser");
            }
            connection.commit();
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), entity.toString(), LocalDateTime.now());
            unosLog.newEntry("warehouse");
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    for (WareCapacity cap : entity.getCapacity()){
                        try(PreparedStatement capStmt = connection.prepareStatement("INSERT INTO WAREHOUSE_CAPACITY(CAPACITY, WAREHOUSE_ID, CATEGORY_ID) VALUES(?,?,?)");) {
                            capStmt.setInt(1, cap.getCapacity());
                            capStmt.setLong(2, generatedKeys.getLong(1));
                            capStmt.setLong(3, cap.getCategory().getId());
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

    private void warehoseFields(T entity, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getCity());
        stmt.setString(3,entity.getCountry());
        stmt.setString(4, entity.getPostalCode());
        stmt.setString(5, entity.getStreetNumber());
        stmt.setString(6, entity.getStreetName());
    }

    /**
     * Metoda update postavlja nove vrijednosti za postojeće skladište
     * Izmjena briše sve unose za kapaciteta u bazi i piše nove
     * @param entity
     */
    public void update(T entity){
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("UPDATE " +
                    "WAREHOUSE SET NAME=?, CITY=?, COUNTRY=?, POSTAL_CODE=?, STREET_NUMBER=?, STREET_NAME=? WHERE ID =?",
                    Statement.RETURN_GENERATED_KEYS
            );){
            T oldItem = this.findById(entity.getId());
            warehoseFields(entity, stmt);
            stmt.setLong(7, entity.getId());
            stmt.executeUpdate();

            try(PreparedStatement capStmt = connection.prepareStatement("DELETE FROM WAREHOUSE_CAPACITY WHERE WAREHOUSE_ID = ? ")) {
                capStmt.setLong(1, entity.getId());
                capStmt.executeUpdate();
            }

            for (WareCapacity cap : entity.getCapacity()) {
                try (PreparedStatement capStmt = connection.prepareStatement("INSERT INTO WAREHOUSE_CAPACITY(CAPACITY, WAREHOUSE_ID, CATEGORY_ID) VALUES(?,?,?)")) {
                    capStmt.setInt(1, cap.getCapacity());
                    capStmt.setLong(2, entity.getId());
                    capStmt.setLong(3, cap.getCategory().getId());
                    capStmt.executeUpdate();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }
            T newItem = this.findById(entity.getId());
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), entity.getName(), LocalDateTime.now());
            unosLog.updateEntry(oldItem,newItem, "warehouse");
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
            PreparedStatement capStmt = connection.prepareStatement("DELETE FROM WAREHOUSE_CAPACITY WHERE WAREHOUSE_ID = ? ")) {
            capStmt.setLong(1, entity.getId());
            capStmt.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("DELETE " +  "FROM WAREHOUSE  WHERE ID =?" );){
            stmt.setLong(1, entity.getId());
            stmt.executeUpdate();
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), entity.toString(), LocalDateTime.now());
            unosLog.delEntry("warehouse");
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
    private T extracWarehouseFromResultSet(ResultSet resultSet) throws SQLException{
        Long id =resultSet.getLong("id");
        String name = resultSet.getString("name");
        String city = resultSet.getString("city");
        String postalCode = resultSet.getString("postal_code");
        String country = resultSet.getString("country");
        String streetName = resultSet.getString("street_name");
        String streetNumber = resultSet.getString("street_number");
        Warehouse warehouse = new Warehouse(name, city, postalCode, streetNumber, country ,streetName);
        warehouse.setId(id);
        return (T)warehouse;
    }
    private List<WareCapacity> extractCapacatyList(ResultSet rez){
        List<WareCapacity> result = new ArrayList<>();
        CategoryRepository<Category> cRep = new CategoryRepository<>();
        try {while(rez.next()){
            Long catId = rez.getLong("category_id");
            Integer cap = rez.getInt("capacity");
            result.add(new WareCapacity(cRep.findById(catId),cap));
        }}catch(SQLException e){
            log.error(e.getMessage());
        }
        return result;
    }

}
