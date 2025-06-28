package sims.hodaksims.repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.exceptions.EmptyRepositoryResultException;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.WareCapacity;
import sims.hodaksims.model.Warehouse;
import sims.hodaksims.utils.DbConUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class WarehouseRepository<T extends Warehouse> extends AbstractRepository<T>{
    private static Logger log = LoggerFactory.getLogger(WarehouseRepository.class);
    public T findById(Long id){
        try(Connection connection = DbConUtil.getConnection()){
            PreparedStatement stmt =connection.prepareStatement("SELECT WAREHOUSE.* FROM WAREHOUSE WHERE ID = ?");
            stmt.setLong(1,id);
            ResultSet resultSet = stmt.executeQuery();
            if(!resultSet.next()){
                throw new EmptyRepositoryResultException("Not found");
            }else {
                return this.extracWarehouseFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryAccessException(e.getMessage());
        }

    }
    public List<T>findAll() throws RepositoryAccessException{
            List<T> warehouses = new ArrayList<>();
            try(Connection connection = DbConUtil.getConnection()) {
                Statement stmt =connection.createStatement();
                ResultSet resultSet = stmt.executeQuery("SELECT WAREHOUSE.* FROM WAREHOUSE");

                    while(resultSet.next()){
                        Warehouse warehouse = this.extracWarehouseFromResultSet(resultSet);
                        warehouses.add((T)warehouse);
                    }
                return warehouses;
            }catch(SQLException e) {
                throw new RepositoryAccessException(e.getMessage());
            }
    }

    public void save(T entity){
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt =connection.prepareStatement("INSERT INTO " +
                    "WAREHOUSE(NAME, CITY, COUNTRY, POSTAL_CODE, STREET_NUMBER, STREET_NAME) VALUES (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
        );){
            connection.setAutoCommit(false);
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getCity());
            stmt.setString(3,entity.getCountry());
            stmt.setString(4, entity.getPostalCode());
            stmt.setString(5, entity.getStreetNumber());
            stmt.setString(6, entity.getStreetName());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Failed to insert new warehouser");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    for (WareCapacity cap : entity.getCapacity()){
                        try(PreparedStatement capStmt = connection.prepareStatement("INSERT INTO WAREHOUSE_CAPACITY(CAPACITY, WAREHOUSE_ID, CATEGORY_ID) VALUES(?,?,?)");) {
                            capStmt.setInt(1, cap.getCapacity());
                            capStmt.setLong(2, generatedKeys.getLong(1));
                            capStmt.setLong(3, cap.getCategory().getId());
                            capStmt.executeUpdate();
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
    private T extracWarehouseFromResultSet(ResultSet resultSet) throws SQLException{
        Long id =resultSet.getLong("id");
        String name = resultSet.getString("name");
        String city = resultSet.getString("city");
        String postalCode = resultSet.getString("postal_code");
        String country = resultSet.getString("country");
        String streetName = resultSet.getString("street_name");
        String streetNumber = resultSet.getString("street_number");
        Warehouse warehouse = new Warehouse(name, city, postalCode, country, streetName, streetNumber);
        warehouse.setId(id);
        return (T)warehouse;
    }

}
