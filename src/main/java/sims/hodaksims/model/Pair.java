package sims.hodaksims.model;

public class Pair<K, V> {
    private final K key;
    private final V value;
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public K getKey() {
        return key;
    }

    @Override
    public String toString() {
        return  key +", "+ value ;
    }

    public V getValue() {
        return value;
    }

}
