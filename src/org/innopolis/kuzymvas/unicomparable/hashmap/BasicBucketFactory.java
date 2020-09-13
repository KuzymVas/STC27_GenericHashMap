package org.innopolis.kuzymvas.unicomparable.hashmap;

public class BasicBucketFactory implements  BucketFactory {

    /**
     * Перечисление возможных типов корзин, производимых фабрикой
     */
    public enum  BucketType {
        AVL_TREE, // Коризна, основанная на АВЛ дереве
        LIST // Корзина, основанная на односвязном списке
    }

    private final BucketType targetType;

    public BasicBucketFactory(BucketType targetType) {
        this.targetType = targetType;
    }

    @Override
    public Bucket createBucket() {
        switch (targetType) {
            case AVL_TREE: return new AVLTreeBucket();
            case LIST: return new ListBucket();
            default: return null;
        }
    }

    @Override
    public Bucket[] createBuckets(int bucketNumber) {
        final Bucket[] buckets = new Bucket[bucketNumber];
        for (int i = 0; i < bucketNumber; i++) {
            buckets[i] = createBucket();
        }
        return buckets;
    }
}
