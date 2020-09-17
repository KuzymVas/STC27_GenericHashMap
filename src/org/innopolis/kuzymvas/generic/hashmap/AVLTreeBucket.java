package org.innopolis.kuzymvas.generic.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.generic.datastructures.AVLTreeNode;
import org.innopolis.kuzymvas.generic.datastructures.KeyValuePair;

import java.util.ArrayList;
import java.util.List;

public class AVLTreeBucket<K,V> implements Bucket<K,V> {

    private AVLTreeNode<K,V> root;

    public AVLTreeBucket() {
        root = null;
    }

    @Override
    public boolean put(K key, V value) {
        if (root == null) {
            root = new AVLTreeNode<>(key, value);
            return true;
        } else {
            final boolean retVal = !(root.containsKey(key));
            root = root.insert(key, value);
            return retVal;
        }
    }

    @Override
    public void replace(K key, V value) throws KeyNotPresentException {
        if (root == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        root.replaceValue(key, value);
    }

    @Override
    public V get(Object key) throws KeyNotPresentException {
        if (root == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        return root.getValue(key);
    }

    @Override
    public void remove(Object key) throws KeyNotPresentException {
        if (root == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        root = root.remove(key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (root == null) {
            return false;
        }
        return root.containsKey(key);
    }

    @Override
    public boolean containsPair(KeyValuePair<?,?> pair) {
        if (root == null) {
            return false;
        }
        return root.containsPair(pair);
    }

    @Override
    public void describeBucket(StringBuilder strB) {
        if (root == null) {
            return;
        }
        root.describeTree(strB);
    }

    @Override
    public List<KeyValuePair<K,V>> getKeyValuePairs() {
        if (root == null) {
            return new ArrayList<>();
        }
        return root.getKeyValuePairs();
    }

    @Override
    public int[] getKeyValuePairsHashes() {
        if (root == null) {
            return new int[0];
        }
        return root.getKeyValuePairsHashes();
    }
}
