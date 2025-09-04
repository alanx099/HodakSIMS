package sims.hodaksims.repository;

import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.exceptions.RepositoryIntegrityException;
import sims.hodaksims.model.Entity;

import java.util.List;

/**
 * Apstraktna klasa koju nasljeđuju sve repository klase za CRUD postupke na bazi podataka
 * @param <T>
 */
public abstract class AbstractRepository<T extends Entity> {
        /**
         * Find by id vraca natrag jedan objekt koji smo potražili u bazi, objekt se definira na implementaciji
         * točnije u pozivu.
         * @param var1
         * @return
         * @throws RepositoryAccessException
         */
        public abstract T findById(Long var1) throws RepositoryAccessException;

        /**
         * Metoda find all nam vraća se zapise pojedinog entiteta unutar baze
         * @return
         * @throws RepositoryAccessException
         */
        public abstract List<T> findAll()throws RepositoryAccessException;

        /**
         * Metoda Save sprema nove entitete u bazu podataka
         * @param var1
         * @throws RepositoryAccessException
         */
        public abstract void save(T var1)throws RepositoryAccessException;

        /**
         * Funkcija update ažurira već postojeće podatke unutar baze
         * @param var1
         * @throws RepositoryAccessException
         */
        public abstract void update(T var1)throws RepositoryAccessException;

        /**
         * Metoda kojom brišemo instance objekta iz spremišta
         * @param var1
         * @throws RepositoryAccessException
         */
        public abstract void delete(T var1) throws RepositoryIntegrityException, RepositoryAccessException;
}
