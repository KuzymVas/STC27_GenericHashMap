package org.innopolis.kuzymvas.unicomparable.hashmap;

public class BasicBucketFactory implements  BucketFactory {

    /**
     * Перечисление возможных типов корзин, производимых фабрикой
     */
    public enum  BucketType {
        AVL_TREE // Коризна, основанная на АВЛ дереве
    }

    private final BucketType targetType;

    BasicBucketFactory(BucketType targetType) {
        this.targetType = targetType;
    }

    @Override
    public Bucket createBucket() {
        switch (targetType) {
            case AVL_TREE: return new AVLTreeBucket();
            default: return null;
        }
    }

    @Override
    public Bucket[] createBuckets(int bucketNumber) {
        Bucket[] buckets = new Bucket[bucketNumber];
        for (int i = 0; i < bucketNumber; i++) {
            buckets[i] = createBucket();
        }
        return buckets;
    }
}
