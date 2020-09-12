package org.innopolis.kuzymvas.unicomparable.hashmap;

public class MockBucketFactory   implements  BucketFactory{

    MockBucket[] backdoorBuckets;

    @Override
    public Bucket createBucket() {
        return null;
    }

    @Override
    public Bucket[] createBuckets(int bucketNumber) {
        backdoorBuckets = new MockBucket[bucketNumber];
        Bucket[] buckets = new Bucket[bucketNumber];
        for (int i = 0; i < bucketNumber; i++) {
            backdoorBuckets[i] = new MockBucket();
            buckets[i] = backdoorBuckets[i];
        }
        return buckets;
    }

    public MockBucket[] getBackdoorToBuckets() {
        return backdoorBuckets;
    }
}
