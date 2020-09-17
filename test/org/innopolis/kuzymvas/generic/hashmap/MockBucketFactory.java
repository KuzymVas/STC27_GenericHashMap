package org.innopolis.kuzymvas.generic.hashmap;

import java.util.ArrayList;
import java.util.List;

public class MockBucketFactory<K, V> implements BucketFactory<K, V> {

    private List<MockBucket<K, V>> backdoorBuckets;

    @Override
    public Bucket<K, V> createBucket() {
        return null;
    }

    @Override
    public List<Bucket<K, V>> createBuckets(int bucketNumber) {
        backdoorBuckets = new ArrayList<>();
        final List<Bucket<K, V>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketNumber; i++) {
            backdoorBuckets.add(new MockBucket<>());
            buckets.add(backdoorBuckets.get(i));
        }
        return buckets;
    }

    public List<MockBucket<K, V>> getBackdoorToBuckets() {
        return backdoorBuckets;
    }
}
