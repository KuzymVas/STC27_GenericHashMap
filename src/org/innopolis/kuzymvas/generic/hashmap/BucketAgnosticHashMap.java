package org.innopolis.kuzymvas.generic.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.generic.datastructures.KeyValuePair;

import java.util.*;

public class BucketAgnosticHashMap<K, V> implements Map<K, V> {

    private final List<Bucket<K, V>> buckets; // Массив корзин для каждого из возможных значений хэша
    private int size; // Число пар ключ-значение в хранилище

    /**
     * Создает хэш таблицу с 1024 корзинами
     *
     * @param factory - фабрика, которая снабдит таблицу нужным типом корзин. Не может быть null
     */
    public BucketAgnosticHashMap(BucketFactory<K, V> factory) {
        this(factory, 1024);
    }

    /**
     * Создаем хэш таблицу с заданным положительным числом корзин
     *
     * @param factory      - фабрика, которая снабдит таблицу нужным типом корзин. Не может быть null
     * @param bucketNumber - число корзин. Должно быть строго больше 0
     * @throws IllegalArgumentException - выбрасывается, если число корзин меньше, либо равно нулю.
     */
    public BucketAgnosticHashMap(BucketFactory<K, V> factory, int bucketNumber) throws IllegalArgumentException {
        size = 0;
        if (factory == null) {
            throw new IllegalArgumentException("Bucket factory can't be null");
        }
        if (bucketNumber <= 0) {
            throw new IllegalArgumentException("Hash map can't have 0 or less buckets");
        }
        buckets = factory.createBuckets(bucketNumber);
    }

    @Override
    public V put(K key, V value) {
        final int keyHash = getKeyBucket(key);
        V oldValue = null;
        if (buckets.get(keyHash).containsKey(key)) {
            try {
                oldValue = buckets.get(keyHash).get(key);
            } catch (KeyNotPresentException e) {
                e.printStackTrace();
            }
        }
        if (buckets.get(keyHash).put(key, value)) {
            size++;
        }
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        final int keyHash = getKeyBucket(key);
        try {
            final V value = buckets.get(keyHash).get(key);
            buckets.get(keyHash).remove(key);
            size--;
            return value;
        } catch (KeyNotPresentException e) {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V replace(K key, V value) {
        final int keyHash = getKeyBucket(key);
        try {
            final V oldValue = buckets.get(keyHash).get(key);
            buckets.get(keyHash).replace(key, value);
            return  oldValue;
        } catch (KeyNotPresentException e) {
            return null;
        }
    }


    public boolean containsPair(KeyValuePair<?, ?> pair) {
        final int keyHash = getKeyBucket(pair.getKey());
        return buckets.get(keyHash).containsPair(pair);
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        final int keyHash = getKeyBucket(key);
        return buckets.get(keyHash).containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        final int keyHash = getKeyBucket(key);
        try {
            return buckets.get(keyHash).get(key);
        } catch (KeyNotPresentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        final StringBuilder strB = new StringBuilder("SimpleHashMap{ size=").append(size).append(", buckets=");
        for (int i = 0; i < buckets.size(); i++) {
            strB.append("bucket[").append(i).append("]{");
            buckets.get(i).describeBucket(strB);
            strB.append((i < buckets.size() - 1) ? "}," : "}");
        }
        strB.append(" }");
        return strB.toString();
    }

    /**
     * Проверяет эквиваленность данной хэш таблицы объекту.
     * Для эквивалентности объект должен реализовывать интерфейс unicomparable.hashmap.HashMap
     * и содержать в точности те же пары ключ-значение, что и эта таблица.
     * Внутренняя стрктура объекта не важна для эквивалентности.
     *
     * @param o - объект по отношению к которому проверяется эквивалетность
     * @return - true, если таблица и объект эквивалентны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BucketAgnosticHashMap)) {
            return false;
        }
        final BucketAgnosticHashMap<?, ?> that = (BucketAgnosticHashMap<?, ?>) o;
        if (this.size != that.size()) // разные размеры -> не равны
        {
            return false;
        }
        for (Bucket<K, V> bucket : buckets) { // Из каждого ведра
            List<KeyValuePair<K, V>> pairs = bucket.getKeyValuePairs();
            for (KeyValuePair<K, V> pair : pairs) { // Каждую пару, что в нем есть
                if (!that.containsPair(pair)) { // Ищем в другом
                    return false;
                }
            }
        }
        return true;// Если размеры совпали и каждая наша пара есть в другом -> равны
    }

    /**
     * Возвращает хэш код для данной таблицы.
     * Ее хэш код зависит только от содержащихся в ней пар ключ-значение
     * и не зависит от порядка их добавления или типа корзин.
     *
     * @return - хэш код таблицы
     */
    @Override
    public int hashCode() {
        final int[] nodeHashes = new int[size]; // Массив хэшей по числу элементов
        int currPos = 0;
        for (Bucket<K, V> bucket : buckets) { // Перебираем ведра
            if (bucket != null) {
                final int[] bucketHashes = bucket.getKeyValuePairsHashes(); // Получаем все хэши в ведре
                System.arraycopy(bucketHashes, 0, nodeHashes, currPos, bucketHashes.length); // Пишем их в массив
                currPos += bucketHashes.length;
            }
        }
        Arrays.sort(nodeHashes); // Сортируем хэши, чтобы обеспечить независимость от порядка элементов в ведрах
        return Arrays.hashCode(nodeHashes); // Берем хэш сортированного массива хэшей.
    }

    /**
     * Определяет номер корзины, соответствующей ключу
     *
     * @param key - ключ
     * @return номер корзины в массиве коризн
     */
    private int getKeyBucket(Object key) {

        return (key == null) ? 0 : Math.abs(key.hashCode()) % buckets.size();
    }
}
