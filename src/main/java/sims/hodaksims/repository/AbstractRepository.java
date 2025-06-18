package sims.hodaksims.repository;

import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.Entity;

import java.util.List;

public abstract class AbstractRepository<T extends Entity> {
        public abstract T findById(Long var1) throws RepositoryAccessException;
        public abstract List<T> findAll()throws RepositoryAccessException;
        public abstract void save(T var1)throws RepositoryAccessException;

}
