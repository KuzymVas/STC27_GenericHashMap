package org.innopolis.kuzymvas.unicomparable.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;
import org.innopolis.kuzymvas.unicomparable.datastructures.KeyValuePair;

import java.util.Arrays;

public class BucketAgnosticHashMap implements HashMap {

    Bucket[] buckets; // Массив корзин для каждого из возможных значений хэша
    int size; // Число пар ключ-значение в хранилище

    /**
     * Создает хэш таблицу с 1024 корзинами
     *
     * @param factory - фабрика, которая снабдит таблицу нужным типом корзин. Не может быть null
     */
    public BucketAgnosticHashMap(BucketFactory factory) {
        this(factory, 1024);
    }

    /**
     * Создаем хэш таблицу с заданным положительным числом корзин
     *
     * @param factory      - фабрика, которая снабдит таблицу нужным типом корзин. Не может быть null
     * @param bucketNumber - число корзин. Должно быть строго больше 0
     * @throws IllegalArgumentException - выбрасывается, если число корзин меньше, либо равно нулю.
     */
    public BucketAgnosticHashMap(BucketFactory factory, int bucketNumber) throws IllegalArgumentException {
        size = 0;
        if (factory == null) throw new IllegalArgumentException("Bucket factory can't be null");
        if (bucketNumber <= 0) throw new IllegalArgumentException("Hash map can't have 0 or less buckets");
        buckets = factory.createBuckets(bucketNumber);
    }

    /**
     * Определяет номер корзины, соответствующей ключу
     *
     * @param key - ключ
     * @return номер корзины в массиве коризн
     */
    private int getKeyBucket(Object key) {

        return (key == null) ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }


    @Override
    public void put(UniComparable key, Object value) {
        int keyHash = getKeyBucket(key);
        if (buckets[keyHash].put(key, value))
            size++;

    }

    @Override
    public void replace(UniComparable key, Object value) throws KeyNotPresentException {
        int keyHash = getKeyBucket(key);
        try {
            buckets[keyHash].replace(key, value);
        } catch (KeyNotPresentException e) {
            throw new KeyNotPresentException("Key not found in hash map during replace attempt. Key value: " + key);
        }
    }

    @Override
    public Object get(UniComparable key) throws KeyNotPresentException {
        int keyHash = getKeyBucket(key);
        try {
            return buckets[keyHash].get(key);
        } catch (KeyNotPresentException e) {
            throw new KeyNotPresentException("Key not found in hash map during get attempt. Key value: " + key);
        }
    }

    @Override
    public Object remove(UniComparable key) throws KeyNotPresentException {
        int keyHash = getKeyBucket(key);
        try {
            Object value = buckets[keyHash].get(key);
            buckets[keyHash].remove(key);
            size--;
            return value;
        } catch (KeyNotPresentException e) {
            throw new KeyNotPresentException("Key not found in hash map during remove attempt. Key value: " + key);
        }
    }

    @Override
    public boolean containsKey(UniComparable key) {
        int keyHash = getKeyBucket(key);
        return buckets[keyHash].containsKey(key);
    }

    @Override
    public boolean containsPair(KeyValuePair pair) {
        int keyHash = getKeyBucket(pair.getKey());
        return buckets[keyHash] != null && buckets[keyHash].containsPair(pair);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder strB = new StringBuilder("SimpleHashMap{ size=").append(size).append(", buckets=");
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                strB.append("bucket[").append(i).append("]{");
                buckets[i].describeBucket(strB);
                strB.append((i < buckets.length - 1) ? "}," : "}");
            }
        }
        strB.append(" }");
        return strB.toString();
    }

    /**
     * Проверяет эквиваленность данной хэш таблицы объекту.
     * Для эквивалентности объект должен реализовывать интерфейс unicomparable.hashmap.HashMap
     * и содержать в точности те же пары ключ-значение, что и эта таблица.
     * Внутренняя стрктура объекта не важна для эквивалентности.
     * @param o - объект по отношению к которому проверяется эквивалетность
     * @return - true, если таблица и объект эквивалентны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashMap)) return false;
        HashMap that = (HashMap) o;
        if (this.size != that.size()) // разные размеры -> не равны
            return false;
        for (Bucket bucket : buckets) { // Из каждого ведра
            KeyValuePair[] pairs = bucket.getKeyValuePairs();
            for (KeyValuePair pair : pairs) { // Каждую пару, что в нем есть
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
     * @return - хэш код таблицы
     */
    @Override
    public int hashCode() {
        int[] nodeHashes = new int[size]; // Массив хэшей по числу элементов
        int currPos = 0;
        for (Bucket bucket : buckets) { // Перебираем ведра
            if (bucket != null) {
                int[] bucketHashes = bucket.getKeyValuePairsHashes(); // Получаем все хэши в ведре
                System.arraycopy(bucketHashes, 0, nodeHashes, currPos, bucketHashes.length); // Пишем их в массив
                currPos += bucketHashes.length;
            }
        }
        Arrays.sort(nodeHashes); // Сортируем хэши, чтобы обеспечить независимость от порядка элементов в ведрах
        return Arrays.hashCode(nodeHashes); // Берем хэш сортированного массива хэшей.
    }
}
