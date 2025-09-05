package sims.hodaksims.model;

/**
 * Pair klasa kojom čuvamo 2 objekta odjednom
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> {
    private final K key;
    private final V value;

    /**
     * Pair konstruktor
     * @param key key
     * @param value value
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * getKey dohvati ključ
     * @return key
     */
    public K getKey() {
        return key;
    }

    @Override
    public String toString() {
        return  key +", "+ value ;
    }

    /**
     * getValue dohvati vrijednost
     * @return value
     */
    public V getValue() {
        return value;
    }

}
