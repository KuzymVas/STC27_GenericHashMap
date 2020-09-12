package org.innopolis.kuzymvas.unicomparable.hashmap;

public interface BucketFactory {

    Bucket createBucket();
    Bucket[] createBuckets(int bucketNumber);
}
