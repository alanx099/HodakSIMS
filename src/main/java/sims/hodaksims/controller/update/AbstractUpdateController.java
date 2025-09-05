package sims.hodaksims.controller.update;


public abstract class AbstractUpdateController<T> {
    /**
     * Metoda koja postavlja sve vrijednosto objekta u formu
     * @param item objekt bilo kojeg tipa
     */
    public abstract void setFields(T item);
}
