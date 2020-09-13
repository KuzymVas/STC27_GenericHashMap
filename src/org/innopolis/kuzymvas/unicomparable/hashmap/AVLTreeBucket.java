package org.innopolis.kuzymvas.unicomparable.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;
import org.innopolis.kuzymvas.unicomparable.datastructures.AVLTreeNode;
import org.innopolis.kuzymvas.unicomparable.datastructures.KeyValuePair;

public class AVLTreeBucket implements Bucket{

    AVLTreeNode root;

    public AVLTreeBucket() {
        root = null;
    }

    @Override
    public boolean put(UniComparable key, Object value) {
        if (root == null) {
            root = new AVLTreeNode(key, value);
            return true;
        }
        else {
            final boolean retVal = !(root.containsKey(key));
            root = root.insert(key, value);
            return  retVal;
        }
    }

    @Override
    public void replace(UniComparable key, Object value) throws KeyNotPresentException {
        if (root == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        root.replaceValue(key, value);
    }

    @Override
    public Object get(UniComparable key) throws KeyNotPresentException {
        if (root == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        return root.getValue(key);
    }

    @Override
    public void remove(UniComparable key) throws KeyNotPresentException {
        if (root == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        root = root.remove(key);
    }

    @Override
    public boolean containsKey(UniComparable key) {
        if (root == null) {
            return  false;
        }
        return root.containsKey(key);
    }

    @Override
    public boolean containsPair(KeyValuePair pair) {
        if (root == null) {
            return  false;
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
    public KeyValuePair[] getKeyValuePairs() {
        if (root == null) {
            return new KeyValuePair[0];
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
