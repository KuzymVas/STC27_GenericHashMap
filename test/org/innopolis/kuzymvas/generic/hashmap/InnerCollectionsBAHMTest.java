package org.innopolis.kuzymvas.generic.hashmap;

import org.innopolis.kuzymvas.generic.datastructures.KeyValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class InnerCollectionsBAHMTest {

    private final static int TEST_SIZE = 16;

    private BucketAgnosticHashMap<FixedHash, Object> hashMap;
    private Set<Map.Entry<FixedHash, Object>> entrySet;
    private Set<FixedHash> keySet;
    private Collection<Object> valuesCollection;

    private List<FixedHash> keys;
    private List<Object> values;
    private List<KeyValuePair<FixedHash, Object>> entries;
    private KeyValuePair<FixedHash, Object> entryNotInMap;

    @Before
    public void setUp() {
        hashMap = new BucketAgnosticHashMap<>(
                new MockBucketFactory<>(true), TEST_SIZE);
        keys = new ArrayList<>();
        values = new ArrayList<>();
        entries = new ArrayList<>();
        for (int i = 0; i < TEST_SIZE; i++) {
            FixedHash key = new FixedHash(i, i);
            Object value = "Value_" + i;
            keys.add(key);
            values.add(value);
            entries.add(new KeyValuePair<>(key, value));
            hashMap.put(key, value);
        }
        entryNotInMap = new KeyValuePair<>(new FixedHash(TEST_SIZE, TEST_SIZE), "Value_" + TEST_SIZE);
        entrySet = hashMap.entrySet();
        keySet = hashMap.keySet();
        valuesCollection = hashMap.values();
    }

    @Test
    public void testSize() {
        Assert.assertEquals("Hash map size differs from the number of unique keys that was put into it", TEST_SIZE,
                            hashMap.size());
        Assert.assertEquals("Entry set size differs from hash map", TEST_SIZE, entrySet.size());
        Assert.assertEquals("Key set size differs from hash map", TEST_SIZE, keySet.size());
        Assert.assertEquals("Value collection size differs from hash map", TEST_SIZE, valuesCollection.size());
    }

    @Test
    public void testContents() {
        for (int i = 0; i < TEST_SIZE; i++) {
            Assert.assertTrue(
                    "Entry set missing entry [" + i + "]",
                    entrySet.contains(entries.get(i))
            );
            Assert.assertTrue(
                    "Key set missing entry [" + i + "]",
                    keySet.contains(keys.get(i))
            );
            Assert.assertTrue(
                    "Values collection missing entry [" + i + "]",
                    valuesCollection.contains(values.get(i))
            );
        }
    }

    @Test
    public void testClearEntries() {
        entrySet.clear();
        Assert.assertTrue("Clearing entry set didn't cleared hash map", hashMap.isEmpty());
        Assert.assertTrue("Clearing entry set didn't cleared entry set", entrySet.isEmpty());
        Assert.assertTrue("Clearing entry set didn't cleared key set", keySet.isEmpty());
        Assert.assertTrue("Clearing entry set didn't cleared values collection", valuesCollection.isEmpty());
    }

    @Test
    public void testClearKeys() {
        keySet.clear();
        Assert.assertTrue("Clearing key set didn't cleared hash map", hashMap.isEmpty());
        Assert.assertTrue("Clearing key set didn't cleared entry set", entrySet.isEmpty());
        Assert.assertTrue("Clearing key set didn't cleared key set", keySet.isEmpty());
        Assert.assertTrue("Clearing key set didn't cleared values collection", valuesCollection.isEmpty());
    }

    @Test
    public void testClearValues() {
        valuesCollection.clear();
        Assert.assertTrue("Clearing values collection didn't cleared hash map", hashMap.isEmpty());
        Assert.assertTrue("Clearing values collection didn't cleared entry set", entrySet.isEmpty());
        Assert.assertTrue("Clearing values collection didn't cleared key set", keySet.isEmpty());
        Assert.assertTrue("Clearing values collection didn't cleared values collection", valuesCollection.isEmpty());
    }

    @Test
    public void testAdd() {
        try {
            entrySet.add(entryNotInMap);
            Assert.fail("Entry set permitted addition");
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            keySet.add(entryNotInMap.getKey());
            Assert.fail("Key set permitted addition");
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            valuesCollection.add(entryNotInMap.getValue());
            Assert.fail("Value collection permitted addition");
        } catch (UnsupportedOperationException ignore) {
        }
    }

    @Test
    public void testRemoveEntrySet() {
        if (TEST_SIZE > 0) {
            entrySet.remove(entries.get(0));
            Assert.assertEquals("Removal from entry set didn't remove element from hash map", TEST_SIZE - 1,
                                hashMap.size());
            Assert.assertEquals("Removal from entry set didn't remove element from entry set", TEST_SIZE - 1,
                                entrySet.size());
            Assert.assertEquals("Removal from entry set didn't remove element from key set", TEST_SIZE - 1,
                                keySet.size());
            Assert.assertEquals("Removal from entry set didn't remove element from values collection", TEST_SIZE - 1,
                                valuesCollection.size());

            Assert.assertFalse("Removal from entry set didn't remove required element from hash map",
                               hashMap.containsKey(keys.get(0)));
            Assert.assertFalse("Removal from entry set didn't remove required element from entry set",
                               entrySet.contains(entries.get(0)));
            Assert.assertFalse("Removal from entry set didn't remove required element from key set",
                               keySet.contains(keys.get(0)));
            Assert.assertFalse("Removal from entry set didn't remove required element from values collection",
                               valuesCollection.contains(values.get(0)));
        }
    }

    @Test
    public void testRemoveKeySet() {
        if (TEST_SIZE > 0) {
            keySet.remove(keys.get(0));
            Assert.assertEquals("Removal from key set didn't remove element from hash map", TEST_SIZE - 1,
                                hashMap.size());
            Assert.assertEquals("Removal from key set didn't remove element from entry set", TEST_SIZE - 1,
                                entrySet.size());
            Assert.assertEquals("Removal from key set didn't remove element from key set", TEST_SIZE - 1,
                                keySet.size());
            Assert.assertEquals("Removal from key set didn't remove element from values collection", TEST_SIZE - 1,
                                valuesCollection.size());

            Assert.assertFalse("Removal from key set didn't remove required element from hash map",
                               hashMap.containsKey(keys.get(0)));
            Assert.assertFalse("Removal from key set didn't remove required element from entry set",
                               entrySet.contains(entries.get(0)));
            Assert.assertFalse("Removal from key set didn't remove required element from key set",
                               keySet.contains(keys.get(0)));
            Assert.assertFalse("Removal from key set didn't remove required element from values collection",
                               valuesCollection.contains(values.get(0)));
        }
    }

    @Test
    public void testRemoveValuesCollection() {
        try {
            valuesCollection.remove(values.get(0));
            Assert.fail("Value collection permitted removal");
        } catch (UnsupportedOperationException ignore) {
        }
    }

    @Test
    public void testAddAll() {
        List<Map.Entry<FixedHash, Object>> entryList = new ArrayList<>();
        List<FixedHash> keyList = new ArrayList<>();
        List<Object> valuesList = new ArrayList<>();

        entryList.add(entryNotInMap);
        keyList.add(entryNotInMap.getKey());
        valuesList.add(entryNotInMap.getValue());

        try {
            entrySet.addAll(entryList);
            Assert.fail("Entry set permitted addition");
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            keySet.addAll(keyList);
            Assert.fail("Key set permitted addition");
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            valuesCollection.addAll(valuesList);
            Assert.fail("Value collection permitted addition");
        } catch (UnsupportedOperationException ignore) {
        }
    }

    @Test
    public void testRemoveAllEntrySet() {
        List<Map.Entry<FixedHash, Object>> entryList = new ArrayList<>();
        if (TEST_SIZE > 0) {
            entryList.add(entries.get(0));
            entrySet.removeAll(entryList);
            Assert.assertEquals("Removal from entry set didn't remove element from hash map", TEST_SIZE - 1,
                                hashMap.size());
            Assert.assertEquals("Removal from entry set didn't remove element from entry set", TEST_SIZE - 1,
                                entrySet.size());
            Assert.assertEquals("Removal from entry set didn't remove element from key set", TEST_SIZE - 1,
                                keySet.size());
            Assert.assertEquals("Removal from entry set didn't remove element from values collection", TEST_SIZE - 1,
                                valuesCollection.size());

            Assert.assertFalse("Removal from entry set didn't remove required element from hash map",
                               hashMap.containsKey(keys.get(0)));
            Assert.assertFalse("Removal from entry set didn't remove required element from entry set",
                               entrySet.contains(entries.get(0)));
            Assert.assertFalse("Removal from entry set didn't remove required element from key set",
                               keySet.contains(keys.get(0)));
            Assert.assertFalse("Removal from entry set didn't remove required element from values collection",
                               valuesCollection.contains(values.get(0)));
        }
    }
    @Test
    public void testRemoveAllKeySet() {
        List<FixedHash> keyList = new ArrayList<>();
        if (TEST_SIZE > 0) {
            keyList.add(keys.get(0));
            keySet.removeAll(keyList);
            Assert.assertEquals("Removal from key set didn't remove element from hash map", TEST_SIZE - 1,
                                hashMap.size());
            Assert.assertEquals("Removal from key set didn't remove element from entry set", TEST_SIZE - 1,
                                entrySet.size());
            Assert.assertEquals("Removal from key set didn't remove element from key set", TEST_SIZE - 1,
                                keySet.size());
            Assert.assertEquals("Removal from key set didn't remove element from values collection", TEST_SIZE - 1,
                                valuesCollection.size());

            Assert.assertFalse("Removal from key set didn't remove required element from hash map",
                               hashMap.containsKey(keys.get(0)));
            Assert.assertFalse("Removal from key set didn't remove required element from entry set",
                               entrySet.contains(entries.get(0)));
            Assert.assertFalse("Removal from key set didn't remove required element from key set",
                               keySet.contains(keys.get(0)));
            Assert.assertFalse("Removal from key set didn't remove required element from values collection",
                               valuesCollection.contains(values.get(0)));
        }
    }

    @Test
    public void testRemoveAllValuesCollection() {
        try {
            List<Object> valuesList = new ArrayList<>();
            valuesList.add(values.get(0));
            valuesCollection.removeAll(valuesList);
            Assert.fail("Value collection permitted removal");
        } catch (UnsupportedOperationException ignore) {
        }
    }

}
