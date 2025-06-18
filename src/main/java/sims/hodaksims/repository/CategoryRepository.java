package sims.hodaksims.repository;

import sims.hodaksims.exceptions.EmptyRepositoryResultException;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.Category;
import sims.hodaksims.model.Entity;
import sims.hodaksims.utils.DbConUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.util.Optional;

public class CategoryRepository<T extends Category> extends AbstractRepository<T>  {
    @Override
    public T findById(Long id) throws RepositoryAccessException {
        try(Connection connection = DbConUtil.getConnection()){
            PreparedStatement stmt = connection.prepareStatement("SELECT CATEGORY.* FROM CATEGORY WHERE ID = ?");
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

    @Override
    public void save(T saveRes) throws RepositoryAccessException {
            try(Connection connection = DbConUtil.getConnection()){
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO CATEGORY(NAME, DESCRIPTION) VALUES(?,?)");
                stmt.setString(1,saveRes.getName());
                stmt.setString(2,saveRes.getDescription());
                stmt.executeQuery();
            }catch (SQLException e){
                throw new RepositoryAccessException(e.getMessage());
            }
    }
    private T extractCategoryFromResultSet(ResultSet result) throws SQLException{
        Long id = result.getLong("ID");
        String name = result.getString("NAME");
        String description = result.getString("DESCRIPTION");
        Category resCat = new Category(name,description);
        resCat.setId(id);
        return (T)resCat;

    }
}
