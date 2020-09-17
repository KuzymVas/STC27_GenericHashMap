package org.innopolis.kuzymvas.generic.hashmap;

import java.util.List;

public interface BucketFactory<K,V> {

    /**
     * Возвращает объект корзину
     *
     * @return - корзина
     */
    Bucket<K,V> createBucket();

    /**
     * Возвращает массив корзин заданного размера
     *
     * @param bucketNumber - количество корзин
     * @return - массив корзин заданного размера
     */
    List<Bucket<K,V>> createBuckets(int bucketNumber);
}
