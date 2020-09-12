package org.innopolis.kuzymvas.unicomparable.hashmap;

import org.innopolis.kuzymvas.unicomparable.datastructures.KeyValuePair;
import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;

public interface Bucket {
    boolean put(UniComparable key, Object value);

    void replace(Object key, Object value) throws KeyNotPresentException;

    Object get(UniComparable key) throws KeyNotPresentException;

    void remove(UniComparable key) throws KeyNotPresentException;

    boolean containsKey(UniComparable key);

    boolean containsPair(KeyValuePair pair);

    void describeBucket(StringBuilder strB);

    KeyValuePair[] getKeyValuePairs();

    int[] getKeyValuePairsHashes();
}
