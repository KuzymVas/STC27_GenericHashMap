package org.innopolis.kuzymvas.generic.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.generic.datastructures.KeyValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class BucketsTest {

    private Bucket<Double,Object> bucket;
    private ArrayList<Double> keys;
    private ArrayList<Object> values;
    private ArrayList<Integer> shuffle;
    private Object replaceValue;
    private Double invalidKey;
    private final String bucketName;
    private final BasicBucketFactory.BucketType bucketType;

    public BucketsTest(BasicBucketFactory.BucketType bucketType, String bucketName) {
        this.bucketName = bucketName;
        this.bucketType = bucketType;
    }

    @Parameterized.Parameters
    public static Collection bucketsToTest() {
        return Arrays.asList(new Object[][]{
                {BasicBucketFactory.BucketType.AVL_TREE, "AVLTreeBucket"},
                {BasicBucketFactory.BucketType.LIST, "ListBucket"}
        });
    }

    @Before
    public void setUp() {
        // Создаем хранилище для тестирования
        ;bucket = new BasicBucketFactory<Double,Object>(bucketType).createBucket();
        // Подготавливаем массив объектов-ключей
        keys = new ArrayList<>(5);
        keys.add(1.0);
        keys.add(0.7/0.29);
        keys.add(0.3);
        keys.add(24.0);
        keys.add(15.0);
        // Подготавливаем массив объектов-значений
        values = new ArrayList<>(5);
        values.add(42);
        values.add(2L);
        values.add(null);
        values.add("");
        values.add(new Object());
        // "Случайный" порядок для имитации произвольного доступа к элементам в хранилище
        shuffle = new ArrayList<>(7);
        shuffle.add(3);
        shuffle.add(1);
        shuffle.add(0);
        shuffle.add(2);
        shuffle.add(4);
        shuffle.add(4);
        shuffle.add(2);
        // Значение для использованием в замене
        replaceValue = 0;
        // Ключ, который никогда не будет присутствовать в хранилище
        invalidKey = 0.0;
    }

    @Test
    public void testPutSingle() {
        Assert.assertTrue("Empty bucket " + bucketName + " somehow didn't create a new node when put into", bucket.put(keys.get(0), values.get(0)));
        Assert.assertTrue("Bucket " + bucketName + " doesn't contain key, that was put in it", bucket.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("Bucket " + bucketName + " returned incorrect value for a given key", values.get(0), bucket.get(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("Bucket " + bucketName + " 'get' method threw an exception.");
        }
        KeyValuePair<Double,Object> pair = new KeyValuePair<>(keys.get(0), values.get(0));
        Assert.assertTrue("Bucket " + bucketName + " doesn't contain pair, that was put in it", bucket.containsPair(pair));

    }

    @Test
    public void testPutSame() {
        Assert.assertTrue("Empty bucket " + bucketName + " somehow didn't create a new node when put into", bucket.put(keys.get(0), values.get(0)));
        Assert.assertFalse("Bucket " + bucketName + " did create a new node when a same key was put into it", bucket.put(keys.get(0), replaceValue));
        Assert.assertTrue("Bucket " + bucketName + " doesn't contain key, that was put in it", bucket.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("Bucket " + bucketName + " returned incorrect value for a given key", replaceValue, bucket.get(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("Bucket " + bucketName + " 'get' method threw an exception.");
        }
    }

    @Test
    public void testPutMultiple() {
        for (int i = 0; i < keys.size(); i++) {
            Assert.assertTrue("Bucket " + bucketName + " didn't create a new node for key, that wasn't put into it before", bucket.put(keys.get(i), values.get(i)));
        }
        for (Integer i : shuffle) {
            Assert.assertTrue("Bucket " + bucketName + " doesn't contain key[" + i + "], that was put in it", bucket.containsKey(keys.get(i)));
            try {
                Assert.assertEquals("Bucket " + bucketName + " returned incorrect value for a given key [" + i + "]", values.get(i), bucket.get(keys.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("Bucket " + bucketName + " 'get' method threw an exception at key-value [" + i + "].");
            }
            KeyValuePair<Double,Object> pair = new KeyValuePair<>(keys.get(i), values.get(i));
            Assert.assertTrue("Bucket " + bucketName + " doesn't contain pair, that was put in it at key-value [" + i + "].", bucket.containsPair(pair));
        }

    }

    @Test
    public void testReplaceSingle() {
        bucket.put(keys.get(0), values.get(0));
        try {
            bucket.replace(keys.get(0), replaceValue);
        } catch (KeyNotPresentException e) {
            Assert.fail("Bucket " + bucketName + " 'replace' method threw an exception.");
        }
        Assert.assertTrue("Bucket " + bucketName + " doesn't contain key, that was put in it", bucket.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("Bucket " + bucketName + " returned incorrect value for a given key", replaceValue, bucket.get(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("Bucket " + bucketName + " 'get' method threw an exception.");
        }
    }

    @Test
    public void testReplaceMultiple() {
        for (int i = 0; i < keys.size(); i++) {
            bucket.put(keys.get(i), values.get(i));
        }
        for (Integer i : shuffle) {
            try {
                bucket.replace(keys.get(i), replaceValue);
            } catch (KeyNotPresentException e) {
                Assert.fail("Bucket " + bucketName + " 'replace' method threw an exception.");
            }
            Assert.assertTrue("Bucket " + bucketName + " doesn't contain key[" + i + "], that was put in it", bucket.containsKey(keys.get(i)));
            try {
                Assert.assertEquals("Bucket " + bucketName + " returned incorrect value for a given key [" + i + "]", replaceValue, bucket.get(keys.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("Bucket " + bucketName + " 'get' method threw an exception at key-value [" + i + "].");
            }
        }
    }

    @Test
    public void testReplaceNegative() {
        for (int i = 0; i < keys.size(); i++) {
            bucket.put(keys.get(i), values.get(i));
        }
        try {
            bucket.replace(invalidKey, replaceValue);
            Assert.fail("Bucket " + bucketName + " 'replace' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            for (Integer i : shuffle) {
                Assert.assertTrue("Bucket " + bucketName + " doesn't contain key[" + i + "], that was put in it", bucket.containsKey(keys.get(i)));
                try {
                    Assert.assertEquals("Bucket " + bucketName + " returned incorrect value for a given key [" + i + "]", values.get(i), bucket.get(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    Assert.fail("Bucket " + bucketName + " 'get' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }

    @Test
    public void testRemoveSingle() {
        bucket.put(keys.get(0), values.get(0));
        try {
            bucket.remove(keys.get(0));
        } catch (KeyNotPresentException e) {
            Assert.fail("Bucket " + bucketName + " 'remove' method threw an exception.");
        }
        Assert.assertFalse("Bucket " + bucketName + " does contain key, that was removed from it", bucket.containsKey(keys.get(0)));
    }

    @Test
    public void testRemoveMultiple() {
        for (int i = 0; i < keys.size(); i++) {
            bucket.put(keys.get(i), values.get(i));
        }
        for (Double key : keys) { // shuffle содержит повторяющиеся значения, поэтому не используется здесь
            try {
                bucket.remove(key);
            } catch (KeyNotPresentException e) {
                Assert.fail("Bucket " + bucketName + " 'remove' method threw an exception.");
            }
            Assert.assertFalse("Bucket " + bucketName + " does contain key, that was removed from it", bucket.containsKey(key));
        }
    }

    @Test
    public void testRemoveNegative() {
        for (int i = 0; i < keys.size(); i++) {
            bucket.put(keys.get(i), values.get(i));
        }
        try {
            bucket.remove(invalidKey);
            Assert.fail("Bucket " + bucketName + " 'remove' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            for (Integer i : shuffle) {
                Assert.assertTrue("Bucket " + bucketName + " doesn't contain key[" + i + "], that was put in it", bucket.containsKey(keys.get(i)));
                try {
                    Assert.assertEquals("Bucket " + bucketName + " returned incorrect value for a given key [" + i + "]", values.get(i), bucket.get(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    Assert.fail("Bucket " + bucketName + " 'get' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }

    @Test
    public void testKeyValuePairFunctions() {
        final List<KeyValuePair<Double,Object>> pairs = new ArrayList<>();
        final int[] hashes = new int[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            pairs.add(new KeyValuePair<>(keys.get(i), values.get(i)));
            hashes[i] = pairs.get(i).hashCode();
            bucket.put(keys.get(i), values.get(i));
            Assert.assertTrue("Bucket " + bucketName + " doesn't contain key-value pair, that was put into it", bucket.containsPair(pairs.get(i)));
        }
        final List<KeyValuePair<Double,Object>> pairsFromBucket = bucket.getKeyValuePairs();
        Assert.assertEquals("List of pairs inside the bucket " + bucketName + " has a different length than amount of pairs that were put into it", pairs.size(), pairsFromBucket.size());
        for (int i = 0; i < pairs.size(); i++) {
            Assert.assertTrue("List of pairs inside the bucket " + bucketName + " does not contain pair [" + i + "], that was put into it", pairsFromBucket.contains(pairs.get(i)));
        }
        final int[] hashesFromBucket = bucket.getKeyValuePairsHashes();
        Assert.assertEquals("List of hashes inside the bucket " + bucketName + " has a different length than amount of pairs that were put into it", hashes.length, hashesFromBucket.length);
        // Нельзя использовать asList с массивом примитивных типов.
        for (int i = 0; i < hashes.length; i++) {
            boolean contains = false;
            for (int hashFromBucket : hashesFromBucket) {
                if (hashes[i] == hashFromBucket) {
                    contains = true;
                    break;
                }
            }
            Assert.assertTrue("List of hashes inside the bucket " + bucketName + " does not contain hash of pair [" + i + "], that was put into it", contains);
        }

    }


}