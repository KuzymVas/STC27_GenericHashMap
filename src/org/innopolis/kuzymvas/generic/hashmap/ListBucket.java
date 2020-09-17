package org.innopolis.kuzymvas.generic.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.generic.datastructures.KeyValuePair;
import org.innopolis.kuzymvas.generic.datastructures.ListNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListBucket<K,V> implements Bucket<K,V> {

    private ListNode<K,V> head;

    public ListBucket() {
        head = null;
    }

    @Override
    public boolean put(K key, V value) {
        if (head == null) {
            head = new ListNode<>(key, value);
            return true;
        } else {
            return head.putIntoList(key, value);
        }
    }

    @Override
    public void replace(K key, V value) throws KeyNotPresentException {
        if (head == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        head.replaceValue(key, value);
    }

    @Override
    public V get(Object key) throws KeyNotPresentException {
        if (head == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        return head.getValue(key);
    }

    @Override
    public void remove(Object key) throws KeyNotPresentException {
        if (head == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        head = head.removeFromList(key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (head == null) {
            return false;
        }
        return head.containsKey(key);
    }

    @Override
    public boolean containsPair(KeyValuePair<?,?> pair) {
        if (head == null) {
            return false;
        }
        return head.containsPair(pair);
    }

    @Override
    public void describeBucket(StringBuilder strB) {
        if (head == null) {
            return;
        }
        head.describeList(strB);
    }

    @Override
    public List<KeyValuePair<K,V>> getKeyValuePairs() {
        if (head == null) {
            return new ArrayList<>();
        }
        return head.getKeyValuePairs();
    }

    @Override
    public int[] getKeyValuePairsHashes() {
        if (head == null) {
            return new int[0];
        }
        return head.getKeyValuePairsHashes();
    }

    @Override
    public Map.Entry<K, V> getEntry(Object key) {
        return head.getEntry(key);
    }

    @Override
    public void clear() {
        head = null;
    }
}
