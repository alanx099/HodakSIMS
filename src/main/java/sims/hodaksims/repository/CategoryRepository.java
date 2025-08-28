package sims.hodaksims.repository;

import sims.hodaksims.exceptions.EmptyRepositoryResultException;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.Category;
import sims.hodaksims.model.ChangeLog;
import sims.hodaksims.model.CurrentUser;
import sims.hodaksims.model.Entity;
import sims.hodaksims.utils.DbConUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * CategoryRepository klasa nasljeđuje abstraktnu klasu {@link AbstractRepository}
 * te njene sve metode
 * @param <T>
 */
public class CategoryRepository<T extends Category> extends AbstractRepository<T>  {
    /**
     * Metoda vraća kategorije po id-u iz baze podataka
     * @param id
     * @return
     * @throws RepositoryAccessException
     */
    @Override
    public T findById(Long id) throws RepositoryAccessException {

        try(Connection connection = DbConUtil.getConnection();
                    PreparedStatement stmt = connection.prepareStatement("SELECT CATEGORY.* FROM CATEGORY WHERE ID = ?");){

            stmt.setLong(1,id);
            ResultSet result = stmt.executeQuery();
            if(!result.next()){
                throw new EmptyRepositoryResultException("Category NOT found");
            }else{
                return this.extractCategoryFromResultSet(result);
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
        List<T> allCategories = new ArrayList<>();
        try(Connection connection = DbConUtil.getConnection()){
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT CATEGORY.* FROM CATEGORY");
            while(result.next()){
                Category catRes = this.extractCategoryFromResultSet(result);
                allCategories.add((T)catRes);
            }
            return allCategories;
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
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO CATEGORY(NAME, DESCRIPTION) VALUES(?,?)")
            ){
                stmt.setString(1,saveRes.getName());
                stmt.setString(2,saveRes.getDescription( ));
                stmt.executeUpdate();
                ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), saveRes.toString(), LocalDateTime.now());
                unosLog.newEntry("category");

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
            PreparedStatement stmt = connection.prepareStatement("UPDATE CATEGORY SET NAME = ?,DESCRIPTION=? WHERE ID=?  ")
        ){
            T oldItem = this.findById(saveRes.getId());
            stmt.setString(1,saveRes.getName());
            stmt.setString(2,saveRes.getDescription());
            stmt.setLong(3,saveRes.getId());
            stmt.executeUpdate();
            T newItem = this.findById(saveRes.getId());
            ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), saveRes.toString(), LocalDateTime.now());
            unosLog.updateEntry(oldItem,newItem, "category");
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());
        }
    }

    /**
     * Metoda kojom brišemo instance Kategorije iz spremišta
     *
     * @param delRes
     * @throws RepositoryAccessException
     */
    @Override
    public void delete(T delRes) throws RepositoryAccessException {
        try(Connection connection = DbConUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM CATEGORY WHERE ID=?")
        ){
            stmt.setLong(1,delRes.getId());
            stmt.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryAccessException(e.getMessage());

        }
        ChangeLog unosLog = new ChangeLog(CurrentUser.getInstance().getUserCur().getRole(), delRes.toString(), LocalDateTime.now());
        unosLog.delEntry("categoy");

    }

    /**
     * Metoda extractCategoryFromResultSet je helper metoda da se kod za dohvat
     * objekta iz result seta ne ponavlja
     * @param result
     * @return
     * @throws SQLException
     */
    private T extractCategoryFromResultSet(ResultSet result) throws SQLException{
        Long id = result.getLong("ID");
        String name = result.getString("NAME");
        String description = result.getString("DESCRIPTION");
        Category resCat = new Category(name,description);
        resCat.setId(id);
        return (T)resCat;

    }
}
