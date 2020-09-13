package org.innopolis.kuzymvas.unicomparable.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;
import org.innopolis.kuzymvas.unicomparable.datastructures.KeyValuePair;
import org.innopolis.kuzymvas.unicomparable.datastructures.ListNode;

public class ListBucket implements Bucket{

    ListNode head;

    public ListBucket() {
        head = null;
    }

    @Override
    public boolean put(UniComparable key, Object value) {
        if (head == null) {
            head = new ListNode(key, value);
            return true;
        }
        else {
            return head.putIntoList(key, value);
        }
    }

    @Override
    public void replace(UniComparable key, Object value) throws KeyNotPresentException {
        if (head == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        head.replaceValue(key, value);
    }

    @Override
    public Object get(UniComparable key) throws KeyNotPresentException {
        if (head == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        return head.getValue(key);
    }

    @Override
    public void remove(UniComparable key) throws KeyNotPresentException {
        if (head == null) {
            throw new KeyNotPresentException("Bucket is empty");
        }
        head = head.removeFromList(key);
    }

    @Override
    public boolean containsKey(UniComparable key) {
        if (head == null) {
            return  false;
        }
        return head.containsKey(key);
    }

    @Override
    public boolean containsPair(KeyValuePair pair) {
        if (head == null) {
            return  false;
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
    public KeyValuePair[] getKeyValuePairs() {
        if (head == null) {
            return new KeyValuePair[0];
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
}
