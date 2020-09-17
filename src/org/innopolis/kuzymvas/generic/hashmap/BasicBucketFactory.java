package org.innopolis.kuzymvas.generic.hashmap;

import java.util.ArrayList;
import java.util.List;

public class BasicBucketFactory<K,V> implements BucketFactory<K,V> {

    /**
     * Перечисление возможных типов корзин, производимых фабрикой
     */
    public enum BucketType {
        AVL_TREE, // Коризна, основанная на АВЛ дереве
        LIST // Корзина, основанная на односвязном списке
    }

    private final BucketType targetType;

    public BasicBucketFactory(BucketType targetType) {
        this.targetType = targetType;
    }

    @Override
    public Bucket<K,V> createBucket() {
        switch (targetType) {
            case AVL_TREE:
                return new AVLTreeBucket<>();
            case LIST:
                return new ListBucket<>();
            default:
                return null;
        }
    }

    @Override
    public List<Bucket<K,V>> createBuckets(int bucketNumber) {
        final List<Bucket<K,V>> bucketList = new ArrayList<>();
        for (int i = 0; i < bucketNumber; i++) {
            bucketList.add(createBucket());
        }
        return bucketList;
    }
}
