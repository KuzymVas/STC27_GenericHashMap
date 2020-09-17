package org.innopolis.kuzymvas.generic.hashmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс фабрики корзиню Позволяет возвращать корзины заданного при создании типа.
 * @param <K> - тип ключа корзины
 * @param <V> - тип значения корзины
 */
public class BasicBucketFactory<K, V> implements BucketFactory<K, V> {

    private final BucketType targetType;

    public BasicBucketFactory(BucketType targetType) {
        this.targetType = targetType;
    }

    @Override
    public Bucket<K, V> createBucket() {
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
    public List<Bucket<K, V>> createBuckets(int bucketNumber) {
        final List<Bucket<K, V>> bucketList = new ArrayList<>();
        for (int i = 0; i < bucketNumber; i++) {
            bucketList.add(createBucket());
        }
        return bucketList;
    }

    /**
     * Перечисление возможных типов корзин, производимых фабрикой
     */
    public enum BucketType {
        AVL_TREE, // Коризна, основанная на АВЛ дереве
        LIST // Корзина, основанная на односвязном списке
    }
}
