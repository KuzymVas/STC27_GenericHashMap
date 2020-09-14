package org.innopolis.kuzymvas.unicomparable.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;
import org.innopolis.kuzymvas.unicomparable.UniComparableContainer;
import org.innopolis.kuzymvas.unicomparable.datastructures.KeyValuePair;

public class MockBucket implements Bucket {

    public boolean usedContainsKey;
    public boolean usedPut, usedReplace, usedGet, usedRemove;
    public String description;
    public boolean usedContainsPair;
    public boolean throwExceptions;
    public boolean returnOnContainsRequests;
    public int hashValue;
    public boolean hasHash;

    @Override
    public boolean put(UniComparable key, Object value) {
        usedPut = true;
        return true;
    }

    @Override
    public void replace(UniComparable key, Object value) throws KeyNotPresentException {
        usedReplace = true;
        if (throwExceptions) {
            throw new KeyNotPresentException("Mock Exception");
        }
    }

    @Override
    public Object get(UniComparable key) throws KeyNotPresentException {
        usedGet = true;
        if (throwExceptions) {
            throw new KeyNotPresentException("Mock Exception");
        }
        return null;
    }

    @Override
    public void remove(UniComparable key) throws KeyNotPresentException {
        usedRemove = true;
        if (throwExceptions) {
            throw new KeyNotPresentException("Mock Exception");
        }
    }

    @Override
    public boolean containsKey(UniComparable key) {
        usedContainsKey = true;
        return returnOnContainsRequests;
    }

    @Override
    public boolean containsPair(KeyValuePair pair) {
        usedContainsPair = true;
        return returnOnContainsRequests;
    }

    @Override
    public void describeBucket(StringBuilder strB) {
        strB.append(description);
    }

    @Override
    public KeyValuePair[] getKeyValuePairs() {
        KeyValuePair[] mockPair = new KeyValuePair[1];
        mockPair[0] = new KeyValuePair(new UniComparableContainer(0), 0);
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
