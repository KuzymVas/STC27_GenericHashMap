package org.innopolis.kuzymvas.unicomparable.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;
import org.innopolis.kuzymvas.unicomparable.UniComparableContainer;
import org.innopolis.kuzymvas.unicomparable.datastructures.KeyValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BucketAgnosticHashMapTest {

    private static class FixedHash {

        private final int val;
        private final int hash;

        public FixedHash(int val, int hash) {
            this.val = val;
            this.hash = hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FixedHash that = (FixedHash) o;
            return (this.val == that.val);
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }


    BucketAgnosticHashMap hashMap;
    MockBucket[] buckets;
    UniComparable negativeHashKey;
    UniComparable firstSameHashKey, secondSameHashKey;
    UniComparable differentHashKey;
    Object value;

    @Before
    public void setUp() {
        MockBucketFactory factory = new MockBucketFactory();
        hashMap = new BucketAgnosticHashMap(factory, 10);
        buckets = factory.getBackdoorToBuckets();
        negativeHashKey = new UniComparableContainer(new FixedHash(0, -1));
        firstSameHashKey = new UniComparableContainer(new FixedHash(1, 0));
        secondSameHashKey = new UniComparableContainer(new FixedHash(2, 0));
        differentHashKey = new UniComparableContainer(new FixedHash(3, 1));
        value = new Object();
    }

    @Test
    public void testConstructorException() {
        try {
            new BucketAgnosticHashMap(new MockBucketFactory(), -1);
            Assert.fail("Was able to create hash map with a negative bucket count");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            new BucketAgnosticHashMap(new MockBucketFactory(), 0);
            Assert.fail("Was able to create hash map with a zero bucket count");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            new BucketAgnosticHashMap(null, 10);
            Assert.fail("Was able to create hash map without providing a bucket factory");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testToStringTest() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i].description = "bucket" + i;
        }
        String hashMapDescription = hashMap.toString();
        System.out.println("hashMapDescription = " + hashMapDescription);
        for (int i = 0; i < buckets.length; i++) {
            Assert.assertNotEquals("HashMap didn't include one of bucket descriptions into its own", -1, hashMapDescription.lastIndexOf("bucket" + i));
        }
    }

    @Test
    public void testEmptySize() {
        Assert.assertEquals("Empty hash map size is incorrect", 0, hashMap.size());
    }


    @Test
    public void testSingleKey() {
        hashMap.put(firstSameHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 1, hashMap.size());
        int accessedBucketNum = -1;
        int putCounter = 0;
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i].usedPut) {
                accessedBucketNum = i;
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);

        hashMap.containsKey(firstSameHashKey);
        Assert.assertTrue("During 'containsKey' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedContainsKey);
        int containCounter = 0;
        for (MockBucket bucket : buckets) {
            if (bucket.usedContainsKey) {
                containCounter++;
            }
        }
        Assert.assertEquals("During 'containsKey' different amount of bucket than only one was accessed", 1, containCounter);

        try {
            hashMap.get(firstSameHashKey);
            Assert.assertTrue("During 'get' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedGet);
            int getCounter = 0;
            for (MockBucket bucket : buckets) {
                if (bucket.usedGet) {
                    getCounter++;
                }
            }
            Assert.assertEquals("During 'get' different amount of bucket than only one was accessed", 1, getCounter);
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'get' method threw an exception.");
        }
        hashMap.containsPair(new KeyValuePair(firstSameHashKey, null));
        Assert.assertTrue("During 'containsPair' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedContainsPair);
        int containPairCounter = 0;
        for (MockBucket bucket : buckets) {
            if (bucket.usedContainsPair) {
                containPairCounter++;
            }
        }
        Assert.assertEquals("During 'containsKey' different amount of bucket than only one was accessed", 1, containPairCounter);

        try {
            hashMap.replace(firstSameHashKey, null);
            Assert.assertTrue("During 'replace' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedReplace);
            int replaceCounter = 0;
            for (MockBucket bucket : buckets) {
                if (bucket.usedReplace) {
                    replaceCounter++;
                }
            }
            Assert.assertEquals("During 'replace' different amount of bucket than only one was accessed", 1, replaceCounter);
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'replace' method threw an exception.");
        }

        try {
            hashMap.remove(firstSameHashKey);
            Assert.assertEquals("Hash map size didn't go down after removal", 0, hashMap.size());
            Assert.assertTrue("During 'remove' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedRemove);
            int removeCounter = 0;
            for (MockBucket bucket : buckets) {
                if (bucket.usedRemove) {
                    removeCounter++;
                }
            }
            Assert.assertEquals("During 'remove' different amount of bucket than only one was accessed", 1, removeCounter);
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'remove' method threw an exception.");
        }
    }

    @Test
    public void testSameKey() {
        hashMap.put(firstSameHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 1, hashMap.size());
        int accessedBucketNum = -1;
        int putCounter = 0;
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i].usedPut) {
                accessedBucketNum = i;
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);
        buckets[accessedBucketNum].clearFlags();

        hashMap.put(secondSameHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 2, hashMap.size());
        Assert.assertTrue("During 'put' attempt with same hash of key same bucket wasn't accessed", buckets[accessedBucketNum].usedPut);
        putCounter = 0;
        for (MockBucket bucket : buckets) {
            if (bucket.usedPut) {
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);


        hashMap.containsKey(secondSameHashKey);
        Assert.assertTrue("During 'containsKey' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedContainsKey);
        int containCounter = 0;
        for (MockBucket bucket : buckets) {
            if (bucket.usedContainsKey) {
                containCounter++;
            }
        }
        Assert.assertEquals("During 'containsKey' different amount of bucket than only one was accessed", 1, containCounter);

        try {
            hashMap.get(secondSameHashKey);
            Assert.assertTrue("During 'get' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedGet);
            int getCounter = 0;
            for (MockBucket bucket : buckets) {
                if (bucket.usedGet) {
                    getCounter++;
                }
            }
            Assert.assertEquals("During 'get' different amount of bucket than only one was accessed", 1, getCounter);
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'get' method threw an exception.");
        }

        hashMap.containsPair(new KeyValuePair(secondSameHashKey, null));
        Assert.assertTrue("During 'containsPair' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedContainsPair);
        int containPairCounter = 0;
        for (MockBucket bucket : buckets) {
            if (bucket.usedContainsPair) {
                containPairCounter++;
            }
        }
        Assert.assertEquals("During 'containsKey' different amount of bucket than only one was accessed", 1, containPairCounter);

        try {
            hashMap.replace(secondSameHashKey, null);
            Assert.assertTrue("During 'replace' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedReplace);
            int replaceCounter = 0;
            for (MockBucket bucket : buckets) {
                if (bucket.usedReplace) {
                    replaceCounter++;
                }
            }
            Assert.assertEquals("During 'replace' different amount of bucket than only one was accessed", 1, replaceCounter);
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'replace' method threw an exception.");
        }

        try {
            hashMap.remove(secondSameHashKey);
            Assert.assertEquals("Hash map size didn't go down after removal", 1, hashMap.size());
            Assert.assertTrue("During 'remove' attempt correct bucket wasn't accessed", buckets[accessedBucketNum].usedRemove);
            int removeCounter = 0;
            for (MockBucket bucket : buckets) {
                if (bucket.usedRemove) {
                    removeCounter++;
                }
            }
            Assert.assertEquals("During 'remove' different amount of bucket than only one was accessed", 1, removeCounter);
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'remove' method threw an exception.");
        }
    }

    @Test
    public void testDifferentKey() {
        hashMap.put(firstSameHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 1, hashMap.size());
        int accessedBucketNum = -1;
        int putCounter = 0;
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i].usedPut) {
                accessedBucketNum = i;
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);
        buckets[accessedBucketNum].clearFlags();

        hashMap.put(differentHashKey, value);
        Assert.assertEquals("Hash map size is incorrect", 2, hashMap.size());
        Assert.assertFalse("During 'put' attempt with different hash of key same bucket wasn accessed", buckets[accessedBucketNum].usedPut);
        putCounter = 0;
        for (MockBucket bucket : buckets) {
            if (bucket.usedPut) {
                putCounter++;
            }
        }
        Assert.assertEquals("During 'put' different amount of bucket than only one was accessed", 1, putCounter);
    }

    @Test
    public void testExceptions() {
        for(MockBucket bucket: buckets) {
            bucket.throwExceptions = true;
        }
        try {
            hashMap.get(firstSameHashKey);
            Assert.fail("Hash map hide an exception thrown during get attempt");
        } catch (KeyNotPresentException ignore) {
        }
        try {
            hashMap.replace(firstSameHashKey,null);
            Assert.fail("Hash map hide an exception thrown during replace attempt");
        } catch (KeyNotPresentException ignore) {
        }
        try {
            hashMap.remove(firstSameHashKey);
            Assert.fail("Hash map hide an exception thrown during remove attempt");
        } catch (KeyNotPresentException ignore) {
        }
    }

    @Test
    public void testEquals() {
        MockBucketFactory factory = new MockBucketFactory();
        BucketAgnosticHashMap otherHashMap = new BucketAgnosticHashMap(factory, 10);
        MockBucket[] otherBuckets = factory.getBackdoorToBuckets();

        hashMap.put(firstSameHashKey, null);
        hashMap.put(firstSameHashKey, null);
        hashMap.put(firstSameHashKey, null);
        Assert.assertEquals("Hash map size is incorrect", 3, hashMap.size());

        otherHashMap.put(firstSameHashKey, null);
        otherHashMap.put(firstSameHashKey, null);
        otherHashMap.put(firstSameHashKey, null);
        Assert.assertEquals("Hash map size is incorrect", 3, hashMap.size());

        Assert.assertNotEquals("Same size hash maps which doesn't contain each other elements are equal", hashMap, otherHashMap);

        for(MockBucket bucket: buckets) {
            bucket.returnOnContainsRequests = true;
        }
        for(MockBucket bucket: otherBuckets) {
            bucket.returnOnContainsRequests = true;
        }
        Assert.assertEquals("Same size hash maps which does contain each other elements are not equal", hashMap, otherHashMap);

        hashMap.put(firstSameHashKey, null);
        Assert.assertEquals("Hash map size is incorrect", 4, hashMap.size());
        Assert.assertNotEquals("Different size hash maps are equal", hashMap, otherHashMap);
    }

    @Test
    public void testHash() {
        // Чтобы хэш таблица ожидала два значения из корзин, имитируем, что положили в нее два элемента
        hashMap.put(firstSameHashKey,null);
        hashMap.put(firstSameHashKey,null);

        buckets[0].hasHash = true;
        buckets[1].hasHash = true;
        buckets[0].hashValue = 100;
        buckets[1].hashValue = 200;
        int firstHash = hashMap.hashCode();
        buckets[0].hashValue = 200;
        buckets[1].hashValue = 100;
        int secondHash = hashMap.hashCode();
        buckets[0].hashValue = 1;
        int thirdHash = hashMap.hashCode();
        Assert.assertEquals("Hash map hash  changed when hashes of key value-pairs were reordered", firstHash, secondHash);
        Assert.assertNotEquals("Hash map size didn't change when hashes of key-value pairs change", firstHash, thirdHash);

    }


}