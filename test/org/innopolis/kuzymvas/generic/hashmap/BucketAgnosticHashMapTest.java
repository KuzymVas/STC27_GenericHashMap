package org.innopolis.kuzymvas.generic.hashmap;

import org.innopolis.kuzymvas.generic.datastructures.KeyValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BucketAgnosticHashMapTest {

    BucketAgnosticHashMap<FixedHash, Object> hashMap;
    List<MockBucket<FixedHash, Object>> buckets;
    FixedHash negativeHashKey;
    FixedHash firstSameHashKey, secondSameHashKey;
    FixedHash differentHashKey;
    Object value;

    @Before
    public void setUp() {
        MockBucketFactory<FixedHash, Object> factory = new MockBucketFactory<>(false);
        hashMap = new BucketAgnosticHashMap<>(factory, 10);
        buckets = factory.getBackdoorToBuckets();
        negativeHashKey = new FixedHash(0, -1);
        firstSameHashKey = new FixedHash(1, 0);
        secondSameHashKey = new FixedHash(2, 0);
        differentHashKey = new FixedHash(3, 1);
        value = new Object();
    }

    @Test
    public void testConstructorException() {
        try {
            new BucketAgnosticHashMap<>(new MockBucketFactory<FixedHash, Object>(false), -1);
            Assert.fail("Was able to create hash map with a negative bucket count");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            new BucketAgnosticHashMap<>(new MockBucketFactory<FixedHash, Object>(false), 0);
            Assert.fail("Was able to create hash map with a zero bucket count");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            new BucketAgnosticHashMap<FixedHash, Object>(null, 10);
            Assert.fail("Was able to create hash map without providing a bucket factory");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testToStringTest() {
        for (int i = 0; i < buckets.size(); i++) {
            buckets.get(i).description = "bucket" + i;
        }
        final String hashMapDescription = hashMap.toString();
        System.out.println("hashMapDescription = " + hashMapDescription);
        for (int i = 0; i < buckets.size(); i++) {
            Assert.assertNotEquals("Has mMap didn't include one of bucket descriptions into its own", -1,
                                   hashMapDescription.lastIndexOf("bucket" + i));
        }
    }

    @Test
    public void testEmptySize() {

        Assert.assertEquals("Empty hash map size is incorrect", 0, hashMap.size());
        Assert.assertTrue("Empty map indicated that it's not empty",hashMap.isEmpty());
    }

    @Test
    public void testSingleKey() {
        hashMap.put(firstSameHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 1, hashMap.size());
        Assert.assertFalse("Non-empty map indicated that it's empty",hashMap.isEmpty());
        int accessedBucketNum = -1;
        int putCounter = 0;
        for (int i = 0; i < buckets.size(); i++) {
            if (buckets.get(i).usedPut) {
                accessedBucketNum = i;
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);

        hashMap.containsKey(firstSameHashKey);
        Assert.assertTrue("During 'containsKey' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedContainsKey);
        int containCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedContainsKey) {
                containCounter++;
            }
        }
        Assert.assertEquals("During 'containsKey' different amount of bucket than only one was accessed", 1,
                            containCounter);

        hashMap.get(firstSameHashKey);
        Assert.assertTrue("During 'get' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedGet);
        int getCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedGet) {
                getCounter++;
            }
        }
        Assert.assertEquals("During 'get' different amount of bucket than only one was accessed", 1, getCounter);

        hashMap.containsPair(new KeyValuePair<>(firstSameHashKey, null));
        Assert.assertTrue("During 'containsPair' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedContainsPair);
        int containPairCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedContainsPair) {
                containPairCounter++;
            }
        }
        Assert.assertEquals("During 'containsKey' different amount of bucket than only one was accessed", 1,
                            containPairCounter);

        hashMap.replace(firstSameHashKey, null);
        Assert.assertTrue("During 'replace' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedReplace);
        int replaceCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedReplace) {
                replaceCounter++;
            }
        }
        Assert.assertEquals("During 'replace' different amount of bucket than only one was accessed", 1,
                            replaceCounter);

        hashMap.remove(firstSameHashKey);
        Assert.assertEquals("Hash map size didn't go down after removal", 0, hashMap.size());
        Assert.assertTrue("Empty map indicated that it's not empty",hashMap.isEmpty());
        Assert.assertTrue("During 'remove' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedRemove);
        int removeCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedRemove) {
                removeCounter++;
            }
        }
        Assert.assertEquals("During 'remove' different amount of bucket than only one was accessed", 1, removeCounter);
    }

    @Test
    public void testSameKey() {
        hashMap.put(firstSameHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 1, hashMap.size());
        Assert.assertFalse("Non-empty map indicated that it's empty",hashMap.isEmpty());
        int accessedBucketNum = -1;
        int putCounter = 0;
        for (int i = 0; i < buckets.size(); i++) {
            if (buckets.get(i).usedPut) {
                accessedBucketNum = i;
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);
        buckets.get(accessedBucketNum).clearFlags();

        hashMap.put(secondSameHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 2, hashMap.size());
        Assert.assertFalse("Non-empty map indicated that it's empty",hashMap.isEmpty());
        Assert.assertTrue("During 'put' attempt with same hash of key same bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedPut);
        putCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedPut) {
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);

        hashMap.containsKey(secondSameHashKey);
        Assert.assertTrue("During 'containsKey' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedContainsKey);
        int containCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedContainsKey) {
                containCounter++;
            }
        }
        Assert.assertEquals("During 'containsKey' different amount of bucket than only one was accessed", 1,
                            containCounter);

        hashMap.get(secondSameHashKey);
        Assert.assertTrue("During 'get' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedGet);
        int getCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedGet) {
                getCounter++;
            }
        }
        Assert.assertEquals("During 'get' different amount of bucket than only one was accessed", 1, getCounter);

        hashMap.containsPair(new KeyValuePair<>(secondSameHashKey, null));
        Assert.assertTrue("During 'containsPair' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedContainsPair);
        int containPairCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedContainsPair) {
                containPairCounter++;
            }
        }
        Assert.assertEquals("During 'containsKey' different amount of bucket than only one was accessed", 1,
                            containPairCounter);

        hashMap.replace(secondSameHashKey, null);
        Assert.assertTrue("During 'replace' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedReplace);
        int replaceCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedReplace) {
                replaceCounter++;
            }
        }
        Assert.assertEquals("During 'replace' different amount of bucket than only one was accessed", 1,
                            replaceCounter);

        hashMap.remove(secondSameHashKey);
        Assert.assertEquals("Hash map size didn't go down after removal", 1, hashMap.size());
        Assert.assertTrue("During 'remove' attempt correct bucket wasn't accessed",
                          buckets.get(accessedBucketNum).usedRemove);
        int removeCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedRemove) {
                removeCounter++;
            }
        }
        Assert.assertEquals("During 'remove' different amount of bucket than only one was accessed", 1,
                            removeCounter);
    }

    @Test
    public void testDifferentKey() {
        hashMap.put(firstSameHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 1, hashMap.size());
        Assert.assertFalse("Non-empty map indicated that it's empty",hashMap.isEmpty());
        int accessedBucketNum = -1;
        int putCounter = 0;
        for (int i = 0; i < buckets.size(); i++) {
            if (buckets.get(i).usedPut) {
                accessedBucketNum = i;
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);
        buckets.get(accessedBucketNum).clearFlags();

        hashMap.put(differentHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 2, hashMap.size());
        Assert.assertFalse("Non-empty map indicated that it's empty",hashMap.isEmpty());
        Assert.assertFalse("During 'put' attempt with different hash of key same bucket was accessed",
                           buckets.get(accessedBucketNum).usedPut);
        putCounter = 0;
        for (MockBucket<FixedHash, Object> bucket : buckets) {
            if (bucket.usedPut) {
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);
    }


    @Test
    public void testEquals() {
        final MockBucketFactory<FixedHash, Object> factory = new MockBucketFactory<>(false);
        final BucketAgnosticHashMap<FixedHash, Object> otherHashMap = new BucketAgnosticHashMap<>(factory, 10);
        final List<MockBucket<FixedHash, Object>> otherBuckets = factory.getBackdoorToBuckets();

        hashMap.put(firstSameHashKey, null);
        hashMap.put(firstSameHashKey, null);
        hashMap.put(firstSameHashKey, null);
        Assert.assertEquals("Hash map size is incorrect", 3, hashMap.size());

        otherHashMap.put(firstSameHashKey, null);
        otherHashMap.put(firstSameHashKey, null);
        otherHashMap.put(firstSameHashKey, null);
        Assert.assertEquals("Hash map size is incorrect", 3, hashMap.size());

        Assert.assertNotEquals("Same size hash maps which doesn't contain each other elements are equal", hashMap,
                               otherHashMap);

        for (MockBucket<FixedHash, Object> bucket : buckets) {
            bucket.returnOnContainsRequests = true;
        }
        for (MockBucket<FixedHash, Object> bucket : otherBuckets) {
            bucket.returnOnContainsRequests = true;
        }
        Assert.assertEquals("Same size hash maps which does contain each other elements are not equal", hashMap,
                            otherHashMap);

        hashMap.put(firstSameHashKey, null);
        Assert.assertEquals("Hash map size is incorrect", 4, hashMap.size());
        Assert.assertNotEquals("Different size hash maps are equal", hashMap, otherHashMap);
    }

    @Test
    public void testHash() {
        // Чтобы хэш таблица ожидала два значения из корзин, имитируем, что положили в нее два элемента
        hashMap.put(firstSameHashKey, null);
        hashMap.put(firstSameHashKey, null);

        buckets.get(0).hasHash = true;
        buckets.get(1).hasHash = true;
        buckets.get(0).hashValue = 100;
        buckets.get(1).hashValue = 200;
        int firstHash = hashMap.hashCode();
        buckets.get(0).hashValue = 200;
        buckets.get(1).hashValue = 100;
        int secondHash = hashMap.hashCode();
        buckets.get(0).hashValue = 1;
        int thirdHash = hashMap.hashCode();
        Assert.assertEquals("Hash map hash  changed when hashes of key value-pairs were reordered", firstHash,
                            secondHash);
        Assert.assertNotEquals("Hash map size didn't change when hashes of key-value pairs change", firstHash,
                               thirdHash);
    }

    @Test
    public void testPutAll() {
        Map<FixedHash, Object> realMap = new HashMap<>();
        realMap.put(firstSameHashKey, null);
        realMap.put(secondSameHashKey, null);
        realMap.put(differentHashKey, null);
        hashMap.putAll(realMap);
        Assert.assertEquals("Hash map size is incorrect", 3, hashMap.size());
        Assert.assertFalse("Non-empty map indicated that it's empty",hashMap.isEmpty());
        int putCounter = 0;
        for (int i = 0; i < buckets.size(); i++) {
            if (buckets.get(i).usedPut) {
                putCounter++;
            }
        }
        Assert.assertEquals("During 'putAll' different amount of bucket than expected two was accessed", 2, putCounter);
    }

    @Test
    public void testClear() {
        Map<FixedHash, Object> realMap = new HashMap<>();
        realMap.put(firstSameHashKey, null);
        realMap.put(secondSameHashKey, null);
        realMap.put(differentHashKey, null);
        hashMap.putAll(realMap);
        Assert.assertEquals("Hash map size is incorrect", 3, hashMap.size());
        Assert.assertFalse("Non-empty map indicated that it's empty",hashMap.isEmpty());
        hashMap.clear();
        int clearCounter = 0;
        for (int i = 0; i < buckets.size(); i++) {
            if (buckets.get(i).usedClear) {
                clearCounter++;
                buckets.get(i).clear();
            }
        }
        Assert.assertEquals("During 'clear' not all buckets accessed", buckets.size(), clearCounter);
    }
}