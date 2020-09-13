package org.innopolis.kuzymvas.unicomparable.hashmap;

public interface BucketFactory {

    /**
     *  Возвращает объект корзину
     * @return - корзина
     */
    Bucket createBucket();

    /**
     * Возвращает массив корзин заданного размера
     * @param bucketNumber - количество корзин
     * @return - массив корзин заданного размера
     */
    Bucket[] createBuckets(int bucketNumber);
}
