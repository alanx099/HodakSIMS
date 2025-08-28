package sims.hodaksims.repository;

import sims.hodaksims.exceptions.EmptyRepositoryResultException;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.*;
import sims.hodaksims.model.Contract;
import sims.hodaksims.utils.DbConUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ContractRepository klasa nasljeđuje abstraktnu klasu {@link AbstractRepository}
 * te njene sve metode
 * @param <T>
 */
public class ContractRepository<T extends Contract> extends AbstractRepository<T>  {
    private static final String OBJNAME = "contract";

    /**
     * Metoda vraća kategorije po id-u iz baze podataka
     * @param id
     * @return
     * @throws RepositoryAccessException
     */
    @Override
    public T findById(Long id) throws RepositoryAccessException {

        try(Connection connection = DbConUtil.getConnection();
                    PreparedStatement stmt = connection.prepareStatement("SELECT CONTRACT.* FROM CONTRACT WHERE ID = ?");){

            stmt.setLong(1,id);
            ResultSet result = stmt.executeQuery();
            if(!result.next()){
                throw new EmptyRepositoryResultException("Contract NOT found id:" + id.intValue());
            }else{
                return this.extractContractFromResultSet(result);
            }

        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }

    }

    /**
     * Metoda findAll vraća listu svih kategorija unutar baze
     * @return
     * @throws RepositoryAccessException
     */
    @Override
    public List<T> findAll() throws RepositoryAccessException {
        List<T> allContracts = new ArrayList<>();
        try(Connection connection = DbConUtil.getConnection()){
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT CONTRACT.* FROM CONTRACT");
            while(result.next()){
                Contract contRes = this.extractContractFromResultSet(result);
                allContracts.add((T)contRes);
            }
            return allContracts;
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Metoda save zapisuje nove kategorije u bazu
     * @param saveRes
     * @throws RepositoryAccessException
     */
    @Override
    public void save(T saveRes) throws RepositoryAccessException {
            try(Connection connection = DbConUtil.getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO CONTRACT(DATE_START, DATE_END, SUPPLIER_ID, WAREHOUSE_ID) VALUES ( ?,?,?,? )")
            ){
                stmt.setDate(1, Date.valueOf(saveRes.getStartDate()));
                stmt.setDate(2, Date.valueOf(saveRes.getEndDate()));
                stmt.setLong(3,saveRes.getSupplier().getId());
                stmt.setLong(4,saveRes.getWarehouse().getId());

                stmt.executeUpdate();
                ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), saveRes.toString(), LocalDateTime.now());
                unosLog.newEntry(OBJNAME);

            }catch (SQLException e){
                throw new RepositoryAccessException(e.getMessage());
            }
    }

    /**
     * Metoda update ažurira postojeće kategorije u bazi
     * @param saveRes
     * @throws RepositoryAccessException
     */
    @Override
    public void update(T saveRes) throws RepositoryAccessException {
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE CONTRACT SET SUPPLIER_ID = ?,WAREHOUSE_ID = ?,DATE_START = ?,DATE_END = ? WHERE ID=?  ");
        ){
            T oldItem = this.findById(saveRes.getId());
            stmt.setLong(1,saveRes.getSupplier().getId());
            stmt.setLong(2,saveRes.getWarehouse().getId());
            stmt.setDate(3, Date.valueOf(saveRes.getStartDate()));
            stmt.setDate(4, Date.valueOf(saveRes.getEndDate()));
            stmt.setLong(5,saveRes.getId());
            stmt.executeUpdate();
            T newItem = this.findById(saveRes.getId());
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), saveRes.toString(), LocalDateTime.now());
            unosLog.updateEntry(oldItem,newItem, OBJNAME);
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Metoda kojom brišemo instance Kategorije iz spremišta
     *
     *
     * @param delRes
     * @throws RepositoryAccessException
     */
    @Override
    public void delete(T delRes) throws RepositoryAccessException {
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM CONTRACT WHERE ID=?")
        ){
            stmt.setLong(1,delRes.getId());
            stmt.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
        ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), delRes.toString(), LocalDateTime.now());
        unosLog.delEntry(OBJNAME);
        unosLog.delEntry(OBJNAME);

    }

    /**
     * Metoda extractContractFromResultSet je helper metoda da se kod za dohvat
     * objekta iz result seta ne ponavlja
     * @param result
     * @return
     * @throws SQLException
     */
    private T extractContractFromResultSet(ResultSet result) throws SQLException{
        SupplierRepository<Supplier> supplierRepository = new SupplierRepository<>();
        WarehouseRepository<Warehouse> warehouseRepository = new WarehouseRepository<>();
        Long id = result.getLong("ID");
        LocalDate startDate = result.getDate("DATE_START").toLocalDate();
        LocalDate endDate = result.getDate("DATE_END").toLocalDate();
        Supplier supplierId = supplierRepository.findById(result.getLong("SUPPLIER_ID"));
        Warehouse wareHouseId = warehouseRepository.findById(result.getLong("WAREHOUSE_ID"));
        Contract resCont = new Contract(supplierId, wareHouseId, startDate, endDate);
        resCont.setId(id);
        return (T)resCont;

    }
}

