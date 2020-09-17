package org.innopolis.kuzymvas.generic.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.generic.datastructures.KeyValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleEntryMockBucket<K,V> implements  Bucket<K,V> {

    private KeyValuePair<K,V> pair = null;

    @Override
    public boolean put(K key, V value) {
        if (pair != null && pair.hasKey(key)) {
            pair.setValue(value);
            return false;
        }
        else {
            pair = new KeyValuePair<>(key, value);
            return  true;
        }

    }

    @Override
    public void replace(K key, V value) throws KeyNotPresentException {
        if (pair != null && pair.hasKey(key)) {
            pair.setValue(value);
        }
        else {
            throw new KeyNotPresentException();
        }
    }

    @Override
    public V get(Object key) throws KeyNotPresentException {
        if (pair != null && pair.hasKey(key)) {
            return  pair.getValue();
        }
        else {
            throw new KeyNotPresentException();
        }
    }

    @Override
    public void remove(Object key) throws KeyNotPresentException {
        if (pair != null && pair.hasKey(key)) {
            pair = null;
        }
        else {
            throw new KeyNotPresentException();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return (pair != null && pair.hasKey(key));
    }

    @Override
    public void describeBucket(StringBuilder strB) {
        pair.describeSelf(strB);
    }

    @Override
    public List<KeyValuePair<K, V>> getKeyValuePairs() {
        List<KeyValuePair<K, V>> list = new ArrayList<>();
        if (pair != null) {
            list.add(pair);
        }
        return list;
    }

    @Override
    public int[] getKeyValuePairsHashes() {
        int[] hash = new int[1];
        hash[0] = pair.hashCode();
        return hash;
    }

    @Override
    public Map.Entry<K, V> getEntry(Object key) {
        if ((pair != null && pair.hasKey(key))) {
            return pair;
        }
        return null;
    }

    @Override
    public void clear() {
        pair = null;
    }
}
