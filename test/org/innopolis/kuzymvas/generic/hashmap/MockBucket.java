package org.innopolis.kuzymvas.generic.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.generic.datastructures.KeyValuePair;

import java.util.ArrayList;
import java.util.List;

public class MockBucket<K,V> implements Bucket<K,V> {

    public boolean usedContainsKey;
    public boolean usedPut, usedReplace, usedGet, usedRemove;
    public String description;
    public boolean usedContainsPair;
    public boolean throwExceptions;
    public boolean returnOnContainsRequests;
    public int hashValue;
    public boolean hasHash;

    @Override
    public boolean put(K key, V value) {
        usedPut = true;
        return true;
    }

    @Override
    public void replace(K key, V value) throws KeyNotPresentException {
        usedReplace = true;
        if (throwExceptions) {
            throw new KeyNotPresentException("Mock Exception");
        }
    }

    @Override
    public V get(Object key) throws KeyNotPresentException {
        usedGet = true;
        if (throwExceptions) {
            throw new KeyNotPresentException("Mock Exception");
        }
        return null;
    }

    @Override
    public void remove(Object key) throws KeyNotPresentException {
        usedRemove = true;
        if (throwExceptions) {
            throw new KeyNotPresentException("Mock Exception");
        }
    }

    @Override
    public boolean containsKey(Object key) {
        usedContainsKey = true;
        return returnOnContainsRequests;
    }

    @Override
    public boolean containsPair(KeyValuePair<?,?> pair) {
        usedContainsPair = true;
        return returnOnContainsRequests;
    }

    @Override
    public void describeBucket(StringBuilder strB) {
        strB.append(description);
    }

    @Override
    public List<KeyValuePair<K, V>> getKeyValuePairs() {
        List<KeyValuePair<K, V>> mockPair = new ArrayList<>();
        mockPair.add(new KeyValuePair<>(null, null));
        return mockPair;
    }

    @Override
    public int[] getKeyValuePairsHashes() {
        if (hasHash) {
            int[] mockHash = new int[1];
            mockHash[0] = hashValue;
            return mockHash;
        }
        return new int[0];
    }

    public void clearFlags() {
        usedPut = false;
        usedGet = false;
        usedContainsKey = false;
        usedContainsPair = false;
        usedRemove = false;
        usedReplace = false;
    }
}
